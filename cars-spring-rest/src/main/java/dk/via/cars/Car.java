package dk.via.cars;

import java.math.BigDecimal;

public class Car {
	private String licenseNumber;
	private String model;
	private int year;
	private Money price;

	public Car() {
	}
	
	public Car(String licenseNumber, String model, int year, Money price) {
		this.licenseNumber = licenseNumber;
		this.model = model;
		this.year = year;
		this.price = price;
	}

	public Car(String licenseNumber, String model, int year, BigDecimal amount, String currency) {
		this(licenseNumber, model, year, new Money(amount, currency));
	}

	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Money getPrice() {
		return price;
	}

	public void setPrice(Money price) {
		this.price = price;
	}
}
