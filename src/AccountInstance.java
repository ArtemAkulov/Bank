/**
 * The AccountInstance class is used to store the data from the table. It's not used very extensively and doesn't have its own methods.
 * Its singular current implementation is to populate the ComboBox in the main frame and transmit data to another classes.
 * As we don't need all the data in the table for that, we don't store opening date and account status in this class.
 */
class AccountInstance
{
    int accountID;
    String accountNumber;
    String accountHolder;
    String accountBalance;
}
