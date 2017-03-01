package classes;

import java.util.Date;

public class Data {

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getFood() {
		return food;
	}

	public void setFood(String food) {
		this.food = food;
	}

	public boolean isSweets() {
		return sweets;
	}

	public void setSweets(boolean sweets) {
		this.sweets = sweets;
	}

	public boolean isSport() {
		return sport;
	}

	public void setSport(boolean sport) {
		this.sport = sport;
	}

	public double getKcal_sport() {
		return kcal_sport;
	}

	public void setKcal_sport(double kcal_sport) {
		this.kcal_sport = kcal_sport;
	}

	private String uid;
	private Date date;
	private double weight;
	private String food;
	private boolean sweets;
	private boolean sport;
	private double kcal_sport;

}
