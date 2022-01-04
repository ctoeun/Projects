package atmInterface;

import java.util.ArrayList;

/*
 * This class creates a new banking account for defined users
 * 
 * "Checking" or "Savings"
 * 
 */
public class Account {

	private String name; // name of the account
	private String UUID; // users unique universal id
	private User holder; // holder of account
	private ArrayList<Transaction> transactions;

	/**
	 * 
	 * Constructor for creating new account
	 * 
	 * @param name
	 * @param holder
	 * @param bank
	 */
	public Account(String name, User holder, Bank bank) {
		this.name = name;
		this.holder = holder;

		// get a new, unique universal ID for user
		this.UUID = bank.getNewAccountUUID();

		// new transactions list
		this.transactions = new ArrayList<Transaction>();
	}

	/**
	 * Returns the acconut name
	 * 
	 * @return
	 */
	public String getAccountName() {
		return this.name;
	}
	
	/**
	 * Returns the users unique ID
	 * 
	 * @return
	 */
	public String getUUID() {
		return this.UUID;
	}

	/**
	 * Returns the users summary line when requested
	 * 
	 * @return
	 */
	public String getSummaryLine() {

		// get the accounts balance
		double balance = this.getBalance();

		// format the summary line, depending on whether the balance is negative
		if (balance >= 0) {
			return String.format("%s : $%.02f : %s", this.UUID, balance, this.name);
		} else {
			return String.format("%s : $(%.02f) : %s", this.UUID, balance, this.name);
		}
	}

	/**
	 * Gets the users current balance on account
	 * 
	 * @return
	 */
	public double getBalance() {
		double balance = 0.0;

		// get balance within transaction
		for (Transaction t : this.transactions) {
			balance += t.getAmount();
		}

		return balance;

	}

	/**
	 * Prints the users complete transaction history after deposits, withdraws, and transfers
	 */
	public void printTransactionHistory() {
		System.out.printf("\nTransaction history for account %s : %s\n", this.UUID, this.name);
		for (int t = this.transactions.size() - 1; t >= 0; t--) {
			System.out.println(this.transactions.get(t).getSummaryLine());
		}
		System.out.println();
	}

	/**
	 * Adds a new transaction with optional memo per user
	 * 
	 * @param amount
	 * @param memo
	 */
	public void addTransaction(double amount, String memo) {

		// create new transaction object and add it to list
		Transaction newTrans = new Transaction(amount, memo, this);
		this.transactions.add(newTrans);
	}

}
