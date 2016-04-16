import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import javax.swing.*;

/**
 * The WithDrawFromAccount class is used to provide user with an option to take
 */
class WithdrawFromAccount extends JFrame
{
    private static final long serialVersionUID = 1L;
    private JLabel withdrawalPrompt = new JLabel("Enter the amount to withdraw");
    private JTextField withdrawalValue = new JTextField("", 25);
    private JButton cancelButton = new JButton("Cancel");
    private JButton confirmButton = new JButton("Confirm");

    /**
     *
     * @param accountNumber The constructor method for this class is provided with an account number.
     *                      It is used to form the frame title.
     */
    WithdrawFromAccount(String accountNumber)
    {
        super("Make a withdrawal from the account number " + accountNumber);
    }

    /**
     *
     * @param currentBalance The method is provided with the stored value of the account balance.
     *                       It is used to get the balance after withdrawal by subtracting the withdrawal amount from it.
     * @param accountID An account primary key value is also provided to the method as the means to construct a relevant SQL update query.
     * @throws SQLException The method can throw SQLException while updating the table.
     */
    void WithdrawalStartUp(String currentBalance, int accountID) throws SQLException
    {
        GroupLayout withdrawalFromAccountDialogLayout = new GroupLayout(getContentPane());
        getContentPane().setLayout(withdrawalFromAccountDialogLayout);
        withdrawalFromAccountDialogLayout.setAutoCreateGaps(true);
        withdrawalFromAccountDialogLayout.setAutoCreateContainerGaps(true);
        withdrawalFromAccountDialogLayout.setHorizontalGroup(withdrawalFromAccountDialogLayout
                .createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(withdrawalFromAccountDialogLayout.createSequentialGroup()
                        .addComponent(withdrawalPrompt, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(withdrawalValue, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(withdrawalFromAccountDialogLayout.createSequentialGroup()
                        .addComponent(cancelButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(confirmButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        withdrawalFromAccountDialogLayout.setVerticalGroup(withdrawalFromAccountDialogLayout.createSequentialGroup()
                .addGroup(withdrawalFromAccountDialogLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(withdrawalPrompt).addComponent(withdrawalValue))
                .addGroup(withdrawalFromAccountDialogLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(cancelButton).addComponent(confirmButton)));

        cancelButton.addActionListener(e ->
        {
            Bank.withdrawalON = !Bank.withdrawalON;
            WithdrawFromAccount.this.dispose();
        });

        confirmButton.addActionListener(e ->
        {
            Bank.withdrawalON = !Bank.withdrawalON;
            try
            {
                Bank.accDBInstance.updateAccountBalance(accountID, String.valueOf(Float.valueOf(currentBalance) - Float.valueOf(withdrawalValue.getText())));
                Bank.RefreshDropBox();
            }
            catch (SQLException e1)
            {
                ExceptionHandler oops = new ExceptionHandler();
                oops.ExceptionHandlerStartUp("SQL Exception");
            }
            WithdrawFromAccount.this.dispose();
        });

        WithdrawFromAccount.this.addWindowListener(new WindowListener()
        {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e)
            {
                Bank.withdrawalON = !Bank.withdrawalON;
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

