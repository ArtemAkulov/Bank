import java.sql.*;

public class AccountsDatabase
{
    static final String driverJDBC = "com.mysql.jdbc.Driver";
    static final String addressDB = "jdbc:mysql://localhost:3306?autoReconnect=true&useSSL=false";
    static final String userName = "root";
    static final String passWord = "dahlia";
    static Connection accountsDBConnection = null;

    public AccountsDatabase() throws SQLException
    {
        Connect2DB();
        if (!CheckIfDBExists())
        {
            CreateDB();
            PopulateDB();
        }
    }

    private boolean CheckIfDBExists() throws SQLException
    {
        Statement existenceStatement = null;
        String existenceQuery = "select count(*) as db_exists from information_schema.tables where table_schema = 'bank' and table_name = 'accounts'";

        try
        {
            existenceStatement = AccountsDatabase.accountsDBConnection.createStatement();
            ResultSet existenceResultSet = existenceStatement.executeQuery(existenceQuery);
            existenceResultSet.next();
            boolean dbExists = existenceResultSet.getInt("db_exists") > 0;
            return dbExists;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (existenceStatement != null)
            {
                existenceStatement.close();
            }
        }
        return false;
    }

    private void CreateDB() throws SQLException
    {
        Statement createDBStatement = null;
        String createDBQuery = "create database bank";

        try
        {
            createDBStatement = AccountsDatabase.accountsDBConnection.createStatement();
            createDBStatement.executeUpdate(createDBQuery);
        }
        catch (SQLException e)
        {

        }
        finally
        {
            if (createDBStatement != null)
            {
                createDBStatement.close();
            }
        }
    }

    private void PopulateDB() throws SQLException
    {
        Statement createTableStatement = null;
        String createTableQuery = "create table bank.accounts (account_id integer, account_number varchar(20), account_holder varchar(50), account_balance float(11,2), date_opened date, is_closed boolean)";

        try
        {
            createTableStatement = AccountsDatabase.accountsDBConnection.createStatement();
            createTableStatement.executeUpdate(createTableQuery);
        }
        catch (SQLException e)
        {

        }
        finally
        {
            if (createTableStatement != null)
            {
                createTableStatement.close();
            }
        }
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
        final String prepareIDQuery = "select account_id from bank.accounts order by account_id desc limit 1";
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
            openQuery = "insert into bank.accounts values (" + nextID + ", \"" + accountNumber + "\", \"" + accountHolder + "\", \"" + initialBalance + "\", Curdate(), 0)";
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
        String updateQuery = "update bank.accounts set is_closed = 1 where account_id = " + String.valueOf(accountID);

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
        String updateQuery = "update bank.accounts set account_balance = " + updatedBalance + " where account_id = " + String.valueOf(accountID);

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