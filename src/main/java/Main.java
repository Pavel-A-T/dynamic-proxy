public class Main {
    public static void main(String[] args) throws InterruptedException {
        Example ex = new Example();
        Examplable proxy = (Examplable) MyInvocationHandler.proxyFor(ex, new Class[]{Examplable.class});
        proxy.calculate();
        proxy.doSome();
        ex.calculate();
        ex.doSome();

    }
}
