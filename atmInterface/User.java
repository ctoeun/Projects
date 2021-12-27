package atmInterface;

import java.util.ArrayList;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {

	private String firstName, lastName, UUID;
	private byte[] pinHash;
	private ArrayList<Account> accounts;

	/**
	 * Constructor that creates a new user
	 * 
	 * @param firstName
	 * @param lastName
	 * @param pin
	 * @param bank
	 */
	public User(String firstName, String lastName, String pin, Bank bank) {
		// set the users name
		this.firstName = firstName;
		this.lastName = lastName;

		// store the pin's MD5 hash for security
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			this.pinHash = md.digest(pin.getBytes());
		} catch (NoSuchAlgorithmException e) {
			System.err.println("error, caught NoSuchAlgorithmException");
			e.printStackTrace();
			System.exit(1);
		}

		// get a new, unique universal ID for user
		this.UUID = bank.getNewUserUUID();

		// create empty list of accounts
		this.accounts = new ArrayList<Account>();

		// print log message
		System.out.printf("New user %s, %s with ID %s created.\n", this.lastName, this.firstName, this.UUID);

	}

	/**
	 * 
	 * @return
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * 
	 * @return
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * 
	 * @return
	 */
	public String getFullName() {
		return this.firstName + " " + this.lastName;
	}

	/**
	 * 
	 * @param newAccount
	 */
	public void addAccount(Account newAccount) {
		this.accounts.add(newAccount);
	}

	/**
	 * 
	 * @return
	 */
	public String getUUID() {
		return this.UUID;
	}

	/**
	 * 
	 * @param pin
	 * @return
	 */
	public boolean validPin(String pin) {

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return MessageDigest.isEqual(md.digest(pin.getBytes()), pinHash);
		} catch (NoSuchAlgorithmException e) {
			System.err.println("error, caught NoSuchAlgorithmException");
			e.printStackTrace();
			System.exit(1);
		}
		return false;
	}

	/**
	 * 
	 */
	public void printAccountsSummary() {

		System.out.printf("\n\n%s's account summaries\n", this.firstName);
		for (int i = 0; i < this.accounts.size(); i++) {
			System.out.printf("  %d) %s\n", i + 1, this.accounts.get(i).getSummaryLine());
		}
		System.out.println();

	}

	/**
	 * 
	 * @return
	 */
	public int numAccounts() {
		return this.accounts.size();
	}

	/**
	 * 
	 * @param acctIndex
	 */
	public void printAccountTransactionHistory(int acctIndex) {
		this.accounts.get(acctIndex).printTransactionHistory();
	}

	/**
	 * 
	 * @param acctIndex
	 * @return
	 */
	public double getAccountBalance(int acctIndex) {
		return this.accounts.get(acctIndex).getBalance();
	}

	/**
	 * 
	 * @param acctIndex
	 * @return
	 */
	public String getAccountUUID(int acctIndex) {
		return this.accounts.get(acctIndex).getUUID();
	}

	/**
	 * 
	 * @param acctIndex
	 * @return
	 */
	public String getAccountName(int acctIndex) {
		return this.accounts.get(acctIndex).getAccountName();
	}

	/**
	 * 
	 * @param acctIndex
	 * @param amount
	 * @param memo
	 */
	public void addAccountTransaction(int acctIndex, double amount, String memo) {
		this.accounts.get(acctIndex).addTransaction(amount, memo);
	}

}
