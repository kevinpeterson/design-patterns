package patterns.doubledispatch;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * A Double Dispatch Demo.
 *
 * Included is standard Java Behavior and a Proxy/Reflection based solution.
 */
public class DoubleDispatch {

    public static void main(String[] args) {
        Part part = new Part();
        Part widget = new Widget();
        Part gadget = new Gadget();

        Worker worker = new FactoryWorker();

        System.out.println("This is using standard Java behavior.");
        System.out.println("-------------------------------------");
        worker.assemble(part);
        worker.assemble(widget);
        worker.assemble(gadget);

        //Wrapper it with the Reflection-based Double Dispatch wrapper.
        worker = DoubleDispatchWrapper.wrap(new FactoryWorker(), Worker.class);

        System.out.println("\nThis is using Double Dispatch using Reflection.");
        System.out.println("-----------------------------------------------");
        worker.assemble(part);
        worker.assemble(widget);
        worker.assemble(gadget);
    }

    /**
     * A Proxy/Reflection based wrapper to enable Double Dispatch.
     */
    static class DoubleDispatchWrapper {

        @SuppressWarnings("unchecked")
        static <T> T wrap(final T delegate, Class<T> proxyInterface){
            return (T) Proxy.newProxyInstance(
                DoubleDispatchWrapper.class.getClassLoader(),
                new Class<?>[]{proxyInterface},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Class<?>[] params = new Class<?>[args.length];
                        for(int i=0;i<args.length;i++){
                            params[i] = args[i].getClass();
                        }

                        //Adjust the method -- get the correct one for the correct params.
                        Method correctMethod = delegate.getClass().getDeclaredMethod(method.getName(), params);

                        return correctMethod.invoke(delegate, args);
                    }
                });
        }
    }

    static class Part {}
    static class Widget extends Part {}
    static class Gadget extends Part {}

    interface Worker {
        void assemble(Part part);
        void assemble(Widget widget);
        void assemble(Gadget gadget);
    }

    static class FactoryWorker implements Worker {

        public void assemble(Part part) {
            System.out.println("Just a regular Part... I can handle that.");
        }

        public void assemble(Widget widget) {
            System.out.println("A Widget! Easy!");
        }

        public void assemble(Gadget gadget) {
            System.out.println("Gadgets are harder... this might take a while.");
        }
    }

}
