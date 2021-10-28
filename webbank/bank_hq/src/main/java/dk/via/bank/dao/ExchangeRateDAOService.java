package dk.via.bank.dao;

import dk.via.bank.dao.exception.NotFound;
import dk.via.bank.model.ExchangeRate;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/exchange_rate")
public class ExchangeRateDAOService {
	private final ExchangeRateMapper mapper;
	private final DatabaseHelper<ExchangeRate> helper;

	@SuppressWarnings("unused")
	public ExchangeRateDAOService() {
		this(DatabaseHelper.JDBC_URL, DatabaseHelper.USERNAME, DatabaseHelper.PASSWORD);
	}

	public ExchangeRateDAOService(String jdbcURL, String username, String password) {
		mapper = new ExchangeRateMapper();
		helper = new DatabaseHelper<>(jdbcURL, username, password);
	}

	private static class ExchangeRateMapper implements DataMapper<ExchangeRate> {
		@Override
		public ExchangeRate create(ResultSet rs) throws SQLException {
			return new ExchangeRate(rs.getString("from_currency"), rs.getString("to_currency"), rs.getBigDecimal("rate"));
		}
	}

	@GetMapping
	public List<ExchangeRate> getRates(@RequestParam(value = "fromCurrency", required = false) String fromCurrency, @RequestParam(value = "toCurrency", required = false) String toCurrency) {
		List<ExchangeRate> rates;
		if (fromCurrency != null && toCurrency != null)
			rates = helper.map(mapper, "SELECT * FROM Exchange_rates WHERE from_currency = ? AND to_currency = ?", fromCurrency, toCurrency);
		else if (fromCurrency != null)
			rates = helper.map(mapper, "SELECT * FROM Exchange_rates WHERE from_currency = ?", fromCurrency);
		else if (toCurrency != null)
			rates = helper.map(mapper, "SELECT * FROM Exchange_rates WHERE to_currency = ?", toCurrency);
		else
			rates = helper.map(mapper, "SELECT * FROM Exchange_rates");
		return rates;
	}

	@GetMapping("/{from}")
	public List<ExchangeRate> getExchangeRate(@PathVariable("from") String fromCurrency) {
		return helper.map(mapper, "SELECT * FROM Exchange_rates WHERE from_currency = ?", fromCurrency);
	}

	@GetMapping("/{from}/{to}")
	public ExchangeRate getExchangeRate(@PathVariable("from") String fromCurrency, @PathVariable("to") String toCurrency) {
		ExchangeRate rate = helper.mapSingle(mapper, "SELECT * FROM Exchange_rates WHERE from_currency = ? AND to_currency = ?", fromCurrency, toCurrency);
		if (rate == null) {
			throw new NotFound();
		}
		return rate;
	}
}
