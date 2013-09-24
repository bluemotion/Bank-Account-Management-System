package csc309.A4;

public class Deposit {
	int id;
	int account_id;
	double ammount;
	
	public Deposit(int id, int account_id, double ammount) {
		super();
		this.id = id;
		this.account_id = account_id;
		this.ammount = ammount;
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
	public void setLogin(int account_id) {
		this.account_id = account_id;
	}
	public double getAmmount() {
		return ammount;
	}
	public void setAmmount(double ammount) {
		this.ammount = ammount;
	}
}