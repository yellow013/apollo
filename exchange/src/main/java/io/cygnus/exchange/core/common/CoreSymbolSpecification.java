package io.cygnus.exchange.core.common;

import java.util.Objects;

import io.cygnus.exchange.core.common.enums.SymbolType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.ToString;
import net.openhft.chronicle.bytes.BytesIn;
import net.openhft.chronicle.bytes.BytesOut;
import net.openhft.chronicle.bytes.WriteBytesMarshallable;

@Builder
@AllArgsConstructor
@ToString
public final class CoreSymbolSpecification implements WriteBytesMarshallable, StateHash {

	public final int symbolId;

	@NonNull
	public final SymbolType type;

	// currency pair specification
	public final int baseCurrency; // base currency
	public final int quoteCurrency; // quote/counter currency (OR futures contract currency)
	public final long baseScaleK; // base currency amount multiplier (lot size in base currency units)
	public final long quoteScaleK; // quote currency amount multiplier (step size in quote currency units)

	// fees per lot in quote? currency units
	public final long takerFee; // TODO check invariant: taker fee is not less than maker fee
	public final long makerFee;

	// margin settings (for type=FUTURES_CONTRACT only)
	public final long marginBuy; // buy margin (quote currency)
	public final long marginSell; // sell margin (quote currency)

	public CoreSymbolSpecification(BytesIn<?> bytes) {
		this.symbolId = bytes.readInt();
		this.type = SymbolType.of(bytes.readByte());
		this.baseCurrency = bytes.readInt();
		this.quoteCurrency = bytes.readInt();
		this.baseScaleK = bytes.readLong();
		this.quoteScaleK = bytes.readLong();
		this.takerFee = bytes.readLong();
		this.makerFee = bytes.readLong();
		this.marginBuy = bytes.readLong();
		this.marginSell = bytes.readLong();
	}

	/*
	 * NOT SUPPORTED YET:
	 * 
	 * // order book limits -- for FUTURES only // public final long highLimit; //
	 * public final long lowLimit;
	 * 
	 * // swaps -- not by // public final long longSwap; // public final long
	 * shortSwap;
	 * 
	 * // activity (inactive, active, expired)
	 * 
	 */

	@Override
	public void writeMarshallable(@SuppressWarnings("rawtypes") BytesOut bytes) {
		bytes.writeInt(symbolId);
		bytes.writeByte(type.getCode());
		bytes.writeInt(baseCurrency);
		bytes.writeInt(quoteCurrency);
		bytes.writeLong(baseScaleK);
		bytes.writeLong(quoteScaleK);
		bytes.writeLong(takerFee);
		bytes.writeLong(makerFee);
		bytes.writeLong(marginBuy);
		bytes.writeLong(marginSell);
	}

	@Override
	public int stateHash() {
		return Objects.hash(symbolId, type.getCode(), baseCurrency, quoteCurrency, baseScaleK, quoteScaleK, takerFee,
				makerFee, marginBuy, marginSell);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		CoreSymbolSpecification that = (CoreSymbolSpecification) o;
		return symbolId == that.symbolId && baseCurrency == that.baseCurrency && quoteCurrency == that.quoteCurrency
				&& baseScaleK == that.baseScaleK && quoteScaleK == that.quoteScaleK && takerFee == that.takerFee
				&& makerFee == that.makerFee && marginBuy == that.marginBuy && marginSell == that.marginSell
				&& type == that.type;
	}
}
