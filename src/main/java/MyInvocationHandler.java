import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MyInvocationHandler implements InvocationHandler {
    private final Object target;
    private static Set box = new HashSet();

    public MyInvocationHandler(Object target) {
        this.target = target;
    }

    public static Object proxyFor(Object target, Class[] classes) {
        // создание динамического прокси
        InvocationHandler handler = new MyInvocationHandler(target);
        ClassLoader classLoader = target.getClass().getClassLoader();
        Method[] methods = target.getClass().getDeclaredMethods();
        for (Method m: methods) {
            Annotation[] annotations = m.getDeclaredAnnotations();
            if (Arrays.stream(annotations).anyMatch(e -> e.annotationType().equals(LogAnnotation.class))) {
                box.add(m);
            }
        }
        return Proxy.newProxyInstance(classLoader, classes, handler);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        //System.out.println(target.getClass().getMethod(method.getName()).getAnnotation(LogAnnotation.class));
        Class<?>[] parameterTypes = method.getParameterTypes();
        Method methodTemplate = this.target.getClass().getMethod(method.getName(), parameterTypes );
        if (box.contains(methodTemplate)) {
            String name = method.getName();
            //Старт метода
            System.out.println("Method " + name + " starting work." );
            Object result = run(method, args);
            //отчитываемся, что отработал метод
            System.out.println("Method " + name + " finishing work.");
            return result;
        }
        return run(method, args);
    }

    private Object run(Method method, Object[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Class[] types = method.getParameterTypes();
        Method realMethod = target.getClass().getMethod(method.getName(), types);
        return realMethod.invoke(target, args);
    }
}