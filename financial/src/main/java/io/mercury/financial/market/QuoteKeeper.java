package io.mercury.financial.market;

import java.util.concurrent.atomic.AtomicLong;

import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.primitive.ImmutableIntObjectMap;
import org.eclipse.collections.api.map.primitive.MutableIntObjectMap;

import io.mercury.common.collections.MutableMaps;
import io.mercury.financial.instrument.Instrument;
import io.mercury.financial.instrument.InstrumentKeeper;
import io.mercury.financial.market.impl.BasicMarketData;

/**
 * 管理当前最新行情
 * 
 * @creation 2019年4月16日
 */
public final class QuoteKeeper {

	private final ImmutableIntObjectMap<AtomicQuote> QuoteMap;

	private final static QuoteKeeper InnerInstance = new QuoteKeeper();

	private QuoteKeeper() {
		MutableIntObjectMap<AtomicQuote> tempQuoteMap = MutableMaps.newIntObjectHashMap();
		ImmutableList<Instrument> allInstrument = InstrumentKeeper.getAllInstrument();
		if (allInstrument.isEmpty())
			throw new IllegalStateException("InstrumentKeeper is uninitialized");
		allInstrument.forEach(instrument -> tempQuoteMap.put(instrument.id(), new AtomicQuote()));
		QuoteMap = tempQuoteMap.toImmutable();
	}

	public static void onMarketDate(BasicMarketData marketData) {
		AtomicQuote atomicQuote = InnerInstance.QuoteMap.get(marketData.instrument().id());
		if (atomicQuote == null)
			return;
		atomicQuote.getAskPrice1().set(marketData.getAskPrice1());
		atomicQuote.getAskVolume1().set(marketData.getAskVolume1());
		atomicQuote.getBidPrice1().set(marketData.getBidPrice1());
		atomicQuote.getBidVolume1().set(marketData.getBidVolume1());
	}

	public static AtomicQuote getQuote(Instrument instrument) {
		return InnerInstance.QuoteMap.get(instrument.id());
	}

	public static class AtomicQuote {

		private AtomicLong askPrice1;
		private AtomicLong askVolume1;
		private AtomicLong bidPrice1;
		private AtomicLong bidVolume1;

		public AtomicQuote() {
			super();
			this.askPrice1 = new AtomicLong();
			this.askVolume1 = new AtomicLong();
			this.bidPrice1 = new AtomicLong();
			this.bidVolume1 = new AtomicLong();
		}

		public AtomicLong getAskPrice1() {
			return askPrice1;
		}

		public AtomicLong getAskVolume1() {
			return askVolume1;
		}

		public AtomicLong getBidPrice1() {
			return bidPrice1;
		}

		public AtomicLong getBidVolume1() {
			return bidVolume1;
		}

	}

}
