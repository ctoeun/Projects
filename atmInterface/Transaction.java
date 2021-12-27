package atmInterface;

import java.util.Date;

public class Transaction {

	private double amount; // amount of transaction
	private Date timestamp;
	private String memo;
	private Account inAccount;

	/**
	 * 
	 * @param amount
	 * @param inAccount
	 */
	public Transaction(double amount, Account inAccount) {
		this.amount = amount;
		this.inAccount = inAccount;
		this.timestamp = new Date();
		this.memo = "";

	}

	/**
	 * If memo is available when making transaction
	 * 
	 * @param amount
	 * @param memo
	 * @param inAccount
	 */
	public Transaction(double amount, String memo, Account inAccount) {
		// call argument constructor above
		this(amount, inAccount);
		this.memo = memo;
	}

	/**
	 * Get the amount of the transaction
	 * 
	 * @return - the amount
	 */
	public double getAmount() {
		return this.amount;
	}

	/**
	 * Get the summary line of each transaction
	 * 
	 * @return - the summary line as a string
	 */
	public String getSummaryLine() {

		if (this.amount >= 0) {
			return String.format("%s : $%.02f : %s", this.timestamp.toString(), this.amount, this.memo);
		} else {
			return String.format("%s : -$%.02f : %s", this.timestamp.toString(), -this.amount, this.memo);
		}
	}
}
