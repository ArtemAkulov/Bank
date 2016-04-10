import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AccountsReport extends JFrame
{
    JButton closeWindowButton = new JButton("CLOSE");
    DefaultTableModel accountsTableModel = new DefaultTableModel();
    JTable testTable = new JTable(accountsTableModel);

    public AccountsReport() throws SQLException
    {
        super("Report on all accounts");

        Statement reportStatement = null;
        Statement entriesNumberStatement = null;
        final String entriesNumberQuery = "select account_id from bank.accounts order by account_id desc limit 1";
        final String reportQuery = "select account_number, account_holder, account_balance, date_opened, is_closed from bank.accounts";
        String activeAccountString;
        int entriesNumber = 0;

        accountsTableModel.addColumn("Account number");
        accountsTableModel.addColumn("Account holder");
        accountsTableModel.addColumn("Date of opening");
        accountsTableModel.addColumn("Account balance");
        accountsTableModel.addColumn("Active");

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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        finally
        {
            if (reportStatement != null)
            {
                reportStatement.close();
            }
        }

        getContentPane().add(new JScrollPane(testTable));
        testTable.setFillsViewportHeight(true);
        closeWindowButton.setAlignmentX(10);

        getContentPane().add(closeWindowButton);
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

        setResizable(false);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        AccountsReport.this.setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
