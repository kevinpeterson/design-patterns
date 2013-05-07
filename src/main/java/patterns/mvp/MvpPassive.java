package patterns.mvp;

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
 * MVP (Passive) Pattern Demo.
 */
public class MvpPassive {

    public static void main(String[] args) {
        Model model = new DefaultModel();
        Presenter presenter = new DefaultPresenter(model);
        presenter.init();
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
     * The Presenter Interface
     */
    interface Presenter {
        void changeLevel(int newLevel);
        void init();
    }

    /**
     * The View Interface
     */
    interface View {
        void setLevelDisplay(int newLevel);
        void init();
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
     * A default implementation of the Controller.
     */
    static class DefaultPresenter implements Presenter {

        private Model model;
        private View view;

        DefaultPresenter(Model model){
            super();
            this.model = model;
            this.view = new SwingView(this);
        }

        @Override
        public void init(){
            this.view.init();
            this.model.addObserver(new Observer() {
                @Override
                public void update(Observable o, Object arg) {
                    view.setLevelDisplay(model.getLevel());
                }
            });
        }

        @Override
        public void changeLevel(int newLevel) {
            this.model.setLevel(newLevel);
        }
    }

    /**
     * A Swing View implementation
     */
    public static class SwingView extends JFrame implements View {
        private Presenter presenter;

        private JLabel label;
        private JSlider slider;

        SwingView(Presenter presenter) {
            super();
            this.presenter = presenter;
        }

        @Override
        public void setLevelDisplay(int newLevel) {
            label.setText(Integer.toString(newLevel));
        }

        @Override
        public void init() {
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
                    presenter.changeLevel(slider.getValue());
                }
            });

            panel.add(slider);

            this.getContentPane().add(panel);

            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            this.setSize(400, 200);
            this.setLocationRelativeTo(null);
            this.setVisible(true);
        }
    }
}