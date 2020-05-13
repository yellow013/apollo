package io.redstone.core.keeper;

import javax.annotation.concurrent.NotThreadSafe;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.primitive.MutableIntObjectMap;
import org.slf4j.Logger;

import io.mercury.common.collections.MutableLists;
import io.mercury.common.collections.MutableMaps;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.financial.instrument.Instrument;
import io.redstone.core.strategy.Strategy;

@NotThreadSafe
public final class StrategyKeeper {

	private static final Logger log = CommonLoggerFactory.getLogger(StrategyKeeper.class);

	/**
	 * 策略列表
	 */
	private static final MutableIntObjectMap<Strategy> StrategyMap = MutableMaps.newIntObjectHashMap();

	/**
	 * 子账户与策略的对应关系
	 */
	private static final MutableIntObjectMap<MutableList<Strategy>> SubAccountStrategysMap = MutableMaps
			.newIntObjectHashMap();

	/**
	 * 订阅合约的策略列表
	 */
	private static final MutableIntObjectMap<MutableList<Strategy>> SubscribedInstrumentMap = MutableMaps
			.newIntObjectHashMap();

	private StrategyKeeper() {
	}

	/**
	 * 添加策略并置为激活, 同时添加策略订阅的Instrument
	 * 
	 * @param strategy
	 */
	public static void putStrategy(Strategy strategy) {
		StrategyMap.put(strategy.strategyId(), strategy);
		log.info("StrategyKeeper :: Put strategy, strategyId==[{}]", strategy.strategyId());
		strategy.instruments().forEach(instrument -> {
			SubscribedInstrumentMap.getIfAbsentPut(instrument.id(), MutableLists::newFastList).add(strategy);
			log.info("StrategyKeeper :: Add subscribe instrument, strategyId==[{}], instrumentId==[{}]",
					strategy.strategyId(), instrument.id());
		});
		SubAccountStrategysMap.getIfAbsentPut(strategy.subAccountId(), MutableLists::newFastList).add(strategy);
		
		strategy.subAccountId();
		strategy.enable();
		log.info("StrategyKeeper :: Strategy is enable, strategyId==[{}]", strategy.strategyId());
	}

	public static Strategy getStrategy(int strategyId) {
		return StrategyMap.get(strategyId);
	}

	public static MutableList<Strategy> getSubscribedStrategys(Instrument instrument) {
		return getSubscribedStrategys(instrument.id());
	}

	public static MutableList<Strategy> getSubscribedStrategys(int instrumentId) {
		return SubscribedInstrumentMap.get(instrumentId);
	}

}