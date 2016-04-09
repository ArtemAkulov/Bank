import java.sql.*;

public class AccountsDatabase
{

    static final String driverJDBC = "com.mysql.jdbc.Driver";
    static final String addressDB = "jdbc:mysql://localhost/bank?autoReconnect=true&useSSL=false";
    static final String userName = "root";
    static final String passWord = "dahlia";
    static Connection accountsDBConnection = null;

    public AccountsDatabase() throws SQLException
    {
        if (!CheckIfDBExists()) CreateAndPopulateDB();
        Connect2DB();
    }

    private boolean CheckIfDBExists()
    {
        return true;
    }

    private void CreateAndPopulateDB()
    {

    }

    private void Connect2DB() throws SQLException
    {
        try
        {
            Class.forName(driverJDBC);
            accountsDBConnection = DriverManager.getConnection(addressDB, userName, passWord);
        }
        catch(SQLException se)
        {
            se.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void OpenAccount(String accountNumber, String accountHolder, String initialBalance) throws SQLException
    {
        Statement openStatement = null;
        Statement prepareIDStatement = null;
        String prepareIDQuery = "select account_id from accounts order by account_id desc limit 1";
        String openQuery;
        String nextID = null;

        try
        {
            prepareIDStatement = AccountsDatabase.accountsDBConnection.createStatement();
            ResultSet prepareIDResult = prepareIDStatement.executeQuery(prepareIDQuery);
            if (!prepareIDResult.next())
            {
                nextID = "1";
            }
            else
            {
                nextID = String.valueOf(prepareIDResult.getInt("account_id") + 1);
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (prepareIDStatement != null)
            {
                prepareIDStatement.close();
            }
        }
        try
        {
            openQuery = "insert into accounts values (" + nextID + ", \"" + accountNumber + "\", \"" + accountHolder + "\", \"" + initialBalance + "\", Curdate(), null, 0)";
            openStatement = AccountsDatabase.accountsDBConnection.createStatement();
            openStatement.executeUpdate(openQuery);

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (openStatement != null)
            {
                openStatement.close();
            }
        }
    }

    public void CloseAccount(int accountID) throws SQLException
    {
        Statement updateStatement = null;
        String updateQuery = "update accounts set is_closed = 1 where account_id = " + String.valueOf(accountID);

        try
        {
            updateStatement = AccountsDatabase.accountsDBConnection.createStatement();
            updateStatement.executeUpdate(updateQuery);

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (updateStatement != null)
            {
                updateStatement.close();
            }
        }
    }

    public void updateAccountBalance(int accountID, String updatedBalance) throws SQLException
    {
        Statement updateStatement = null;
        String updateQuery = "update accounts set account_balance = " + updatedBalance + " where account_id = " + String.valueOf(accountID);

        try
        {
            updateStatement = AccountsDatabase.accountsDBConnection.createStatement();
            updateStatement.executeUpdate(updateQuery);

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (updateStatement != null)
            {
                updateStatement.close();
            }
        }
    }

    public void CleanUp() throws SQLException
    {
        try
        {
            if (accountsDBConnection!=null) accountsDBConnection.close();
        }
        catch(SQLException se)
        {
            se.printStackTrace();
        }
    }
}