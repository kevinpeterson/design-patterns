package patterns.mvc;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.Observer;

/**
 * MVC Pattern Demo.
 */
public class Mvc {

    public static void main(String[] args) {
        Model model = new DefaultModel();
        Controller controller = new DefaultController(model);

        //Use two Views with the same Model and Controller to
        //see how they all interact with each other.

        //A View in a Swing GUI
        SwingView swingView = new SwingView(model, controller);
        swingView.init();

        //A View to display and get input from the command line
        CommandLineView commandLineView = new CommandLineView(model, controller);
        commandLineView.init();
    }

    /**
     * The Model Interface
     */
    interface Model {
        int getLevel();
        void setLevel(int newLevel);
        void addObserver(Observer o);
    }

    /**
     * A default implementation of the Model
     */
    static class DefaultModel extends Observable implements Model {
        private int level;

        @Override
        public int getLevel() {
            return level;
        }

        @Override
        public void setLevel(int level) {
            this.level = level;
            this.setChanged();
            this.notifyObservers();
        }
    }

    /**
     * The Controller Interface
     */
    interface Controller {
        void changeLevel(int newLevel);
    }

    /**
     * A default implementation of the Controller.
     */
    static class DefaultController implements Controller {

        private Model model;

        DefaultController(Model model){
            super();
            this.model = model;
        }

        @Override
        public void changeLevel(int newLevel) {
            this.model.setLevel(newLevel);
        }
    }

    /**
     * A Swing View implementation
     */
    public static class SwingView extends JFrame {
        private Model model;
        private Controller controller;

        private JLabel label;
        private JSlider slider;

        SwingView(Model model, Controller controller) {
            super();
            this.model = model;
            this.controller = controller;
        }

        void init() {
            this.getContentPane().setLayout(new FlowLayout());

            JPanel panel = new JPanel();

            label = new JLabel("0");
            label.setFont(new Font("Arial", Font.BOLD, 24));
            panel.add(label);

            slider = new JSlider(JSlider.HORIZONTAL, 1, 5, 1);
            slider.setMajorTickSpacing(5);
            slider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    controller.changeLevel(slider.getValue());
                }
            });

            panel.add(slider);

            this.getContentPane().add(panel);

            this.model.addObserver(new Observer() {
                @Override
                public void update(Observable o, Object arg) {
                    label.setText(Integer.toString(model.getLevel()));
                }
            });

            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            this.setSize(400, 200);
            this.setLocationRelativeTo(null);
            this.setVisible(true);
        }
    }

    /**
     * A command line View implementation
     */
    public static class CommandLineView {
        private Model model;
        private Controller controller;

        CommandLineView(Model model, Controller controller) {
            super();
            this.model = model;
            this.controller = controller;
        }

        void init() {
            this.model.addObserver(new Observer() {
                @Override
                public void update(Observable o, Object arg) {
                    System.out.println(model.getLevel());
                }
            });

            final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            new Thread(new Runnable(){
                @Override
                public void run() {
                    while(true){
                        try {
                            controller.changeLevel(Integer.parseInt(br.readLine()));
                        } catch (NumberFormatException e){
                            System.err.println("Error parsing number: " + e.getMessage());
                        } catch (IOException e) {
                            throw new IllegalStateException(e);
                        }
                    }
                }
            }).start();

        }
    }
}