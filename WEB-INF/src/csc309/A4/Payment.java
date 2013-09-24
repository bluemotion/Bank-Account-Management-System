package csc309.A4;

public class Payment {
	int id;
	int account_id;
	int payee_id;
	String date;
	double ammount;
	
	public Payment(int id, int account_id, int payee_id, double ammount, String date) {
		super();
		this.id = id;
		this.account_id = account_id;
		this.payee_id = payee_id;
		this.ammount = ammount;
		this.date = date;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAccount_id() {
		return account_id;
	}
	public void setAccount_id(int account_id) {
		this.account_id = account_id;
	}
	public int getPayee_id() {
		return payee_id;
	}
	public void setPayee_id(int payee_id) {
		this.payee_id = payee_id;
	}	
	public double getAmmount() {
		return ammount;
	}
	public void setAmmount(double ammount) {
		this.ammount = ammount;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}