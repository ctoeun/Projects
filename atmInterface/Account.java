package atmInterface;

import java.util.ArrayList;

public class Account {

	private String name; // name of the account
	private String UUID; // users unique universal id
	private User holder; // holder of account
	private ArrayList<Transaction> transactions;

	/**
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
	 * 
	 * @return
	 */
	public String getUUID() {
		return this.UUID;
	}

	/**
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
	 * 
	 */
	public void printTransactionHistory() {
		System.out.printf("\nTransaction history for account %s\n", this.UUID);
		for (int t = this.transactions.size() - 1; t >= 0; t--) {
			System.out.println(this.transactions.get(t).getSummaryLine());
		}
		System.out.println();
	}

	/**
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
