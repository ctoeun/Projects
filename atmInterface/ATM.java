package atmInterface;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/*
 * This projects implements an ATM interface to store banking info
 */
public class ATM {

	private static Scanner scan = new Scanner(System.in);
	private static final int MAXATTEMPTS = 3;
	// add new banks
	private static Bank bankOfAmerica = new Bank("Bank of America");
	// add new users
	private static User userJD = bankOfAmerica.addUser("John", "Doe", "1234");
	// add checking account for userJD
	private static Account newAccount = new Account("Checking", userJD, bankOfAmerica);

	/**
	 * Running the ATM interface
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		addAccounts();

		User curUser;

		while (true) {

			// stay in login prompt until successful login
			curUser = ATM.mainMenuPrompt(bankOfAmerica, scan);

			// stay in main menu until user quits
			ATM.printUserMenu(curUser, scan);

		}
	}

	/*
	 * Add all accounts here -- sense of abstraction for main method
	 */
	private static void addAccounts() {
		bankOfAmerica.addAccount(newAccount);
		userJD.addAccount(newAccount);
	}
	
	/**
	 * 
	 * Main menu prompt interface -- asks user for ID and pin
	 * 
	 * @param bank
	 * @param scan
	 * @return
	 */
	public static User mainMenuPrompt(Bank bank, Scanner scan) {

		String userID;
		String pin;
		User authUser;
		int unsuccessfulAttempts = 0;

		// prompt the user for user ID/pin combo until valid
		do {
			System.out.printf("\n\nWelcome to %s\n\n", bank.getBankName());
			System.out.print("Enter user ID: ");
			userID = scan.nextLine();
			System.out.print("Enter pin: ");
			pin = scan.nextLine();

			// try to get the user object corresponding to the ID and pin combo
			authUser = bank.userLogin(userID, pin);
			if (authUser == null) {
				unsuccessfulAttempts++;
				if (unsuccessfulAttempts >= 3) {
					// "account security"
					System.out.println("\nToo many unsuccessful attempts! Locking account!\n");
					System.exit(0);
				}
				System.out.println("Incorrect user ID/pin combination. Please try again.");
				attemptsLeft(unsuccessfulAttempts);
			}

		} while (authUser == null); // continue looping until successful login

		System.out.print("\n\nSuccessful login!");

		return authUser;
	}

	/**
	 * 
	 * Counts the amount of attempts for successful login
	 * 
	 * @param unsuccessfulAttempts
	 */
	private static void attemptsLeft(int unsuccessfulAttempts) {
		System.out.printf("\nAttempts remaining: %d\n", MAXATTEMPTS - unsuccessfulAttempts);
	}

