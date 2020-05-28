package io.mercury.redstone.core.order;

import org.eclipse.collections.api.set.ImmutableSet;

import io.mercury.common.collections.ImmutableSets;
import io.mercury.redstone.core.order.structure.OrdTimestamp;

public final class OrderGroup {

	private long groupOrdSysId;
	private OrdTimestamp ordTimestamp;

	private ImmutableSet<Order> orderSet;

	public OrderGroup(Order... orders) {
		this.orderSet = ImmutableSets.newSet(orders);
		this.ordTimestamp = OrdTimestamp.generate();
	}

	public long groupOrdSysId() {
		return groupOrdSysId;
	}

	public ImmutableSet<Order> getOrderSet() {
		return orderSet;
	}

	public OrdTimestamp ordTimestamp() {
		return ordTimestamp;
	}

}
