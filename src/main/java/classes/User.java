package classes;

import java.util.Date;

public class User {

	private int uid;
	private String firstName;
	private String lastName;
	private String nickName;
	private double startWeight;
	private Date startDate;
	private Date birthDate;
	
//	public User (int startUid) {
//		uid = startUid;
//	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public double getStartWeight() {
		return startWeight;
	}

	public void setStartWeight(double startWeight) {
		this.startWeight = startWeight;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

}
