import java.awt.Dimension;
import javax.swing.*;

/**
 * The ExceptionHandler class provides user with the relevant data in case of catching an exception in the form of pop-up messages.
 */
class ExceptionHandler extends JFrame
{
    private static final long serialVersionUID = 1L;
    private JLabel exceptionPrompt = new JLabel();
    private JButton okButton = new JButton("Okay, I understand :(");

    /**
     * ExceptionHandler() is the constructor method for the class.
     * In order to avoid warnings from the compiler it just calls a superclass constructor and assigns the title to the frame.
     */
    ExceptionHandler()
    {
        super("Something bad just happened");
    }

    /**
     *
     * @param exceptionInstance The parameter is a string determined by the caller and dependent upon a specific circumstance.
     */
    void ExceptionHandlerStartUp(final String exceptionInstance)
    {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        exceptionPrompt.setText(exceptionInstance);
        okButton.setAlignmentX(CENTER_ALIGNMENT);
        okButton.setAlignmentY(BOTTOM_ALIGNMENT);
        exceptionPrompt.setAlignmentX(CENTER_ALIGNMENT);
        exceptionPrompt.setAlignmentY(TOP_ALIGNMENT);
        ExceptionHandler.this.setPreferredSize(new Dimension(400, 70));
        getContentPane().add(exceptionPrompt);
        getContentPane().add(okButton);
        okButton.addActionListener(e -> ExceptionHandler.this.dispose());
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
}
