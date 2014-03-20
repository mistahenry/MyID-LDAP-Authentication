package model;
/**
 * model class that defines a User
 * @author Will Henry
 *
 */
public class User {

	/**
	 * User's first, middle, and last name
	 */
	private String fullName;
	/**
	 * user's 810 number
	 */
	private String identifier;
	/**
	 * user's major
	 */
	private String major;
	
	/**
	 * Constructor for a user
	 * @param fullName
	 * @param identifier
	 * @param major
	 */
	public User(String fullName, String identifier, String major) {
		super();
		this.fullName = fullName;
		this.identifier = identifier;
		this.major = major;
	}
	/**
	 * getter for the User's fullname
	 * @return fullName of the user
	 */
	public String getFullName() {
		return fullName;
	}
	/**
	 * setter for the User's full name
	 * @param fullName - the full name to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	/**
	 * getter for the 810 number of the user
	 * @return identifier - the user's 810 number
	 */
	public String getIdentifier() {
		return identifier;
	}
	/**
	 * Setter for the User's 810 number
	 * @param identifier - the 810 number to set
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	/**
	 * getter for the User's major
	 * @return major - the User's major
	 */
	public String getMajor() {
		return major;
	}
	/**
	 * setter for the major of the user
	 * @param major - the major to set
	 */
	public void setMajor(String major) {
		this.major = major;
	}
}
