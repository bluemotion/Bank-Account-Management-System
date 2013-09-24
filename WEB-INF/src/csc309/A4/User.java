package csc309.A4;

public class User {
	int id;
	String login;
	String salt;
	String first;
	String last;
	boolean admin;
	
	public User(int id, String login, String salt, String first, String last,
			boolean admin) {
		super();
		this.id = id;
		this.login = login;
		this.salt = salt;
		this.first = first;
		this.last = last;
		this.admin = admin;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getFirst() {
		return first;
	}
	public void setFirst(String first) {
		this.first = first;
	}
	public String getLast() {
		return last;
	}
	public void setLast(String last) {
		this.last = last;
	}
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	
	
	
}
