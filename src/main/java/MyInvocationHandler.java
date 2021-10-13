import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MyInvocationHandler implements InvocationHandler {
    private final Object target;

    public MyInvocationHandler(Object target) {
        this.target = target;
    }

    public static Object proxyFor(Object target, Class[] classes) {
        // создание динамического прокси
        InvocationHandler handler = new MyInvocationHandler(target);
        ClassLoader classLoader = target.getClass().getClassLoader();
        return Proxy.newProxyInstance(classLoader, classes, handler);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        //System.out.println(target.getClass().getMethod(method.getName()).getAnnotation(LogAnnotation.class));
        Class<?>[] parameterTypes = method.getParameterTypes();
        Method methodTemplate = this.target.getClass().getMethod(method.getName(), parameterTypes );
        Annotation[] annotations = methodTemplate.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(LogAnnotation.class)) {
                String name = method.getName();
                //Старт метода
                System.out.println("Method " + name + " starting work." );
                Object result = run(method, args);
                //отчитываемся, что отработал метод
                System.out.println("Method " + name + " finishing work.");
                return result;
            }
        }
        return run(method, args);
    }

    private Object run(Method method, Object[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Class[] types = method.getParameterTypes();
        Method realMethod = target.getClass().getMethod(method.getName(), types);
        return realMethod.invoke(target, args);
    }
}