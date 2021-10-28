package dk.via.bank.dao;

import dk.via.bank.dao.exception.BadRequest;
import dk.via.bank.dao.exception.Conflict;
import dk.via.bank.dao.exception.MethodNotAllowed;
import dk.via.bank.dao.exception.NotFound;
import dk.via.bank.model.Account;
import dk.via.bank.model.AccountNumber;
import dk.via.bank.model.Money;
import dk.via.bank.model.parameters.AccountSpecification;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/customers/{cpr}/accounts")
public class AccountDAOService  {
	private final DatabaseHelper<Account> helper;

	public AccountDAOService() {
		this(DatabaseHelper.JDBC_URL, DatabaseHelper.USERNAME, DatabaseHelper.PASSWORD);
	}
	
	public AccountDAOService(String jdbcURL, String username, String password) {
		helper = new DatabaseHelper<>(jdbcURL, username, password);
	}

	@PostMapping
	public Account createAccount(@PathVariable("cpr") String cpr, @RequestBody AccountSpecification specification) {
		int regNumber = specification.getRegNumber();
		String currency = specification.getCurrency();
		final List<Integer> keys = helper.executeUpdateWithGeneratedKeys("INSERT INTO Account(reg_number, customer, currency) VALUES (?, ?, ?)",
				regNumber, cpr, currency);
		return getAccount(new AccountNumber(regNumber, keys.get(0)), cpr);
	}
	
	public static class AccountMapper implements DataMapper<Account>{
		@Override
		public Account create(ResultSet rs) throws SQLException {
			AccountNumber accountNumber = new AccountNumber(rs.getInt("reg_number"), rs.getLong("account_number"));
			BigDecimal balance = rs.getBigDecimal("balance");
			String currency = rs.getString("currency");
			String customerCpr = rs.getString("customer");
			return new Account(accountNumber, new Money(balance, currency), customerCpr);
		}
		
	}

	@GetMapping
    public Collection<Account> readAccountsFor(@PathVariable("cpr") String cpr) {
		return helper.map(new AccountMapper(), "SELECT * FROM Account WHERE customer = ? AND active", cpr) ;
	}

	@GetMapping("/{accountNumber}")
    public Account readAccount(@PathVariable("cpr") String cpr, @PathVariable("accountNumber") String accountString) {
		Account account = getAccount(AccountNumber.fromString(accountString), cpr);
		if (account == null) {
			throw new NotFound();
		}
		return account;
	}

	Account getAccount(AccountNumber accountNumber, String targetCpr) {
		return helper.mapSingle(new AccountMapper(),
				"SELECT * FROM Account WHERE reg_number = ? AND account_number = ? AND customer LIKE ? AND active",
				accountNumber.getRegNumber(), accountNumber.getAccountNumber(), targetCpr);
	}

	@PutMapping("/{accountNumber}")
    public void updateAccount(@PathVariable("cpr") String cpr, @PathVariable("accountNumber") String accountString, @RequestBody Account account) {
		AccountNumber accountNumber = AccountNumber.fromString(accountString);
		if (account.getAccountNumber() != null && !account.getAccountNumber().equals(accountNumber)) {
			throw new Conflict();
		}
		if (getAccount(accountNumber, cpr) == null) {
			throw new MethodNotAllowed();
		}
		if (!account.getSettledCurrency().equals(account.getBalance().getCurrency())) {
			throw new BadRequest();
		}
		helper.executeUpdate("UPDATE ACCOUNT SET balance = ?, currency = ? WHERE reg_number = ? AND account_number = ? AND active",
				account.getBalance().getAmount(), account.getSettledCurrency(), accountNumber.getRegNumber(), accountNumber.getAccountNumber());
	}

	@DeleteMapping("/{accountNumber}")
    public void deleteAccount(@PathVariable("cpr") String cpr, @PathVariable("accountNumber") String accountString) {
		AccountNumber accountNumber = AccountNumber.fromString(accountString);
		helper.executeUpdate(
				"UPDATE ACCOUNT SET active = FALSE WHERE reg_number = ? AND account_number = ? AND customer = ?",
				accountNumber.getRegNumber(), accountNumber.getAccountNumber(), cpr);
	}
}
