import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * The AccountsReport class constructs full report on all the accounts in the system regardless of their status.
 * It is, due to the nature of the assignment, highly independent of the rest of the classes and contacts database directly.
 */
class AccountsReport extends JFrame
{
    private static final long serialVersionUID = 1L;
    private JButton closeWindowButton = new JButton("Close report");
    private DefaultTableModel accountsTableModel = new DefaultTableModel(new Object[][][][][]{},new String[]{"Account number","Account holder","Date of opening", "Account balance", "Active"});
    private JTable reportTable = new JTable(accountsTableModel);

    /**
     * The constructor method for the report.
     */
    AccountsReport()
    {
        super("Report on all accounts");
    }

    /**
     * A query is made on the bank database and will display in a table format the following details:
     * 	Account number
     *	Account holder
     *	date of opening
     * 	account balance
     * 	Active
     *
     * @throws SQLException An exception that provides information on a database access error or other errors.
     * @throws SQLException Due to the fact that the class contacts the database directly the method can throw SQLException.
     * The method constructs the frame with the table containing all the current data on all accounts.
     */
    void ReportStartUp() throws SQLException
    {
        Statement reportStatement = null;
        Statement entriesNumberStatement = null;
        final String entriesNumberQuery = "select account_id from bank.accounts order by account_id desc limit 1";
        final String reportQuery = "select account_number, account_holder, account_balance, date_opened, is_closed from bank.accounts";
        String activeAccountString;
        int entriesNumber = 0;

        try
        {
            entriesNumberStatement = AccountsDatabase.accountsDBConnection.createStatement();
            ResultSet entriesNumberResult = entriesNumberStatement.executeQuery(entriesNumberQuery);
            if (entriesNumberResult.next())
            {
                entriesNumber = entriesNumberResult.getInt("account_id");
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

        try
        {
            reportStatement = AccountsDatabase.accountsDBConnection.createStatement();
            ResultSet reportResultSet = reportStatement.executeQuery(reportQuery);
            for (int i = 1; i <= entriesNumber; i ++)
            {
                reportResultSet.next();
                activeAccountString = (reportResultSet.getString("is_closed").charAt(0) == '1') ? "No" : "Yes";
                accountsTableModel.addRow(new Object[]{reportResultSet.getString("account_number"), reportResultSet.getString("account_holder"), reportResultSet.getString("date_opened"), reportResultSet.getString("account_balance"), activeAccountString});
            }
        }
        catch (SQLException e)
        {
            ExceptionHandler oops = new ExceptionHandler();
            oops.ExceptionHandlerStartUp("SQL Exception");
        }
        finally
        {
            if (reportStatement != null)
            {
                reportStatement.close();
            }
        }

        JScrollPane reportTableSP = new JScrollPane(reportTable);
        getContentPane().add(reportTableSP);
        getContentPane().add(closeWindowButton);
        closeWindowButton.setAlignmentX(CENTER_ALIGNMENT);
        closeWindowButton.addActionListener(e ->
        {
            Bank.reportON = !Bank.reportON;
            AccountsReport.this.dispose();
        });

        AccountsReport.this.addWindowListener(new WindowListener()
        {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e)
            {
                Bank.reportON = !Bank.reportON;
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

        setResizable(true);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setLocationRelativeTo(null);
        setVisible(true);
        AccountsReport.this.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
    }
}

