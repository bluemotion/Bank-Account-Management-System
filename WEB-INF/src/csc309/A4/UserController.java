package csc309.A4;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Map;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.ServletContext;

import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.util.ServletContextAware;
import org.apache.tomcat.dbcp.dbcp.datasources.SharedPoolDataSource;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

public class UserController extends ActionSupport implements ServletContextAware, SessionAware {
	
	private String login="";
	private String password="";
	private String first="";
	private String last="";
	private Double depoAmount;
	private Double paymentAmount;
	private String payeeName;
	
	private Map<String, User> session;
	private ServletContext context;
	private Vector<User>users = new Vector<User>();
	private Vector<Payee>payees = new Vector<Payee>();
	private Vector<Payment>payments = new Vector<Payment>();
	private Vector<Deposit>deposits = new Vector<Deposit>();
	private Double balance = 0.0;
	
	
	public String list() {
		
		Connection con = null;
	    ResultSet rs = null;
	    
	    SharedPoolDataSource dbcp = (SharedPoolDataSource) context.getAttribute("dbpool");
	    
		try {
			con = dbcp.getConnection();
		    PreparedStatement stmt = con.prepareStatement("select * from user");	
		    rs = stmt.executeQuery();
		    
		    while (rs.next()) {
		    	int id = rs.getInt("id");
		    	String login = rs.getString("login");
		    	String salt = rs.getString("salt");
		    	String first = rs.getString("first");
		    	String last = rs.getString("last");
		    	boolean admin = rs.getBoolean("admin");
		    	User user = new User(id, login, salt, first, last, admin);
		    	users.add(user);
		    }
		    
		    
		} catch (SQLException e) {
			addActionError(e.getMessage());
		}
		finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				context.log("UserController.create",e);
			}
		}
		return Action.SUCCESS;
	}
	
	public String create() {
		Connection con = null;
	    
	    SharedPoolDataSource dbcp = (SharedPoolDataSource) context.getAttribute("dbpool");
	    
	    
		try {
		con = dbcp.getConnection();

		int salt = SecurePassword.chooseNewSalt();
		String hash = SecurePassword.getSaltedHash(password, salt);
		
	    PreparedStatement stmt = con.prepareStatement("insert into user (login,hash,salt,first,last) values (?,?,?,?,?)");	
	    stmt.setString(1, login);
	    stmt.setString(2, hash);
	    stmt.setInt(3, salt);
	    stmt.setString(4, first);
	    stmt.setString(5, last);
	    
	    stmt.executeUpdate();
	    
	    createAccount(hash);
	    
		}
	    catch(Exception ex) {
	    	addActionError(ex.getMessage());  
	    	return Action.INPUT;
	    }
		finally {
				try {
					if (con != null)
						con.close();
				} catch (SQLException e) {
					context.log("UserController.create",e);
				}
		}
	    return Action.SUCCESS;	
	}
	
	// Creates a new account at the same time when a user is created
	public void createAccount(String hash) throws Exception {
		Connection con = null;
		ResultSet rt = null;
	    
	    SharedPoolDataSource dbcp = (SharedPoolDataSource) context.getAttribute("dbpool");
	    
	    
		try {
		con = dbcp.getConnection();
	    
	    PreparedStatement stmtUserId = con.prepareStatement("select id from user where hash=\'"+hash+"\'");	
	    rt = stmtUserId.executeQuery();
	    
	    rt.next();
	    int id = rt.getInt("id");
	    
	    PreparedStatement stmtAccount = con.prepareStatement("insert into account (user_id,balance) values (?,?)");	
	    stmtAccount.setInt(1, id);
	    stmtAccount.setDouble(2, 0);
	    
	    stmtAccount.executeUpdate();
	    
		}
	    catch(Exception ex) { 
	    	throw ex;
	    }
		finally {
				try {
					if (con != null)
						con.close();
				} catch (SQLException e) {
					context.log("UserController.createAccount",e);
				}
		}
	}
	
	// Creates a deposit transaction if valid.
	synchronized public String createDeposit() {
		Connection con = null;
		ResultSet rt = null;
		User curUser = (User) this.session.get("logged");
		
	    SharedPoolDataSource dbcp = (SharedPoolDataSource) context.getAttribute("dbpool");
	    
	    
		try {
			if (depoAmount == null || depoAmount <= 0) {
				throw new Exception("Invalid deposit amount.");
			}
			con = dbcp.getConnection();
			PreparedStatement stmtf = con.prepareStatement("select * from account where user_id=" + curUser.id);	
		    rt = stmtf.executeQuery();
		    rt.next();
		    int account_id = rt.getInt("id");
		    double account_balance = rt.getDouble("balance");
		    
		    PreparedStatement stmt = con.prepareStatement("insert into deposit (account_id, ammount) values (?,?)");	
		    stmt.setInt(1, account_id);
		    stmt.setDouble(2, depoAmount);
		    stmt.executeUpdate();
		    
		    double result_balance = account_balance + depoAmount;
		    PreparedStatement stmtUpdate = con.prepareStatement("update account set balance="+result_balance+" where id="+account_id);
		    stmtUpdate.executeUpdate();
		    balance = result_balance;
		}
	    catch(Exception ex) {
	    	addActionError(ex.getMessage());
	    	payeeList();
			clientBalance();
	    	return Action.INPUT;
	    }
		finally {
				try {
					if (con != null)
						con.close();
				} catch (SQLException e) {
					context.log("UserController.createDeposit",e);
				}
		}
	    return Action.SUCCESS;	
	}
	
	// Creates a payment transaction if valid.
	synchronized public String createPayment() {
		Connection con = null;
		ResultSet rt = null;
		ResultSet rp = null;
		User curUser = (User) this.session.get("logged");
		
	    SharedPoolDataSource dbcp = (SharedPoolDataSource) context.getAttribute("dbpool");
	    
	    
		try {
			con = dbcp.getConnection();
			PreparedStatement stmtf = con.prepareStatement("select * from account where user_id=" + curUser.id);	
		    rt = stmtf.executeQuery();
		    rt.next();
		    int account_id = rt.getInt("id");
		    double account_balance = rt.getDouble("balance");
		    // Trying to pay negative or more than what the account has
		    if (paymentAmount == null || paymentAmount <= 0 || paymentAmount > account_balance) {
				throw new Exception("Invalid payment amount.");
			}
		    
		    PreparedStatement stmtPayee = con.prepareStatement("select * from payee where name=\'" + payeeName + "\'");	
		    rp = stmtf.executeQuery();
		    rp.next();
		    int payee_id = rp.getInt("id");
		    java.util.Date date= new java.util.Date();
		    
		    PreparedStatement stmt = con.prepareStatement("insert into payment (account_id, payee_id, ammount, date) values (?,?,?,?)");	
		    stmt.setInt(1, account_id);
		    stmt.setInt(2, payee_id);
		    stmt.setDouble(3, paymentAmount);
		    stmt.setTimestamp(4, new Timestamp(date.getTime()));
		    stmt.executeUpdate();
		    
		    double result_balance = account_balance - paymentAmount;
		    PreparedStatement stmtUpdate = con.prepareStatement("update account set balance="+result_balance+" where id="+account_id);
		    stmtUpdate.executeUpdate();
		    balance = result_balance;
		}
	    catch(Exception ex) {
	    	addActionError(ex.getMessage());
	    	payeeList();
			clientBalance();
	    	return Action.INPUT;
	    }
		finally {
				try {
					if (con != null)
						con.close();
				} catch (SQLException e) {
					context.log("UserController.createDeposit",e);
				}
		}
	    return Action.SUCCESS;	
	}
	
	public void validate(){
		if (password.length() == 0 )			
			addFieldError( "password", getText("password.required") );
		
		if (login.length()==0)			
			addFieldError( "login", getText("login.required") );			

		if (first.length()==0)			
			addFieldError( "first", getText("first.required") );

		if (last.length()==0)			
			addFieldError( "last", getText("last.required") );
		
	}
	
	// Function for users to log in.
	public String login() {
		Connection con = null;
	    ResultSet rs = null;
	    
	    SharedPoolDataSource dbcp = (SharedPoolDataSource) context.getAttribute("dbpool");
	    
		try {
			con = dbcp.getConnection();
		    PreparedStatement stmt = con.prepareStatement("select * from user");	
		    rs = stmt.executeQuery();
		    
		    if (this.login.equals("")) {
		    	throw new Exception("Empty Login");
		    } else if (this.password.equals("")) {
		    	throw new Exception("Empty Password");
		    }
		    while (rs.next()) {
		    	String login = rs.getString("login");
		    	String salt = rs.getString("salt");
		    	String hash = rs.getString("hash");
		    	
		    	int intSalt = Integer.parseInt(salt);
		    	String hashed = SecurePassword.getSaltedHash(password, intSalt);
		    	
		    	// compare login id
		    	if (login.equals(this.login)) {
		    		// compare password
		    		if (hash.equals(hashed)) {
				    	int id = rs.getInt("id");
				    	String first = rs.getString("first");
				    	String last = rs.getString("last");
				    	boolean admin = rs.getBoolean("admin");
				    	User user = new User(id, login, salt, first, last, admin);
				    	this.session.put("logged", user);
				    	if (admin) {
				    		return Action.INPUT;
				    	}
				    	clientBalance();
				    	payeeList();
				    	return Action.SUCCESS;
		    		}
		    	}
		    }
		    throw new Exception("Invalid Id or Password.");
		} catch (SQLException e) {
			addActionError(e.getMessage()); 
		} catch(Exception ex) {
	    	addActionError(ex.getMessage());
	    	return Action.NONE;
	    }
		
		return Action.NONE;
	}
	
	// Function to log out a user.
	public String logout() {
		this.session.clear();
		return Action.SUCCESS;
	}
	
	// Retrieve a list of payees.
	public void payeeList() {
		
		Connection con = null;
	    ResultSet rs = null;
	    
	    SharedPoolDataSource dbcp = (SharedPoolDataSource) context.getAttribute("dbpool");
	    
		try {
			con = dbcp.getConnection();
		    PreparedStatement stmt = con.prepareStatement("select * from payee");	
		    rs = stmt.executeQuery();
		    
		    // loop through all payees and put them in a list.
		    while (rs.next()) {
		    	int id = rs.getInt("id");
		    	String name = rs.getString("name");
		    	String description = rs.getString("description");
		    	Payee payee = new Payee(id, name, description);
		    	payees.add(payee);
		    }
		    
		    
		} catch (SQLException e) {
			addActionError(e.getMessage()); 
		}
		finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				context.log("UserController.create",e);
			}
		}
	}
	
	// Retrieve the currently balance in the user's account.
	public void clientBalance() {
		
		Connection con = null;
		ResultSet rt = null;
		User curUser = (User) this.session.get("logged");
		
	    SharedPoolDataSource dbcp = (SharedPoolDataSource) context.getAttribute("dbpool");
	    
	    
		try {
			con = dbcp.getConnection();
			PreparedStatement stmtf = con.prepareStatement("select * from account where user_id=" + curUser.id);	
		    rt = stmtf.executeQuery();
		    rt.next();
		    balance = rt.getDouble("balance");
		}
	    catch(Exception ex) {
	    }
	}
	
	// Function that helps opening client home when user is already logged in.
	public String openClient() {
		try {
			payeeList();
			clientBalance();
		} catch(Exception ex) {
	    	addActionError(ex.getMessage());
	    }
		return Action.SUCCESS;
	}
	
	// Retrieve transaction history for currently logged user.
	public String transactionHistory() {
		
		Connection con = null;
		ResultSet rf=null;
	    ResultSet rDeposit = null;
	    ResultSet rPayment = null;
	    User curUser = (User) this.session.get("logged");
	    
	    SharedPoolDataSource dbcp = (SharedPoolDataSource) context.getAttribute("dbpool");
	    
		try {
			con = dbcp.getConnection();
			PreparedStatement stmtf = con.prepareStatement("select * from account where user_id=" + curUser.id);	
		    rf = stmtf.executeQuery();
		    rf.next();
		    int account_idTemp = rf.getInt("id");
		    
		    PreparedStatement stmtDeposit = con.prepareStatement("select * from deposit where account_id=" + account_idTemp );	
		    rDeposit = stmtDeposit.executeQuery();
			
		    PreparedStatement stmtPayment = con.prepareStatement("select * from payment where account_id=" + account_idTemp );	
		    rPayment = stmtPayment.executeQuery();
		    
		    
		    // retrieve payment history
		    while (rPayment.next()) {
		    	int id = rPayment.getInt("id");
		    	int account_id = rPayment.getInt("account_id");
		    	int payee_id=rPayment.getInt("payee_id");
		    	double ammount = rPayment.getDouble("ammount");
		    	String date=rPayment.getString("date");
		    	Payment payment = new Payment(id, account_id, payee_id, ammount, date );
		    	payments.add(payment);
		    }
		    
		    // retrieve deposit history
		    while (rDeposit.next()) {
		    	int id = rDeposit.getInt("id");
		    	int account_id = rDeposit.getInt("account_id");
		    	double ammount = rDeposit.getDouble("ammount");
		    	Deposit deposit = new Deposit(id, account_id, ammount);
		    	deposits.add(deposit);
		    }
		    
		    
		} catch (SQLException e) {
			addActionError(e.getMessage()); 
		}
		finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				context.log("PaymentController.create",e);
			}
		}
		return Action.SUCCESS;
	}
	
	// Retrieves transaction history for all users.
	public String managerHistory() {
		Connection con = null;
	    ResultSet rs = null;
	    
	    SharedPoolDataSource dbcp = (SharedPoolDataSource) context.getAttribute("dbpool");
	    
		try {
			con = dbcp.getConnection();
		    PreparedStatement stmt = con.prepareStatement("select * from user");	
		    rs = stmt.executeQuery();
		    
		    // Loop through each user and retrieve their transaction history
		    while (rs.next()) {
		    	boolean admin = rs.getBoolean("admin");
		    	if (!admin) {
			    	int id = rs.getInt("id");
			    	String login = rs.getString("login");
			    	String salt = rs.getString("salt");
			    	String first = rs.getString("first");
			    	String last = rs.getString("last");
			    	
			    	User user = new User(id, login, salt, first, last, admin);
			    	singleHistory(user);
		    	}
		    }
		} catch (SQLException e) {
			addActionError(e.getMessage());
		}
		
		return Action.SUCCESS;
	}
	
	// Retrieve transaction history for single user.
	public void singleHistory(User curUser) {
		Connection con = null;
		ResultSet rf=null;
	    ResultSet rDeposit = null;
	    ResultSet rPayment = null;
	    
	    SharedPoolDataSource dbcp = (SharedPoolDataSource) context.getAttribute("dbpool");
	    
		try {
			con = dbcp.getConnection();
			PreparedStatement stmtf = con.prepareStatement("select * from account where user_id=" + curUser.id);	
		    rf = stmtf.executeQuery();
		    rf.next();
		    int account_idTemp = rf.getInt("id");
		    
		    PreparedStatement stmtDeposit = con.prepareStatement("select * from deposit where account_id=" + account_idTemp );	
		    rDeposit = stmtDeposit.executeQuery();
			
		    PreparedStatement stmtPayment = con.prepareStatement("select * from payment where account_id=" + account_idTemp );	
		    rPayment = stmtPayment.executeQuery();
		    
		    
		    // retrieve payment history
		    while (rPayment.next()) {
		    	int id = rPayment.getInt("id");
		    	int account_id = rPayment.getInt("account_id");
		    	int payee_id=rPayment.getInt("payee_id");
		    	double ammount = rPayment.getDouble("ammount");
		    	String date=rPayment.getString("date");
		    	Payment payment = new Payment(id, account_id, payee_id, ammount, date );
		    	payments.add(payment);
		    }
		    
		    // retrieve deposit history
		    while (rDeposit.next()) {
		    	int id = rDeposit.getInt("id");
		    	int account_id = rDeposit.getInt("account_id");
		    	double ammount = rDeposit.getDouble("ammount");
		    	Deposit deposit = new Deposit(id, account_id, ammount);
		    	deposits.add(deposit);
		    }
		    
		    
		} catch (Exception e) {
			addActionError(e.getMessage()); 
		}
	}
	
	// Back button function for transaction history page.
	public String transBack() {
		User curUser = (User) this.session.get("logged");
		
		// If user is admin.
		if (curUser.admin) {
			return Action.INPUT;
		}
		// If user is client.
		return Action.SUCCESS;
	}
	
	public void setSession(Map arg0) {
		session = arg0;
	}
	
	public void setServletContext(ServletContext arg0) {
		context = arg0;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
	
	public double getDepoAmount() {
		return depoAmount;
	}

	public void setDepoAmount(double depoAmount) {
		this.depoAmount = depoAmount;
	}
	
	public double getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	
	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public Enumeration getUsers() {
		return users.elements();
	}
	
	public Enumeration getPayments() {
		return payments.elements();
	}
	
	public Enumeration getDeposits() {
		return deposits.elements();
	}
	
	public Enumeration getPayees() {
		return payees.elements();
	}
	
	public double getBalance() {
		return balance;
	}
	

}
