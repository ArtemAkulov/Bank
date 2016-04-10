import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import javax.swing.*;

public class DepositIntoAccount extends JFrame
{
    JLabel depositPrompt = new JLabel("Enter the amount to deposit");
    JTextField depositValue = new JTextField("", 25);
    JButton cancelButton = new JButton("Cancel");
    JButton confirmButton = new JButton("Confirm");

    public DepositIntoAccount(String accountNumber, String currentBalance, int accountID) throws SQLException
    {
        super("Make a deposit into the account number " + accountNumber);

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
                Bank.accDBInstance.updateAccountBalance(accountID, String.valueOf(Float.valueOf(depositValue.getText()) + Float.valueOf(currentBalance)));
                Bank.RefreshDropBox();
            }
            catch (SQLException e1)
            {
                e1.printStackTrace();
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
