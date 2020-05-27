package io.mercury.redstone.core.order.structure;

import io.mercury.common.sequence.Serial;
import io.mercury.financial.instrument.Instrument;
import io.mercury.redstone.core.order.enums.OrdStatus;
import io.mercury.redstone.core.order.enums.TrdAction;
import io.mercury.redstone.core.order.enums.TrdDirection;
import io.mercury.serialization.json.JsonUtil;

public final class OrdReport implements Serial<OrdReport> {

	private long serialId = System.nanoTime();

	/**
	 * mapping to order ordSysId
	 */
	private long ordSysId;

	/**
	 * report epoch milliseconds
	 */
	private long epochMillis;

	/**
	 * order status of now report
	 */
	private OrdStatus ordStatus;

	/**
	 * CTP orderRef
	 */
	private String orderRef;

	/**
	 * broker return id
	 */
	private String brokerUniqueId;

	/**
	 * instrument
	 */
	private Instrument instrument;

	/**
	 * direction
	 */
	private TrdDirection direction;

	/**
	 * action
	 */
	private TrdAction action;

	/**
	 * offer quantity
	 */
	private int offerQty;

	/**
	 * filled quantity
	 */
	private int filledQty;

	/**
	 * offer price
	 */
	private long offerPrice;

	/**
	 * order trade price
	 */
	private long tradePrice;

	/**
	 * offer time
	 */
	private String offerTime;

	/**
	 * last update time
	 */
	private String lastUpdateTime;

	public OrdReport(long ordSysId) {
		this.ordSysId = ordSysId;
	}

	public long getOrdSysId() {
		return ordSysId;
	}

	public long getEpochMillis() {
		return epochMillis;
	}

	public OrdStatus getOrdStatus() {
		return ordStatus;
	}

	public String getOrderRef() {
		return orderRef;
	}

	public String getBrokerUniqueId() {
		return brokerUniqueId;
	}

	public Instrument getInstrument() {
		return instrument;
	}

	public TrdDirection getDirection() {
		return direction;
	}

	public TrdAction getAction() {
		return action;
	}

	public int getOfferQty() {
		return offerQty;
	}

	public int getFilledQty() {
		return filledQty;
	}

	public long getOfferPrice() {
		return offerPrice;
	}

	public long getTradePrice() {
		return tradePrice;
	}

	public String getOfferTime() {
		return offerTime;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public OrdReport setEpochMillis(long epochMillis) {
		this.epochMillis = epochMillis;
		return this;
	}

	public OrdReport setOrdStatus(OrdStatus ordStatus) {
		this.ordStatus = ordStatus;
		return this;
	}

	public OrdReport setOrderRef(String orderRef) {
		this.orderRef = orderRef;
		return this;
	}

	public OrdReport setBrokerUniqueId(String brokerUniqueId) {
		this.brokerUniqueId = brokerUniqueId;
		return this;
	}

	public OrdReport setInstrument(Instrument instrument) {
		this.instrument = instrument;
		return this;
	}

	public OrdReport setDirection(TrdDirection direction) {
		this.direction = direction;
		return this;
	}

	public OrdReport setAction(TrdAction action) {
		this.action = action;
		return this;
	}

	public OrdReport setOfferQty(int offerQty) {
		this.offerQty = offerQty;
		return this;
	}

	public OrdReport setFilledQty(int filledQty) {
		this.filledQty = filledQty;
		return this;
	}

	public OrdReport setOfferPrice(long offerPrice) {
		this.offerPrice = offerPrice;
		return this;
	}

	public OrdReport setTradePrice(long tradePrice) {
		this.tradePrice = tradePrice;
		return this;
	}

	public OrdReport setOfferTime(String offerTime) {
		this.offerTime = offerTime;
		return this;
	}

	public OrdReport setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
		return this;
	}

	@Override
	public String toString() {
		return JsonUtil.toJson(this);
	}

	@Override
	public long serialId() {
		return serialId;
	}

}
