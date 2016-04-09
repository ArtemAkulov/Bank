import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import javax.swing.*;

public class WithdrawFromAccount extends JFrame
{
    JLabel withdrawalPrompt = new JLabel("Enter the amount to withdraw");
    JTextField withdrawalValue = new JTextField("", 25);
    JButton cancelButton = new JButton("Cancel");
    JButton confirmButton = new JButton("Confirm");

    public WithdrawFromAccount(String accountNumber, String currentBalance, int accountID) throws SQLException
    {
        super("Make a withdrawal from the account number " + accountNumber);
        setResizable(false);
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
                e1.printStackTrace();
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
        setVisible(true);
    }
}
