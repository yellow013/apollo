package io.redstone.engine.storage;

import javax.annotation.concurrent.NotThreadSafe;

import org.eclipse.collections.api.map.primitive.MutableIntObjectMap;
import org.slf4j.Logger;

import io.mercury.common.collections.InitialCapacity;
import io.mercury.common.collections.MutableMaps;
import io.mercury.common.log.CommonLoggerFactory;
import io.redstone.core.order.api.Order;

@NotThreadSafe
public final class OrderKeeper {

	private static final Logger logger = CommonLoggerFactory.getLogger(OrderKeeper.class);

	private static final OrderKeeper InnerInstance = new OrderKeeper();

	/**
	 * 存储所有的order
	 */
	private final OrderBook allOrders = OrderBook.newInstance(InitialCapacity.L16_Size_65536);

	/**
	 * 按照subAccount分组存储
	 */
	private final MutableIntObjectMap<OrderBook> subAccountOrderBookMap = MutableMaps.newIntObjectHashMap();

	/**
	 * 按照account分组存储
	 */
	private final MutableIntObjectMap<OrderBook> accountOrderBookMap = MutableMaps.newIntObjectHashMap();

	/**
	 * 按照strategy分组存储
	 */
	private final MutableIntObjectMap<OrderBook> strategyOrderBookMap = MutableMaps.newIntObjectHashMap();

	/**
	 * 按照instrument分组存储
	 */
	private final MutableIntObjectMap<OrderBook> instrumentOrderBookMap = MutableMaps.newIntObjectHashMap();

	private OrderKeeper() {
	}

	public static void onOrder(Order order) {

	}

	public static void updateOrder(Order order) {
		switch (order.getStatus()) {
		case Filled:
		case Canceled:
		case NewRejected:
			getAllOrders().terminatedOrder(order);
			int subAccountId = order.getSubAccountId();
			int accountId = AccountKeeper.getAccountId(subAccountId);
			getSubAccountOrderBook(subAccountId).terminatedOrder(order);
			getAccountOrderBook(accountId).terminatedOrder(order);
			getStrategyOrderBook(order.getStrategyId()).terminatedOrder(order);
			getInstrumentOrderBook(order.getInstrument().getInstrumentId()).terminatedOrder(order);
			break;
		default:
			logger.info("Not need processed -> OrdSysId==[{}], OrdStatus==[{}]", order.getOrdSysId(),
					order.getStatus());
			break;
		}
	}

	public static void insertOrder(Order order) {
		getAllOrders().putOrder(order);
		int subAccountId = order.getSubAccountId();
		int accountId = AccountKeeper.getAccountId(subAccountId);
		getSubAccountOrderBook(subAccountId).putOrder(order);
		getAccountOrderBook(accountId).putOrder(order);
		getStrategyOrderBook(order.getStrategyId()).putOrder(order);
		getInstrumentOrderBook(order.getInstrument().getInstrumentId()).putOrder(order);
	}

	public static boolean containsOrder(long ordSysId) {
		return InnerInstance.allOrders.containsOrder(ordSysId);
	}

	public static Order getOrder(long ordSysId) {
		return InnerInstance.allOrders.getOrder(ordSysId);
	}

	public static OrderBook getAllOrders() {
		return InnerInstance.allOrders;
	}

	public static OrderBook getSubAccountOrderBook(int subAccountId) {
		OrderBook subAccountOrderBook = InnerInstance.subAccountOrderBookMap.get(subAccountId);
		if (subAccountOrderBook == null) {
			subAccountOrderBook = OrderBook.newInstance(InitialCapacity.L10_Size_1024);
			InnerInstance.subAccountOrderBookMap.put(subAccountId, subAccountOrderBook);
		}
		return subAccountOrderBook;
	}

	public static OrderBook getAccountOrderBook(int accountId) {
		OrderBook accountOrderBook = InnerInstance.accountOrderBookMap.get(accountId);
		if (accountOrderBook == null) {
			accountOrderBook = OrderBook.newInstance(InitialCapacity.L11_Size_2048);
			InnerInstance.accountOrderBookMap.put(accountId, accountOrderBook);
		}
		return accountOrderBook;
	}

	public static OrderBook getStrategyOrderBook(int strategyId) {
		OrderBook strategyOrderBook = InnerInstance.strategyOrderBookMap.get(strategyId);
		if (strategyOrderBook == null) {
			strategyOrderBook = OrderBook.newInstance(InitialCapacity.L12_Size_4096);
			InnerInstance.strategyOrderBookMap.put(strategyId, strategyOrderBook);
		}
		return strategyOrderBook;
	}

	public static OrderBook getInstrumentOrderBook(int instrumentId) {
		OrderBook instrumentOrderBook = InnerInstance.instrumentOrderBookMap.get(instrumentId);
		if (instrumentOrderBook == null) {
			instrumentOrderBook = OrderBook.newInstance(InitialCapacity.L11_Size_2048);
			InnerInstance.instrumentOrderBookMap.put(instrumentId, instrumentOrderBook);
		}
		return instrumentOrderBook;
	}

}