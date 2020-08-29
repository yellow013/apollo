package io.mercury.redstone.core.adaptor;

import java.util.List;

import javax.annotation.Nonnull;

import io.mercury.common.collections.MutableLists;
import io.mercury.common.fsm.EnableComponent;
import io.mercury.common.util.Assertor;
import io.mercury.financial.instrument.InstrumentManager;
import io.mercury.redstone.core.account.Account;
import io.mercury.redstone.core.account.AccountKeeper;

public abstract class AdaptorBaseImpl extends EnableComponent<Adaptor> implements Adaptor {

	private final int adaptorId;
	private final String adaptorName;
	private final Account account;
	private final List<Account> accounts;

	public AdaptorBaseImpl(int adaptorId, @Nonnull Account... accounts) {
		Assertor.requiredLength(accounts, 1, "accounts");
		this.adaptorId = adaptorId;
		this.account = accounts[0];
		this.accounts = MutableLists.newFastList(accounts);
		this.adaptorName = "Broker[ " + account.brokerName() + "]-InvestorId[" + account.investorId() + "]";
		AdaptorKeeper.putAdaptor(this);
	}

	@Override
	public int adaptorId() {
		return adaptorId;
	}

	@Override
	public String adaptorName() {
		return adaptorName;
	}

	@Override
	public Account account() {
		return account;
	}

	@Override
	public List<Account> accounts() {
		return accounts;
	}

	@Override
	protected Adaptor returnThis() {
		return this;
	}

	@Override
	public boolean startup() {
		if (!AccountKeeper.isInitialized())
			throw new IllegalStateException("Account Keeper uninitialized");
		if (!InstrumentManager.isInitialized())
			throw new IllegalStateException("Instrument Manager uninitialized");
		return innerStartup();
	}

	protected abstract boolean innerStartup();

}
