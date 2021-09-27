package dk.via.cars;

import java.math.BigDecimal;

public class Money {
	private BigDecimal amount;
	private String currency;
	
	public Money() {
	}
	
	public Money(BigDecimal amount, String currency) {
		this.amount = amount;
		this.currency = currency;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		return "Money (" +
				"amount = " + amount +
				", currency = '" + currency + '\'' +
				')';
	}
}
