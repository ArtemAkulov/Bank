import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;

/**
 * <h1> BANK </h1>
 * <p>
 * The "Bank" class implements an application that simply maintains a database of bank accounts.
 * This class manages the lifecycle of bank transactions. Application can get a transaction instance by first
 * calling the mainFrame. The transaction does not begin, however, until any of the following is called by an application.
 * <ul>
 * <li>Open new account
 * <li>Deposit into selected account
 * <li>Withdraw from selected account
 * <li>Generate report on all accounts
 * <li>Close selected account
 * <li>Quit
 * </ul>
 * @author Artem Akulov
 * @author Kristine Cuer
 * @version 1.0
 * @since   2016-04-11
 *
 */
public class Bank
{
    static JFrame mainFrame;
    static AccountsDatabase accDBInstance;
    private static AccountInstance[] accountsImprint;

    static boolean reportON = false;
    static boolean depositON = false;
    static boolean withdrawalON = false;
    static boolean openingON = false;
    static boolean closingON = false;

    private static final String accSelectionDefault = " - Select an account - ";
    private static final String accHolderDefault = "- None selected -";
    private static final String mainFrameTitleDefault = "BANK OF EVIL (formerly Lehman brothers)";
    private static final String reportButtonDefault = " Generate report on all accounts";
    private static final String openButtonDefault = "        Open new account        ";
    private static final String closeButtonDefault = "     Close selected account     ";
    private static final String depositButtonDefault = "  Deposit into selected account ";
    private static final String withdrawButtonDefault = " Withdraw from selected account ";
    private static final String quitButtonDefault = "              Quit              ";
    private static JLabel accountHolderLabel = new JLabel(accHolderDefault, SwingConstants.CENTER);
    private static JLabel accountBalanceLabel = new JLabel("", SwingConstants.CENTER);
    private static JComboBox<String> chooseAccount = new JComboBox<>();

