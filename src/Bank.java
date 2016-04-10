import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;

public class Bank
{
    static JFrame mainFrame;
    static AccountsDatabase accDBInstance;

    static boolean reportON = false;
    static boolean depositON = false;
    static boolean withdrawalON = false;
    static boolean openingON = false;
    static boolean closingON = false;

    static final String accSelectionDefault = " - Select an account - ";
    static String[] accountNumbersSet = new String[20];
    static String[] accountHoldersSet = new String[20];
    static String[] accountBalancesSet = new String[20];
    static String[] accountIDsSet = new String[20];

    static JLabel accountHolderLabel = new JLabel("             - None selected - ");
    static JLabel accountBalanceLabel = new JLabel("             ");
    static JComboBox<String> chooseAccount = new JComboBox<>();

    public static void main(String[] args) throws SQLException
    {
        accDBInstance = new AccountsDatabase();
        SwingUtilities.invokeLater(() ->
        {
            try
            {
                displayMainFrame();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        });
    }

    static void displayMainFrame() throws SQLException
    {
        mainFrame = new JFrame("BANK OF EVIL (formerly Lehman brothers)");

        JButton reportButton        = new JButton(" Generate report on all accounts");
        JButton openAccountButton   = new JButton("        Open new account        ");
        JButton closeAccountButton  = new JButton("     Close selected account     ");
        JButton depositButton       = new JButton("  Deposit into selected account ");
        JButton withdrawButton      = new JButton(" Withdraw from selected account ");
        JButton quitButton          = new JButton("              Quit              ");

        RefreshDropBox();

        openAccountButton.addActionListener(e ->
        {
            if (!openingON)
            {
                openingON = !openingON;
                try
                {
                    OpenAccount openAccountDialog = new OpenAccount();
                }
                catch (SQLException e1)
                {
                    e1.printStackTrace();
                }
            }
        });

        closeAccountButton.addActionListener(e ->
        {
            if (!closingON && chooseAccount.getSelectedItem().toString() != accSelectionDefault)
            {
                closingON = !closingON;
                try
                {
                    CloseAccount closeAccountDialog = new CloseAccount(chooseAccount.getSelectedItem().toString(), Integer.valueOf(accountIDsSet[chooseAccount.getSelectedIndex()]));
                }
                catch (SQLException e1)
                {
                    e1.printStackTrace();
                }
            }
        });

        depositButton.addActionListener(e ->
        {
            if (!depositON && chooseAccount.getSelectedItem().toString() != accSelectionDefault)
            {
                depositON = !depositON;
                try
                {
                    DepositIntoAccount depositDialog = new DepositIntoAccount(chooseAccount.getSelectedItem().toString(), accountBalancesSet[chooseAccount.getSelectedIndex()], Integer.valueOf(accountIDsSet[chooseAccount.getSelectedIndex()]));
                }
                catch (SQLException e1)
                {
                    e1.printStackTrace();
                }
            }
        });

        withdrawButton.addActionListener(e ->
        {
            if (!withdrawalON && chooseAccount.getSelectedItem().toString() != accSelectionDefault)
            {
                withdrawalON = !withdrawalON;
                try
                {
                    WithdrawFromAccount withdrawalDialog = new WithdrawFromAccount(chooseAccount.getSelectedItem().toString(), accountBalancesSet[chooseAccount.getSelectedIndex()], Integer.valueOf(accountIDsSet[chooseAccount.getSelectedIndex()]));
                }
                catch (SQLException e1)
                {
                    e1.printStackTrace();
                }
            }
        });

        reportButton.addActionListener(e ->
        {
            if (!reportON)
            {
                reportON = !reportON;
                try
                {
                    AccountsReport reportDialog = new AccountsReport();
                }
                catch (SQLException e1)
                {
                    e1.printStackTrace();
                }
            }
        });

        quitButton.addActionListener(e ->
        {
            try
            {
                accDBInstance.CleanUp();
            }
            catch (SQLException e1)
            {
                e1.printStackTrace();
            }
            mainFrame.dispose();
            System.exit(0);
        });

        mainFrame.getContentPane().setLayout(new GridLayout(3, 2, 5, 5));

        mainFrame.add(openAccountButton);
        mainFrame.add(closeAccountButton);
        mainFrame.add(depositButton);
        mainFrame.add(withdrawButton);
        mainFrame.add(reportButton);
        mainFrame.add(quitButton);
        mainFrame.add(chooseAccount);
        mainFrame.add(accountHolderLabel);
        mainFrame.add(accountBalanceLabel);

        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setPreferredSize(new Dimension(700, 200));
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    public static void RefreshDropBox() throws SQLException
    {
        Statement entriesNumberStatement = null;
        final String entriesNumberQuery = "select count(*) as entries_number from bank.accounts where is_closed = 0";
        Statement testStatement = null;
        final String testQuery = "select account_id, account_number, account_holder, account_balance from bank.accounts where is_closed = 0";
        int entriesNumber = 0;
        int simpleCounter = 1;

        try
        {
            entriesNumberStatement = AccountsDatabase.accountsDBConnection.createStatement();
            ResultSet entriesNumberResult = entriesNumberStatement.executeQuery(entriesNumberQuery);
            if (entriesNumberResult != null)
            {
                entriesNumberResult.next();
                entriesNumber = entriesNumberResult.getInt("entries_number") + 1;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (entriesNumberStatement != null)
            {
                entriesNumberStatement.close();
            }
        }

        accountNumbersSet = new String[entriesNumber];
        accountHoldersSet = new String[entriesNumber];
        accountBalancesSet = new String[entriesNumber];
        accountIDsSet = new String[entriesNumber];

        accountNumbersSet[0] = accSelectionDefault;
        accountHoldersSet[0] = " - None selected - ";
        accountBalancesSet[0] = "";
        accountIDsSet[0] = "";
        chooseAccount.removeAllItems();

        try
        {
            testStatement = AccountsDatabase.accountsDBConnection.createStatement();
            ResultSet testResultSet = testStatement.executeQuery(testQuery);
            if (testResultSet != null)
            {
                while (testResultSet.next())
                {
                    accountNumbersSet[simpleCounter] = testResultSet.getString("account_number");
                    accountHoldersSet[simpleCounter] = testResultSet.getString("account_holder");
                    accountBalancesSet[simpleCounter] = testResultSet.getString("account_balance");
                    accountIDsSet[simpleCounter] = testResultSet.getString("account_id");
                    simpleCounter++;
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (testStatement != null)
            {
                testStatement.close();
            }
        }

        for (int i = 0; i < accountNumbersSet.length; i++)
        {
            chooseAccount.addItem(accountNumbersSet[i]);
        }

        ActionListener accCBListener = e ->
        {
            if (chooseAccount.getSelectedIndex() > -1)
            {
                accountBalanceLabel.setText("                      " + accountBalancesSet[chooseAccount.getSelectedIndex()]);
                accountHolderLabel.setText("                    " + accountHoldersSet[chooseAccount.getSelectedIndex()]);
            }
        };
        chooseAccount.addActionListener(accCBListener);

        if (chooseAccount.getSelectedIndex() > 1)
        {
            chooseAccount.setSelectedIndex(1);
            accountBalanceLabel.setText(accountBalancesSet[1]);
        }
        chooseAccount.setSelectedIndex(0);

        accountHolderLabel.setText("          - None selected - ");
        accountBalanceLabel.setText("");
    }
}
