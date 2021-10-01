package dk.via.bank.dao;

import dk.via.bank.dao.exception.Conflict;
import dk.via.bank.dao.exception.MethodNotAllowed;
import dk.via.bank.dao.exception.NotFound;
import dk.via.bank.model.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@RestController
@RequestMapping("/customers")
public class CustomerDAOService {
	private final DatabaseHelper<Customer> helper;

	public CustomerDAOService() {
		this(DatabaseHelper.JDBC_URL, DatabaseHelper.USERNAME, DatabaseHelper.PASSWORD, new AccountDAOService());
	}
	
	public CustomerDAOService(String jdbcURL, String username, String password, AccountDAOService accountDAO) {
		this.helper = new DatabaseHelper<>(jdbcURL, username, password);
	}
	
	private static class CustomerMapper implements DataMapper<Customer> {
		@Override
		public Customer create(ResultSet rs) throws SQLException {
			String cpr = rs.getString("cpr");
			String name = rs.getString("name");
			String address = rs.getString("address");
			return new Customer(cpr, name, address);
		}
	}

	@GetMapping
	public Collection<Customer> readCustomers() {
		throw new MethodNotAllowed();
	}

	@PostMapping
	public Customer createCustomer(@RequestBody Customer customer) {
		helper.executeUpdate("INSERT INTO Customer VALUES (?, ?, ?)", customer.getCpr(), customer.getName(), customer.getAddress());
		return customer;
	}

	@GetMapping("/{cpr}")
	public Customer readCustomer(@PathVariable("cpr") String cpr) {
		CustomerMapper mapper = new CustomerMapper();
		Customer customer = helper.mapSingle(mapper, "SELECT * FROM Customer WHERE cpr = ?;", cpr);
		if (customer == null) {
			throw new NotFound();
		}
		return customer;
	}

	@PutMapping("/{cpr}")
	public ResponseEntity<Customer> updateCustomer(@PathVariable("cpr") String cpr, @RequestBody Customer customer) {
		if (customer.getCpr() != null && !customer.getCpr().isEmpty() && !customer.getCpr().equals(cpr)) {
			throw new Conflict();
		}
		if (helper.mapSingle(new CustomerMapper(), "SELECT * FROM Customer WHERE cpr = ?;", cpr) == null) {
			helper.executeUpdate("INSERT INTO Customer VALUES (?, ?, ?)", cpr, customer.getName(), customer.getAddress());
			return ResponseEntity.status(HttpStatus.CREATED).body(customer);
		} else {
			helper.executeUpdate("UPDATE Customer set name = ?, address = ? WHERE cpr = ?", customer.getName(), customer.getAddress(), cpr);
			return ResponseEntity.status(HttpStatus.OK).body(customer);
		}
	}

	@DeleteMapping("/{cpr}")
	public void deleteCustomer(@PathVariable("cpr") String cpr) {
		helper.executeUpdate("DELETE FROM Customer WHERE cpr = ?", cpr);
	}
}