	/**
	 * 
	 * Prints the ATM interface after successful login
	 * 
	 * @param user
	 * @param scan
	 */
	public static void printUserMenu(User user, Scanner scan) {

		// print a summary of the user's accounts
		user.printAccountsSummary();

		// inits
		int choice;

		// user menu
		do {
			System.out.printf("Welcome %s, what would you like to do?\n", user.getFullName());
			System.out.println("  1) Show transaction history");
			System.out.println("  2) Withdraw");
			System.out.println("  3) Deposit");
			System.out.println("  4) Transfer");
			System.out.println("  5) Logout");
			System.out.println("  6) Quit\n");
			System.out.println("Enter choice: ");
			choice = scan.nextInt();

			if (choice < 1 || choice > 6) {
				System.out.println("Invalid choice! Please re-enter number.\n");
			}
		} while (choice < 1 || choice > 6);

		// process choice and complete request
		switch (choice) {
		case 1:
			ATM.showTransactionHistory(user, scan);
			break;

		case 2:
			ATM.withdrawFunds(user, scan);
			break;

		case 3:
			ATM.depositFunds(user, scan);
			break;

		case 4:
			ATM.transferFunds(user, scan);
			break;

		case 5:
			// gobble up rest of previous input
			scan.nextLine();
			break;

		case 6:
			// user wants to exit interface
			exitATM();
		}

		// redisplay this menu unless the user wants to logout
		if (choice != 5) {
			ATM.printUserMenu(user, scan);
		} else {
			// user wants to log out
			System.out.print("\nLogging out...\n");

			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				System.err.println(e);
			}

			System.out.printf("\n%s has been successfully logged out!\n", user.getFullName());
		}
	}

	/*
	 * Exits the ATM and sends message if it interface has successfully quit
	 */
	private static void exitATM() {
		System.out.println("Quitting...\n");
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			System.err.println(e);
			System.exit(0);
		}
		System.out.println("SUCCESSFULLY QUIT");
		System.exit(0);
	}

	/**
	 * 
	 * Prints the transaction history for user accounts
	 * 
	 * @param user
	 * @param scan
	 */
	private static void showTransactionHistory(User user, Scanner scan) {

		int theAcct;

		// get account whose transaction history to look at
		do {
			System.out.printf("Enter the number(1-%d) of the account whose transactions you want to see: ",
					user.numAccounts());
			theAcct = scan.nextInt() - 1;
			if (theAcct < 0 || theAcct >= user.numAccounts()) {
				System.out.println("Invalid Account. Please try again!");
			}
		} while (theAcct < 0 || theAcct >= user.numAccounts());

		// print transaction history
		user.printAccountTransactionHistory(theAcct);
	}

	/**
	 * 
	 * Prints the funds the user wants the withdraw from account
	 * 
	 * @param user
	 * @param scan
	 */
	public static void withdrawFunds(User user, Scanner scan) {

		// inits
		int fromAcct;
		double amount, acctBal;
		String memo;

		// get account to withdraw from

		do {
			System.out.printf("Enter the number(1-%d) of the account to withdraw from: ", user.numAccounts());
			fromAcct = scan.nextInt() - 1;
			if (fromAcct < 0 || fromAcct >= user.numAccounts()) {
				System.out.println("Invalid Account. Please try again!");
			}
		} while (fromAcct < 0 || fromAcct >= user.numAccounts());

		acctBal = user.getAccountBalance(fromAcct);

		// get amount to withdraw
		do {
			System.out.printf("Enter the amount to withdraw (max $%.02f): $", acctBal);
			amount = scan.nextDouble();
			if (amount <= 0) {
				System.out.println("Amount must be greater than zero!");
			} else if (amount > acctBal) {
				System.out.printf("Amount must not be greater than balance of $%.02f\n", acctBal);
			}
		} while (amount <= 0 || amount > acctBal);

		// gobble up rest of previous input
		scan.nextLine();

		// get memo
		System.out.print("Enter a memo: ");
		memo = scan.nextLine();

		// do withdraw
		user.addAccountTransaction(fromAcct, -1 * amount, memo);

	}

	/**
	 * 
	 * Prints the funds the user wants to deposit into account
	 * 
	 * @param user
	 * @param scan
	 */
	private static void depositFunds(User user, Scanner scan) {

		// inits
		int toAcct;
		double amount, acctBal;
		String memo;

		// get account to deposit to

		do {
			System.out.printf("Enter the number (1-%d) of the account to deposit in: ", user.numAccounts());
			toAcct = scan.nextInt() - 1;
			if (toAcct < 0 || toAcct >= user.numAccounts()) {
				System.out.println("Invalid Account. Please try again!");
			}
		} while (toAcct < 0 || toAcct >= user.numAccounts());

		acctBal = user.getAccountBalance(toAcct);

		// get amount to deposit
		do {
			System.out.printf("Enter the amount to deposit (max: $%.02f): $", acctBal);
			amount = scan.nextDouble();
			if (amount <= 0) {
				System.out.println("Amount must be greater than zero! ");
			}
		} while (amount <= 0);

		// gobble up rest of previous input
		scan.nextLine();

		// get memo
		System.out.print("Enter a memo: ");
		memo = scan.nextLine();

		// do deposit
		user.addAccountTransaction(toAcct, amount, memo);

	}

	/**
	 * 
	 * User wants to transfer funds to different account within ATM
	 * 
	 * @param user
	 * @param scan
	 */
	private static void transferFunds(User user, Scanner scan) {

		// inits
		int fromAcct, toAcct;
		double amount, acctBal;

		// get account to transfer from

		do {
			System.out.printf("Enter the number (1-%d) of the account to transfer from: ", user.numAccounts());
			fromAcct = scan.nextInt() - 1;
			if (fromAcct < 0 || fromAcct >= user.numAccounts()) {
				System.out.println("Invalid Account. Please try again!");
			}
		} while (fromAcct < 0 || fromAcct >= user.numAccounts());

		acctBal = user.getAccountBalance(fromAcct);

		// get the account to transfer to
		do {
			System.out.printf("Enter the number (1-%d) of the account to transfer to: ", user.numAccounts());
			toAcct = scan.nextInt() - 1;
			if (toAcct < 0 || toAcct >= user.numAccounts()) {
				System.out.println("Invalid Account. Please try again!");
			}
		} while (toAcct < 0 || toAcct >= user.numAccounts());

		// get amount to transfer
		do {
			System.out.printf("Enter the amount to transfer (max: $%.02f): $", acctBal);
			amount = scan.nextDouble();
			if (amount <= 0) {
				System.out.println("Amount must be greater than zero! ");
			} else if (amount > acctBal) {
				System.out.printf("Amount must not be greater than balance of $%.02f\n", acctBal);
			}
		} while (amount <= 0 || amount > acctBal);

		// finally do transfer
		user.addAccountTransaction(fromAcct, -1 * amount,
				String.format("Transfer to account %s : %s,", user.getAccountUUID(toAcct), user.getAccountName(toAcct)));
		user.addAccountTransaction(toAcct, amount,
				String.format("Transfer to account %s : %s,", user.getAccountUUID(fromAcct), user.getAccountName(fromAcct)));

	}

}
