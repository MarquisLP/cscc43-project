
package Controller;

import database.HostRepository;
import database.UserRepository;
import database.models.Address;
import database.models.Host;
import database.models.Renter;
import database.RenterRepository;
import database.models.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

/*
 * Code copied and modified from CommandLine.java provided on the CSCC43 course website.
 */
public class CommandLine {

    // 'sqlMngr' is the object which interacts directly with MySQL
	private SQLController sqlMngr = null;
    // 'sc' is needed in order to scan the inputs provided by the user
	private Scanner sc = null;
	// 'user' holds account information for the currently-logged in user, which will be used when e.g. posting a listing.
	private User user = null;
	
	//Public functions - CommandLine State Functions
	
    /* Function used for initializing an istance of current
     * class
     */
	public boolean startSession() {
		boolean success = true;
		if (sc == null) {
			sc = new Scanner(System.in);
		}
		if (sqlMngr == null) {
			sqlMngr = new SQLController();
		}
		try {
			success = sqlMngr.connect(this.getCredentials());
		} catch (ClassNotFoundException e) {
			success = false;
			System.err.println("Establishing connection triggered an exception!");
			e.printStackTrace();
			sc = null;
			sqlMngr = null;
		}
		return success;
	}
	
    /* Function that acts as destructor of an instance of this class.
     * Performs some housekeeping setting instance's private field
     * to null
     */
	public void endSession() {
		if (sqlMngr != null)
			sqlMngr.disconnect();
		if (sc != null) {
			sc.close();
		}
		sqlMngr = null;
		sc = null;
	}

    /* Function that executes an infinite loop and activates the respective 
     * functionality according to user's choice. At each time it also outputs
     * the menu of core functionalities supported from our application.
     */
	public boolean execute() {
		if (sc != null && sqlMngr != null) {
			System.out.println("");
			System.out.println("***************************");
			System.out.println("******ACCESS GRANTED*******");
			System.out.println("***************************");
			System.out.println("");

			System.out.println("Welcome to MyBNB!");
			System.out.println("In order to use the system, you must first log into an account.");

			String input = "";
			int choice = -1;

			boolean loginComplete = false;
			while (!(loginComplete)) {
				loginMenu();
				input = sc.nextLine();
				try {
					choice = Integer.parseInt(input);
				}
				catch (NumberFormatException exception) {
					System.out.println("Invalid option\n");
					continue;
				}

				switch (choice) {
					case 0:
					    System.out.println("Goodbye!");
						System.exit(0);
						break;
					case 1:
                        loginComplete = login();
						break;
					case 2:
						loginComplete = signup();
						break;
					default:
						System.out.println("Invalid option\n");
						break;
				}
			}

			if (user instanceof Host) {
			    return hostMenu();
			}
			else {
			    return renterMenu();
			}
		} else {
			System.out.println("");
			System.out.println("Connection could not been established! Bye!");
			System.out.println("");
			return false;
		}
	}
	
	//Private functions

	private static void loginMenu() {
		System.out.println("=========LOGIN MENU=========");
		System.out.println("0. Exit");
		System.out.println("1. Login");
		System.out.println("2. Signup");
		System.out.print("Enter option number [0-2]: ");
	}
	
	//Print menu options
	private static void menu() {
		System.out.println("=========MENU=========");
		System.out.println("0. Exit.");
		System.out.println("1. Insert a record.");
		System.out.println("2. Select a record.");
		System.out.println("3. Print schema.");
		System.out.println("4. Print table schema.");
		System.out.print("Choose one of the previous options [0-4]: ");
	}
	
    // Called during the initialization of an instance of the current class
    // in order to retrieve from the user the credentials with which our program
    // is going to establish a connection with MySQL
	private String[] getCredentials() {
		String[] cred = new String[2];
		System.out.print("Username: ");
		cred[0] = sc.nextLine();
		System.out.print("Password: ");
		cred[1] = sc.nextLine();
		return cred;
	}

	private boolean login() {
	    System.out.print("Enter SIN (or -1 to return to menu): ");
		String sin = sc.nextLine();
		if (sin.equals("-1")) {
			return false;
		}
		while (user == null) {
			try {
				user = UserRepository.getUser(sin);
			}
			catch (NoSuchElementException exception) {
				System.out.println("The given SIN is not registered in the system. Please try again.");
				System.out.print("Enter SIN: ");
				sin = sc.nextLine();
			}
		}

		System.out.println("Login successful!");
		return true;
	}

