package dk.via.cars;

import dk.via.cars.ws.Cars;
import dk.via.db.DataMapper;
import dk.via.db.DatabaseHelper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CarDAO implements Cars {
	private final DatabaseHelper<Car> helper;

	private final static String JDBC_URL = "jdbc:postgresql://localhost:5432/postgres?currentSchema=car_base";

	private final static String USERNAME = "postgres";

	private final static String PASSWORD = "password";

	public CarDAO() {
		this.helper = new DatabaseHelper<>(JDBC_URL, USERNAME, PASSWORD);
	}

	private static Car createCarDTO(String licenseNo, String model, int year, Money price) {
		Car car = new Car();
		car.setLicenseNumber(licenseNo);
		car.setModel(model);
		car.setYear(year);
		car.setPrice(price);
		return car;
	}

	public Car create(String licenseNo, String model, int year, Money price)  {
		helper.executeUpdate("INSERT INTO car VALUES (?, ?, ?, ?, ?)", licenseNo, model, year, price.getAmount(), price.getCurrency());
		return createCarDTO(licenseNo, model, year, price);
	}

	private static class CarMapper implements DataMapper<Car> {
		public Car create(ResultSet rs) throws SQLException {
			String licenseNumber = rs.getString("license_number");
			String model = rs.getString("model");
			int year = rs.getInt("year");
			BigDecimal priceAmount = rs.getBigDecimal("price_amount");
			String priceCurrency = rs.getString("price_currency");
			Money price = new Money();
			price.setAmount(priceAmount);
			price.setCurrency(priceCurrency);
			return createCarDTO(licenseNumber, model, year, price);
		}
	}

	public List<Car> readAll() {
		return helper.map(new CarMapper(), "SELECT * FROM car");
	}

	@Override
	public Car read(String licenseNumber) {
		return helper.mapSingle(new CarMapper(), "SELECT * FROM car WHERE license_number = ?", licenseNumber);
	}

	public void delete(String license_number) {
		helper.executeUpdate("DELETE FROM car WHERE license_number = ?", license_number);
	}
}
