import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import javax.swing.*;

public class CloseAccount extends JFrame
{
    JLabel closePrompt = new JLabel("Are you certain?                                                                ");
    JButton cancelButton = new JButton("Cancel");
    JButton confirmButton = new JButton("Confirm");

    public CloseAccount(String accountNumber, int accountID) throws SQLException
    {
        super("Closing the account number " + accountNumber);
        setResizable(false);
        GroupLayout withdrawalFromAccountDialogLayout = new GroupLayout(getContentPane());
        getContentPane().setLayout(withdrawalFromAccountDialogLayout);
        withdrawalFromAccountDialogLayout.setAutoCreateGaps(true);
        withdrawalFromAccountDialogLayout.setAutoCreateContainerGaps(true);
        withdrawalFromAccountDialogLayout.setHorizontalGroup(withdrawalFromAccountDialogLayout
                .createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(withdrawalFromAccountDialogLayout.createSequentialGroup()
                        .addComponent(closePrompt, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(withdrawalFromAccountDialogLayout.createSequentialGroup()
                        .addComponent(cancelButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(confirmButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        withdrawalFromAccountDialogLayout.setVerticalGroup(withdrawalFromAccountDialogLayout.createSequentialGroup()
                .addGroup(withdrawalFromAccountDialogLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(closePrompt))
                .addGroup(withdrawalFromAccountDialogLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(cancelButton).addComponent(confirmButton)));
        cancelButton.addActionListener(e ->
        {
            Bank.closingON = !Bank.closingON;
            CloseAccount.this.dispose();
        });
        confirmButton.addActionListener(e ->
        {
            Bank.closingON = !Bank.closingON;

            try
            {
                Bank.accDBInstance.CloseAccount(accountID);
                Bank.RefreshDropBox();
            }
            catch (SQLException e1)
            {
                e1.printStackTrace();
            }
            CloseAccount.this.dispose();
        });
        CloseAccount.this.addWindowListener(new WindowListener()
        {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e)
            {
                Bank.closingON = !Bank.closingON;
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