	public boolean signup() {
	    boolean sinExists = true;
	    String sin;

	    do {
			System.out.print("Enter SIN: ");
			sin = sc.nextLine();
			try {
			    UserRepository.getUser(sin);
			    System.out.println("That SIN already exists in the system.");
			}
			catch (NoSuchElementException exception) {
				sinExists = false;
			}
		} while (sinExists);

	    String name = "";
	    while (name.length() == 0) {
			System.out.print("Enter name: ");
			name = sc.nextLine();
			if (name.length() == 0) {
			    System.out.println("Name cannot be empty");
			}
		}

		String occupation;
        System.out.print("Enter occupation (optional): ");
        occupation = sc.nextLine();

		String dateOfBirth = "";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		boolean dateOfBirthChosen = false;
	    while (!(dateOfBirthChosen)) {
			System.out.print("Enter date of birth in YYYY-MM-DD format (optional): ");
			dateOfBirth = sc.nextLine();
			if (dateOfBirth.length() == 0) {
			    dateOfBirthChosen = true;
			}
			try {
			    dateFormat.parse(dateOfBirth);
			    dateOfBirthChosen = true;
			} catch (ParseException exception) {
				System.out.println("Invalid date format. Please try again.");
			}
		}

		String postalCode = "";
	    String city = "";
		String country = "";
		while (postalCode.length() == 0) {
			System.out.print("Enter your address's postal code: ");
			postalCode = sc.nextLine();
			if (postalCode.length() == 0) {
				System.out.println("Postal code cannot be empty");
			}
		}
		while (city.length() == 0) {
			System.out.print("Enter your address's city: ");
			city = sc.nextLine();
			if (city.length() == 0) {
				System.out.println("City cannot be empty");
			}
		}
		while (country.length() == 0) {
			System.out.print("Enter your address's country: ");
			country = sc.nextLine();
			if (country.length() == 0) {
				System.out.println("Country cannot be empty");
			}
		}
		Address address = new Address(postalCode.toLowerCase(), city.toLowerCase(), country.toLowerCase());

		boolean isHost;
	    String accountChoiceString;
	    System.out.println("What account type are you making?");
		System.out.print("Enter 'h' for Host, anything else for Renter: ");
		accountChoiceString = sc.nextLine();
		isHost = accountChoiceString.toLowerCase().equals("h");

		if (isHost) {
			Host newHost = new Host(sin, name, dateOfBirth, occupation, address);
			HostRepository.createHost(newHost);
			user = newHost;
		}
		else {
			String creditCardNumber;
            do {
				System.out.print("Enter your credit card number (digits only): ");
				creditCardNumber = sc.nextLine();
				if (!(creditCardNumber.matches("\\d+"))) {
				    System.out.println("Invalid credit card number.");
				}
			} while (!(creditCardNumber).matches("\\d+"));

            Renter newRenter = new Renter(sin, name, dateOfBirth, occupation, address, creditCardNumber);
            RenterRepository.createRenter(newRenter);
            user = newRenter;
		}

		System.out.println("Signup successful!");
		return true;
	}

	private boolean hostMenu() {
		//TODO: Implement
        return true;
	}

	private boolean renterMenu() {
		//TODO: Implement
		return true;
	}

    // Function that handles the feature: "3. Print schema."
	private void printSchema() {
		ArrayList<String> schema = sqlMngr.getSchema();
		
		System.out.println("");
		System.out.println("------------");
		System.out.println("Total number of tables: " + schema.size());
		for (int i = 0; i < schema.size(); i++) {
			System.out.println("Table: " + schema.get(i));
		}
		System.out.println("------------");
		System.out.println("");
	}
	
    // Function that handles the feature: "4. Print table schema."
	private void printColSchema() {
		System.out.print("Table Name: ");
		String tableName = sc.nextLine();
		ArrayList<String> result = sqlMngr.colSchema(tableName);
		System.out.println("");
		System.out.println("------------");
		System.out.println("Total number of fields: " + result.size()/2);
		for (int i = 0; i < result.size(); i+=2) {
			System.out.println("-");
			System.out.println("Field Name: " + result.get(i));
			System.out.println("Field Type: " + result.get(i+1));
		}
		System.out.println("------------");
		System.out.println("");
	}
	
    // Function that handles the feature: "2. Select a record."
	private void selectOperator() {
		String query = "";
		System.out.print("Issue the Select Query: ");
		query = sc.nextLine();
		query.trim();
		if (query.substring(0, 6).compareToIgnoreCase("select") == 0)
			sqlMngr.selectOp(query);
		else
			System.err.println("No select statement provided!");
	}

    // Function that handles the feature: "1. Insert a record."
	private void insertOperator() {
		int rowsAff = 0;
		int counter = 0;
		String query = "";
		System.out.print("Table: ");
		String table = sc.nextLine();
		System.out.print("Comma Separated Columns: ");
		String cols = sc.nextLine();
		System.out.print("Comma Separated Values: ");
		String[] vals = sc.nextLine().split(",");
        //transform the user input into a valid SQL insert statement
		query = "INSERT INTO " + table + " (" + cols + ") VALUES("; 
		for (counter = 0; counter < vals.length - 1; counter++) {
			query = query.concat("'" + vals[counter] + "',");
		}
		query = query.concat("'" + vals[counter] + "');");
		System.out.println(query);
		rowsAff = sqlMngr.insertOp(query);
		System.out.println("");
		System.out.println("Rows affected: " + rowsAff);
		System.out.println("");
	}

}
