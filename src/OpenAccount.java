import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import javax.swing.*;

public class OpenAccount extends JFrame
{
    JButton cancelButton = new JButton("Cancel");
    JButton confirmButton = new JButton("Confirm");
    JLabel accountNumberPrompt         = new JLabel("Account number  ");
    JLabel accountHolderPrompt         = new JLabel("Holder name     ");
    JLabel accountInitialBalancePrompt = new JLabel("Initial balance ");
    JTextField accountNumberValue = new JTextField("", 50);
    JTextField accountHolderValue = new JTextField("", 49);
    JTextField accountInitialBalance = new JTextField("", 48);

    public OpenAccount() throws SQLException
    {
        super("Open account...");
        setResizable(false);
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
            Bank.openingON = !Bank.openingON;

            try
            {
                Bank.accDBInstance.OpenAccount(accountNumberValue.getText(), accountHolderValue.getText(), accountInitialBalance.getText());
                Bank.RefreshDropBox();
            }
            catch (SQLException e1)
            {
                e1.printStackTrace();
            }
            OpenAccount.this.dispose();
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
        setVisible(true);
    }
}
