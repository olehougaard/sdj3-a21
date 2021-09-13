package dk.via.bank;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.List;

import dk.via.bank.dao.AccountDAO;
import dk.via.bank.dao.CustomerDAO;
import dk.via.bank.dao.ExchangeRateDAO;
import dk.via.bank.dao.HeadQuarters;
import dk.via.bank.dao.TransactionDAO;
import dk.via.bank.model.*;
import dk.via.bank.model.transaction.*;

public class RemoteBranch extends UnicastRemoteObject implements Branch {
	private static final long serialVersionUID = 1;
	private final BranchImpl branch;

	public RemoteBranch(int regNumber, HeadQuarters hq) throws RemoteException {
		branch = new BranchImpl(regNumber, hq);
	}

	@Override
	public Customer createCustomer(String cpr, String name, String address) throws RemoteException {
		return branch.createCustomer(cpr, name, address);
	}

	@Override
	public Customer getCustomer(String cpr) throws RemoteException {
		return branch.getCustomer(cpr);
	}

	@Override
	public Account createAccount(Customer customer, String currency) throws RemoteException {
		return branch.createAccount(customer, currency);
	}

	@Override
	public Account getAccount(AccountNumber accountNumber) throws RemoteException {
		return branch.getAccount(accountNumber);
	}
	
	@Override
	public void cancelAccount(Account account) throws RemoteException {
		branch.cancelAccount(account);
	}

	@Override
	public Collection<Account> getAccounts(Customer customer) throws RemoteException {
		return branch.getAccounts(customer);
	}
	
	@Override
	public void execute(Transaction t) throws RemoteException {
		branch.execute(t);
	}

	@Override
	public Money exchange(Money amount, String targetCurrency) throws RemoteException {
		return branch.exchange(amount, targetCurrency);
	}
	
	@Override
	public List<Transaction> getTransactionsFor(Account primaryAccount) throws RemoteException {
		return branch.getTransactionsFor(primaryAccount);
	}
}
