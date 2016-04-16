import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import javax.swing.*;

/**
 *
 * CloseAccount class manages marking an account as non-active in the database.
 *
 */
class CloseAccount extends JFrame
{
    private static final long serialVersionUID = 1L;
    private JLabel closePrompt = new JLabel();
    private JButton cancelButton = new JButton("Cancel");
    private JButton confirmButton = new JButton("Confirm");

    /**
     * The constructor method creating a JFrame and assigning a title to it.
     */
    CloseAccount()
    {
        super("Close account...");
    }

    /**
     *
     * @param accountNumber numeric identifier for ownership of an account presented to the method as a String value.
     * @param accountID a unique identifier of each accountNumber
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    void CloseStartUp(String accountNumber, int accountID) throws SQLException
    {
        closePrompt.setText("This will close the account number " + accountNumber + ". Are you certain?");

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
                ExceptionHandler oops = new ExceptionHandler();
                oops.ExceptionHandlerStartUp("SQL Exception");
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
        setResizable(false);
        setVisible(true);
    }
}

