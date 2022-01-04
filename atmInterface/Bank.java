package atmInterface;

import java.util.ArrayList;
import java.util.Random;

/*
 * This class creates a new bank for ATM
 */
public class Bank {

	// inits
	private String name;
	private ArrayList<User> user;
	private ArrayList<Account> accounts;
	private Random r = new Random();

	/**
	 * Constructor for creating new bank
	 * 
	 * @param name
	 */
	public Bank(String name) {
		this.name = name;
		this.user = new ArrayList<User>();
		this.accounts = new ArrayList<Account>();
	}

	/**
	 * Returns the bank name
	 * 
	 * @return
	 */
	public String getBankName() {
		return this.name;
	}

	/**
	 * Returns the users unique ID
	 * 
	 * @return
	 */
	public String getNewUserUUID() {
		String uuid;
		int length = 6;
		boolean nonUnique;

		// keep creating id until unique
		do {
			uuid = "";
			for (int i = 0; i < length; i++) {
				uuid += ((Integer) r.nextInt(10)).toString();
			}
			nonUnique = false;
			for (User u : this.user) {
				if (uuid.compareTo(u.getUUID()) == 0) {
					nonUnique = true;
					break;
				}
			}

		} while (nonUnique);

		return uuid;
	}

	/**
	 * Gets the users unique account ID
	 * 
	 * @return
	 */
	public String getNewAccountUUID() {
		String uuid;
		int length = 10;
		boolean nonUnique;

		// keep creating id until unique
		do {
			uuid = "";
			for (int i = 0; i < length; i++) {
				uuid += ((Integer) r.nextInt(10)).toString();
			}
			nonUnique = false;
			for (Account a : this.accounts) {
				if (uuid.compareTo(a.getUUID()) == 0) {
					nonUnique = true;
					break;
				}
			}

		} while (nonUnique);

		return uuid;
	}

	/**
	 * Adds a new user account to bank
	 * 
	 * @param newAccount
	 */
	public void addAccount(Account newAccount) {
		this.accounts.add(newAccount);
	}

	/**
	 * Adds a new user to specified account
	 * 
	 * @param firstName
	 * @param lastName
	 * @param pin
	 * @return
	 */
	public User addUser(String firstName, String lastName, String pin) {
		// create a new User object and add it to user list
		User newUser = new User(firstName, lastName, pin, this);
		this.user.add(newUser);

		// create a savings account
		Account newAccount = new Account("Savings", newUser, this);

		// add holder and bank lists
		newUser.addAccount(newAccount);
		this.addAccount(newAccount);

		return newUser;

	}

	/**
	 * Checks if users ID and pin is valid to login
	 * 
	 * @param userID
	 * @param pin
	 * @return
	 */
	public User userLogin(String userID, String pin) {

		// search list of users
		for (User u : this.user) {
			// check user ID is correct
			if (u.getUUID().compareTo(userID) == 0 && u.validPin(pin)) {
				return u;
			}
		}

		// haven't found user or pin is invalid
		return null;

	}

}
