package csc309.A4;

public class Payee {
	int id;
	String name;
	String comment;
	
	public Payee(int id, String name, String comment) {
		super();
		this.id = id;
		this.name = name;
		this.comment = comment;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}