package patterns.momento;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Stack;

/**
 * Momento Pattern Demo.
 */
public class Memento {

    public static void main(String[] args) throws IOException {
        EditorCaretaker.createAndShowGUI();
    }

    /**
     * The Originator.
     */
    static class EditorOrgininator extends JTextArea {

        EditorOrgininator(int offs, int len){
            super(offs, len);
        }

        EditorMomento save(){
            return new EditorMomento(this.getText());
        }

        void restore(EditorMomento momento){
            this.setText(momento.getState());
        }
    }

    /**
     * The Momento to keep track of state.
     */
    static class EditorMomento {

        String state;

        EditorMomento(String state){
            this.state = state;
        }

        String getState() {
            return state;
        }

        void setState(String state) {
            this.state = state;
        }
    }

    /**
     * The Caretaker.
     */
    static class EditorCaretaker extends JPanel {
        private Stack<EditorMomento> states = new Stack<EditorMomento>();

        protected EditorOrgininator editorOrgininator;

        public EditorCaretaker() {
            super(new GridBagLayout());

            editorOrgininator = new EditorOrgininator(5, 20);
            editorOrgininator.setEditable(true);
            editorOrgininator.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                    states.push(editorOrgininator.save());
                }

                @Override
                public void keyPressed(KeyEvent e) {}

                @Override
                public void keyReleased(KeyEvent e) {}
            });

            JScrollPane scrollPane = new JScrollPane(editorOrgininator);

            //Add Components to this panel.
            GridBagConstraints c = new GridBagConstraints();
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 1.0;
            c.weighty = 1.0;
            this.add(scrollPane, c);

            JButton undoButton = new JButton("Undo");
            undoButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e){
                    if(! states.empty()){
                        editorOrgininator.restore(states.pop());
                    }
                }
            });

            this.add(undoButton);
        }

        /**
         * Swing... never thought I'd see you again...
         */
        private static void createAndShowGUI() {
            JFrame frame = new JFrame("Momento Demo");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.add(new EditorCaretaker());

            //Display
            frame.pack();
            frame.setVisible(true);
        }
    }
}