    /**
     *  Provides main entry point.
     *  @param args command line arguments:
     *  <br>
     *  @throws SQLException An exception that provides information on a database access error or other errors.
     *
     */
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
                ExceptionHandler oops = new ExceptionHandler();
                oops.ExceptionHandlerStartUp("SQL Exception");
            }
        });
    }

    /**
     * displayMainFrame Returns a mainframe to select Bank Options from.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    private static void displayMainFrame() throws SQLException
    {
        mainFrame = new JFrame(mainFrameTitleDefault);
        ImageIcon mainFrameIcon = new ImageIcon(Bank.class.getResource("perkins_1.png"));
        mainFrame.setIconImage(mainFrameIcon.getImage());
        JButton reportButton = new JButton(reportButtonDefault);
        JButton openAccountButton = new JButton(openButtonDefault);
        JButton closeAccountButton = new JButton(closeButtonDefault);
        JButton depositButton = new JButton(depositButtonDefault);
        JButton withdrawButton = new JButton(withdrawButtonDefault);
        JButton quitButton = new JButton(quitButtonDefault);

        RefreshDropBox();

        /**
         * Provides access to 'Open new account' button. Will call OpenAccount class once selected.
         * @throws SQLException An exception that provides information on a database access error or other errors.
         */
        openAccountButton.addActionListener(e ->
        {
            if (!openingON)
            {
                openingON = !openingON;
                try
                {
                    OpenAccount openAccountDialog = new OpenAccount();
                    openAccountDialog.OpenStartUp();
                }
                catch (SQLException e1)
                {
                    ExceptionHandler oops = new ExceptionHandler();
                    oops.ExceptionHandlerStartUp("SQL Exception");
                }
            }
        });
        /**
         * Provides access to 'Close selected account' button. Will call CloseAccount class once selected.
         * @throws SQLException An exception that provides information on a database access error or other errors.
         */
        closeAccountButton.addActionListener(e ->
        {
            if (!closingON && !chooseAccount.getSelectedItem().toString().equals(accSelectionDefault))
            {
                closingON = !closingON;
                try
                {
                    CloseAccount closeAccountDialog = new CloseAccount();
                    closeAccountDialog.CloseStartUp(chooseAccount.getSelectedItem().toString(), accountsImprint[chooseAccount.getSelectedIndex()].accountID);
                }
                catch (SQLException e1)
                {
                    ExceptionHandler oops = new ExceptionHandler();
                    oops.ExceptionHandlerStartUp("SQL Exception");
                }
            }
        });
        /**
         * Provides access to 'Deposit into selected account' button. Will call DepositIntoAccount class once selected.
         * @throws SQLException An exception that provides information on a database access error or other errors.
         */
        depositButton.addActionListener(e ->
        {
            if (!depositON && !chooseAccount.getSelectedItem().toString().equals(accSelectionDefault))
            {
                depositON = !depositON;
                try
                {
                    DepositIntoAccount depositDialog = new DepositIntoAccount(chooseAccount.getSelectedItem().toString());
                    depositDialog.DepositStartUp(accountsImprint[chooseAccount.getSelectedIndex()].accountBalance, accountsImprint[chooseAccount.getSelectedIndex()].accountID);
                }
                catch (SQLException e1)
                {
                    ExceptionHandler oops = new ExceptionHandler();
                    oops.ExceptionHandlerStartUp("SQL Exception");
                }
            }
        });
        /**
         * Provides access to 'Withdraw from selected account' button. Will call WithdrawFromAccount class once selected.
         * @throws SQLException An exception that provides information on a database access error or other errors.
         */
        withdrawButton.addActionListener(e ->
        {
            if (!withdrawalON && !chooseAccount.getSelectedItem().toString().equals(accSelectionDefault))
            {
                withdrawalON = !withdrawalON;
                try
                {
                    WithdrawFromAccount withdrawalDialog = new WithdrawFromAccount(chooseAccount.getSelectedItem().toString());
                    withdrawalDialog.WithdrawalStartUp(accountsImprint[chooseAccount.getSelectedIndex()].accountBalance, accountsImprint[chooseAccount.getSelectedIndex()].accountID);
                }
                catch (SQLException e1)
                {
                    ExceptionHandler oops = new ExceptionHandler();
                    oops.ExceptionHandlerStartUp("SQL Exception");
                }
            }
        });
        /**
         * Provides access to 'Generate report on all accounts' button. Will call AccountsReport class once selected.
         * @throws SQLException An exception that provides information on a database access error or other errors.
         */
        reportButton.addActionListener(e ->
        {
            if (!reportON)
            {
                reportON = !reportON;
                try
                {
                    AccountsReport reportDialog = new AccountsReport();
                    reportDialog.ReportStartUp();
                }
                catch (SQLException e1)
                {
                    ExceptionHandler oops = new ExceptionHandler();
                    oops.ExceptionHandlerStartUp("SQL Exception");
                }
            }
        });
        /**
         * Provides access to 'Quit' Button. Bank mainFrame will terminate once called.
         * @throws SQLException An exception that provides information on a database access error or other errors.
         */
        quitButton.addActionListener(e ->
        {
            try
            {
                accDBInstance.CleanUp();
            }
            catch (SQLException e1)
            {
                ExceptionHandler oops = new ExceptionHandler();
                oops.ExceptionHandlerStartUp("SQL Exception");
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
        mainFrame.setResizable(false);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    /**
     * RefreshDopBox() method Called by Bank.RefreshDropBox() by DepositIntoAccount, OpenAccount and WithdrawFromAccount classes.
     * No Account is selected by default. When an Account is selected from the mainFrame,
     * it will print the Account Holder and Account Balance.
     *
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    static void RefreshDropBox() throws SQLException
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
            ExceptionHandler oops = new ExceptionHandler();
            oops.ExceptionHandlerStartUp("SQL Exception");
        }
        finally
        {
            if (entriesNumberStatement != null)
            {
                entriesNumberStatement.close();
            }
        }

        accountsImprint = new AccountInstance[entriesNumber];
        accountsImprint[0] = new AccountInstance();
        accountsImprint[0].accountNumber = accSelectionDefault;
        accountsImprint[0].accountHolder = accHolderDefault;
        accountsImprint[0].accountBalance = "";
        accountsImprint[0].accountID = -1;
        chooseAccount.removeAllItems();
        try
        {
            testStatement = AccountsDatabase.accountsDBConnection.createStatement();
            ResultSet testResultSet = testStatement.executeQuery(testQuery);
            if (testResultSet != null)
            {
                while (testResultSet.next())
                {
                    accountsImprint[simpleCounter] = new AccountInstance();
                    accountsImprint[simpleCounter].accountNumber = testResultSet.getString("account_number");
                    accountsImprint[simpleCounter].accountHolder = testResultSet.getString("account_holder");
                    accountsImprint[simpleCounter].accountBalance = testResultSet.getString("account_balance");
                    accountsImprint[simpleCounter].accountID = testResultSet.getInt("account_id");
                    simpleCounter++;
                }
            }
        }
        catch (SQLException e)
        {
            ExceptionHandler oops = new ExceptionHandler();
            oops.ExceptionHandlerStartUp("SQL Exception");
        }
        finally
        {
            if (testStatement != null)
            {
                testStatement.close();
            }
        }
        for (AccountInstance i : accountsImprint)
        {
            chooseAccount.addItem(i.accountNumber);
        }

        ActionListener accCBListener = e ->
        {
            if (chooseAccount.getSelectedIndex() > -1)
            {
                accountBalanceLabel.setText(accountsImprint[chooseAccount.getSelectedIndex()].accountBalance);
                accountHolderLabel.setText(accountsImprint[chooseAccount.getSelectedIndex()].accountHolder);
            }
        };
        chooseAccount.addActionListener(accCBListener);
        if (chooseAccount.getSelectedIndex() > 1)
        {
            chooseAccount.setSelectedIndex(1);
            accountBalanceLabel.setText(accountsImprint[1].accountBalance);
        }
        chooseAccount.setSelectedIndex(0);
        accountHolderLabel.setText(accHolderDefault);
        accountBalanceLabel.setText("");
    }
}

