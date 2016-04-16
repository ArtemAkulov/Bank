import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import javax.swing.*;

/**
 *
 * OpenAccount class processes entry of the following values to the bank database:
 * 	Account number
 * 	Holder name
 * 	Initial Balance
 * 	It assigns a newly created account Active as well.
 */
class OpenAccount extends JFrame
{
    private static final long serialVersionUID = 1L;
    private JButton cancelButton = new JButton("Cancel");
    private JButton confirmButton = new JButton("Confirm");
    private JLabel accountNumberPrompt = new JLabel("Account number  ");
    private JLabel accountHolderPrompt = new JLabel("Holder name     ");
    private JLabel accountInitialBalancePrompt = new JLabel("Initial balance ");
    private JTextField accountNumberValue = new JTextField("", 50);
    private JTextField accountHolderValue = new JTextField("", 49);
    private JTextField accountInitialBalance = new JTextField("", 48);

    /**
     *
     * @param accountNumber Account number,
     * @param accountHolder Account holder name
     * @param initialBalance and Account initial balance are passed to the method to check if the input ca be processed.
     * @return The method returns true if the entered values are consistent with the table fields format.
     */
    private boolean CheckInputForConsistency(String accountNumber, String accountHolder, String initialBalance)
    {
        if (accountNumber == null || accountHolder == null || initialBalance == null) return false;
        if (accountNumber.length() == 0 || accountHolder.length() == 0 || initialBalance.length() == 0) return false;
        for (int simpleCounter = 0; simpleCounter < accountNumber.length(); simpleCounter ++)
        {
            if (accountNumber.charAt(simpleCounter) < '0' || accountNumber.charAt(simpleCounter) > '9') return false;
        }
        try
        {
            Float.parseFloat(initialBalance);
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    /**
     * The constructor method for the class
     */
    OpenAccount()
    {
        super("Open account...");
    }

    /**
     * The method provides user interface for opening an account.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    void OpenStartUp() throws SQLException
    {
        GroupLayout openAccountDialogLayout = new GroupLayout(getContentPane());
        getContentPane().setLayout(openAccountDialogLayout);
        openAccountDialogLayout.setAutoCreateGaps(true);
        openAccountDialogLayout.setAutoCreateContainerGaps(true);
        openAccountDialogLayout.setHorizontalGroup(openAccountDialogLayout
                .createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(openAccountDialogLayout.createSequentialGroup()
                        .addComponent(accountNumberPrompt, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(accountNumberValue, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(openAccountDialogLayout.createSequentialGroup()
                        .addComponent(accountHolderPrompt, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(accountHolderValue, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(openAccountDialogLayout.createSequentialGroup()
                        .addComponent(accountInitialBalancePrompt, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(accountInitialBalance, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(openAccountDialogLayout.createSequentialGroup()
                        .addComponent(cancelButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(confirmButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        openAccountDialogLayout.setVerticalGroup(openAccountDialogLayout.createSequentialGroup()
                .addGroup(openAccountDialogLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(accountNumberPrompt).addComponent(accountNumberValue))
                .addGroup(openAccountDialogLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(accountHolderPrompt).addComponent(accountHolderValue))
                .addGroup(openAccountDialogLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(accountInitialBalancePrompt).addComponent(accountInitialBalance))
                .addGroup(openAccountDialogLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(cancelButton).addComponent(confirmButton)));

        cancelButton.addActionListener(e ->
        {
            Bank.openingON = !Bank.openingON;
            OpenAccount.this.dispose();
        });
        confirmButton.addActionListener(e ->
        {
            if (CheckInputForConsistency(accountNumberValue.getText(), accountHolderValue.getText(), accountInitialBalance.getText()))
            {
                Bank.openingON = !Bank.openingON;
                try
                {
                    Bank.accDBInstance.OpenAccount(accountNumberValue.getText(), accountHolderValue.getText(), accountInitialBalance.getText());
                    Bank.RefreshDropBox();
                }
                catch (SQLException e1)
                {
                    ExceptionHandler oops = new ExceptionHandler();
                    oops.ExceptionHandlerStartUp("SQL Exception");
                }
                OpenAccount.this.dispose();
            }
        });

        OpenAccount.this.addWindowListener(new WindowListener()
        {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e)
            {
                Bank.openingON = !Bank.openingON;
            }
            @Override
            public void windowClosed(WindowEvent e) {}
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
}

