import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import javax.swing.*;

/**
 * DepositIntoAccount class manages adding some amount to an account balance.
 *
 */
class DepositIntoAccount extends JFrame
{
    private static final long serialVersionUID = 1L;
    private JLabel depositPrompt = new JLabel("Enter the amount to deposit");
    private JTextField depositValue = new JTextField("", 25);
    private JButton cancelButton = new JButton("Cancel");
    private JButton confirmButton = new JButton("Confirm");

    /**
     * The constructor method for the class.
     * @param accountNumber is passed to the method in order to construct the frame title mentioning the account number.
     */
    DepositIntoAccount(String accountNumber)
    {
        super("Make a deposit into the account number " + accountNumber);
    }

    /**
     *
     * @param currentBalance current balance deposited by the accountHolder
     * @param accountID a unique identifier of each accountNumber
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    void DepositStartUp(String currentBalance, int accountID) throws SQLException
    {
        GroupLayout depositIntoAccountDialogLayout = new GroupLayout(getContentPane());
        getContentPane().setLayout(depositIntoAccountDialogLayout);
        depositIntoAccountDialogLayout.setAutoCreateGaps(true);
        depositIntoAccountDialogLayout.setAutoCreateContainerGaps(true);
        depositIntoAccountDialogLayout.setHorizontalGroup(depositIntoAccountDialogLayout
                .createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(depositIntoAccountDialogLayout.createSequentialGroup()
                        .addComponent(depositPrompt, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(depositValue, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(depositIntoAccountDialogLayout.createSequentialGroup()
                        .addComponent(cancelButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(confirmButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        depositIntoAccountDialogLayout.setVerticalGroup(depositIntoAccountDialogLayout.createSequentialGroup()
                .addGroup(depositIntoAccountDialogLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(depositPrompt).addComponent(depositValue))
                .addGroup(depositIntoAccountDialogLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(cancelButton).addComponent(confirmButton)));

        cancelButton.addActionListener(e ->
        {
            Bank.depositON = !Bank.depositON;
            DepositIntoAccount.this.dispose();
        });

        confirmButton.addActionListener(e ->
        {
            Bank.depositON = !Bank.depositON;
            try
            {
                Float.parseFloat(depositValue.getText());
            }
            catch (Exception floatException)
            {
                ExceptionHandler oops = new ExceptionHandler();
                oops.ExceptionHandlerStartUp("The value is not of the float type");
            }
            try
            {
                Bank.accDBInstance.updateAccountBalance(accountID, String.valueOf(Float.valueOf(depositValue.getText()) + Float.valueOf(currentBalance)));
                Bank.RefreshDropBox();
            }
            catch (SQLException e1)
            {
                ExceptionHandler oops = new ExceptionHandler();
                oops.ExceptionHandlerStartUp("SQL Exception");
            }

            DepositIntoAccount.this.dispose();
        });

        DepositIntoAccount.this.addWindowListener(new WindowListener()
        {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e)
            {
                Bank.depositON = !Bank.depositON;
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

