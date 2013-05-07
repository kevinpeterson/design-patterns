package patterns.state;

import java.util.Timer;
import java.util.TimerTask;

/**
 * State Pattern Demo.
 */
public class State {

    public static void main(String[] args){
        Context context = new Context();
        context.init();
    }

    /**
     * The Context.
     */
    static class Context {

        private IState currentState = new NoAlert();
        private IState offState = new RedAlert();

        private void init(){
            new Timer().scheduleAtFixedRate(new TimerTask(){
                @Override
                public void run() {
                    //swap States
                    IState temp = currentState;
                    currentState = offState;
                    offState = temp;
                }
            }, 5000, 5000);

            new Timer().scheduleAtFixedRate(new TimerTask(){
                @Override
                public void run() {
                    //print the alert every second
                    currentState.alert();
                }
            }, 0, 1000);
        }

    }

    /**
     * The State Interface.
     */
    interface IState {
        void alert();
    }

    /**
     * An all-clear IState Alert implementation.
     */
    static class NoAlert implements IState {
        @Override
        public void alert() {
            System.out.println("Everything is fine.");
        }
    }

    /**
     * A red-alert IState Alert implementation.
     */
    static class RedAlert implements IState {
        @Override
        public void alert() {
            System.out.println("Panic!");
        }
    }

}
