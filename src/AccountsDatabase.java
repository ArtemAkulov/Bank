import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

/**
 *
 * AccountsDatabase class holds access to the database of the bank account information. This is accessed when:
 *  <ul>
 * <li>Opening Account(s)
 * <li>Updating Account(s)
 * <li>Listing Account(s)
 * <li>Closing Account(s)
 * </ul>
 *
 */
class AccountsDatabase
{
    /**
     * Represents the connection to the mysql database called "bank".
     *
     */
    static Connection accountsDBConnection = null;

    /**
     *
     * AccountsDatabase method will check if an existing database already exists.
     * If the a database is not found, a new database will be created and populated.
     *
     * @throws SQLException An exception that provides information on a database access error or other errors.
     *
     */
    AccountsDatabase() throws SQLException
    {
        Connect2DB();
        if (!CheckIfDBExists())
        {
            CreateDB();
            PopulateDB();
        }
    }

    /**
     *
     * @return dbExists will display 'true' or 'false' value if the Database Exists.
     * If there's already a database, this method will simply be closed.
     * @throws SQLException If there's a database access error, an SQLException will be thrown
     */
    private boolean CheckIfDBExists() throws SQLException
    {
        Statement existenceStatement = null;
        String existenceQuery = "select count(*) as db_exists from information_schema.tables where table_schema = 'bank' and table_name = 'accounts'";

        try
        {
            existenceStatement = AccountsDatabase.accountsDBConnection.createStatement();
            ResultSet existenceResultSet = existenceStatement.executeQuery(existenceQuery);
            existenceResultSet.next();
            return existenceResultSet.getInt("db_exists") > 0;
        }
        catch (SQLException e)
        {
            ExceptionHandler oops = new ExceptionHandler();
            oops.ExceptionHandlerStartUp("SQL Exception");
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

    /**
     * A new database will be created when there's no existing database.
     * If there's already a database, this method will simply be closed.
     * @throws SQLException If there's a database access error, an SQLException will be thrown
     */
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
            ExceptionHandler oops = new ExceptionHandler();
            oops.ExceptionHandlerStartUp("SQL Exception");
        }
        finally
        {
            if (createDBStatement != null)
            {
                createDBStatement.close();
            }
        }
    }

    /**
     * Will populate the table in the database created.
     * @throws SQLException If there's a database access error, an SQLException will be thrown
     */
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
            ExceptionHandler oops = new ExceptionHandler();
            oops.ExceptionHandlerStartUp("SQL Exception");
        }
        finally
        {
            if (createTableStatement != null)
            {
                createTableStatement.close();
            }
        }
    }

    /**
     * Details the driver needed for the SQL connection. Also includes the credentials used to access SQL server.
     * @throws SQLException If the driver is not found and the credentials declared are incorrect, an SQLException will be thrown.
     */
    private void Connect2DB() throws SQLException
    {
        String driverJDBC;
        String addressDB;
        String userName;
        String passWord;

        try
        {
            Properties iniFile = new Properties();
            iniFile.load(new FileInputStream("resources/bank.init"));
            driverJDBC = iniFile.getProperty("driver");
            addressDB = iniFile.getProperty("address");
            userName = iniFile.getProperty("username");
            passWord = iniFile.getProperty("password");
        }
        catch (Exception e)
        {
            driverJDBC = "com.mysql.jdbc.Driver";
            addressDB = "jdbc:mysql://localhost:3306?autoReconnect=true&useSSL=false";
            userName = "root";
            passWord = "test1234";
            ExceptionHandler oops = new ExceptionHandler();
            oops.ExceptionHandlerStartUp("Could not read INI file");
        }

        try
        {
            Class.forName(driverJDBC);
            accountsDBConnection = DriverManager.getConnection(addressDB, userName, passWord);
        }
        catch(SQLException se)
        {
            ExceptionHandler oops = new ExceptionHandler();
            oops.ExceptionHandlerStartUp("SQL Exception");
        }
        catch(Exception e)
        {
            ExceptionHandler oops = new ExceptionHandler();
            oops.ExceptionHandlerStartUp("General exception");
        }
    }

    /**
     * Details the String used to open account.
     ** @param accountNumber numeric identifier for ownership of an account     *
     * @param accountHolder an individual who is legally responsible for all transactions made on the account
     * @param initialBalance opening amount deposited by the accountHolder
     * @throws SQLException If there's a database access error, an SQLException will be thrown
     */
    void OpenAccount(String accountNumber, String accountHolder, String initialBalance) throws SQLException
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
            ExceptionHandler oops = new ExceptionHandler();
            oops.ExceptionHandlerStartUp("SQL Exception");
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
            ExceptionHandler oops = new ExceptionHandler();
            oops.ExceptionHandlerStartUp("SQL Exception");
        }
        finally
        {
            if (openStatement != null)
            {
                openStatement.close();
            }
        }
    }

    /**
     * Details the String used to close an account
     * @param accountID a unique identifier of each accountNumber
     * @throws SQLException If there's a database access error, an SQLException will be thrown
     */
    void CloseAccount(int accountID) throws SQLException
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
            ExceptionHandler oops = new ExceptionHandler();
            oops.ExceptionHandlerStartUp("SQL Exception");
        }
        finally
        {
            if (updateStatement != null)
            {
                updateStatement.close();
            }
        }
    }

    /**
     * Details string when updating Account Balance
     * @param accountID a unique identifier of each accountNumber
     * @param updatedBalance updated balance of the accountHolder
     * @throws SQLException If there's a database access error, an SQLException will be thrown
     */
    void updateAccountBalance(int accountID, String updatedBalance) throws SQLException
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
            ExceptionHandler oops = new ExceptionHandler();
            oops.ExceptionHandlerStartUp("SQL Exception");
        }
        finally
        {
            if (updateStatement != null)
            {
                updateStatement.close();
            }
        }
    }

    /**
     * Method called prior to quitting the program to close the connection to DB.
     * @throws SQLException If there's a database access error, an SQLException will be thrown
     */
    void CleanUp() throws SQLException
    {
        try
        {
            if (accountsDBConnection!=null) accountsDBConnection.close();
        }
        catch(SQLException se)
        {
            ExceptionHandler oops = new ExceptionHandler();
            oops.ExceptionHandlerStartUp("SQL Exception");
        }
    }
}