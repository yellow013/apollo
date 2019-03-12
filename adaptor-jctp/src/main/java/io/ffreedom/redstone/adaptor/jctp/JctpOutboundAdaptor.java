package io.ffreedom.redstone.adaptor.jctp;

import java.util.stream.Collectors;

import ctp.thostapi.CThostFtdcInputOrderActionField;
import ctp.thostapi.CThostFtdcInputOrderField;
import io.ffreedom.common.functional.Converter;
import io.ffreedom.jctp.JctpGateway;
import io.ffreedom.redstone.adaptor.jctp.converter.outbound.CtpOutboundCancelOrderConverter;
import io.ffreedom.redstone.adaptor.jctp.converter.outbound.CtpOutboundNewOrderConverter;
import io.ffreedom.redstone.adaptor.jctp.exception.OrderRefNotFoundException;
import io.ffreedom.redstone.adaptor.jctp.utils.JctpOrderRefKeeper;
import io.ffreedom.redstone.core.account.Account;
import io.ffreedom.redstone.core.adaptor.OutboundAdaptor;
import io.ffreedom.redstone.core.adaptor.dto.SubscribeMarketData;
import io.ffreedom.redstone.core.order.Order;

public class JctpOutboundAdaptor extends OutboundAdaptor {

	private Converter<Order, CThostFtdcInputOrderField> newOrderConverter = new CtpOutboundNewOrderConverter();

	private Converter<Order, CThostFtdcInputOrderActionField> cancelOrderConverter = new CtpOutboundCancelOrderConverter();

	private JctpGateway gateway;

	public JctpOutboundAdaptor(int adaptorId, String adaptorName, JctpGateway gateway) {
		super(adaptorId, adaptorName);
		this.gateway = gateway;
		init();
	}

	@Override
	public void init() {

	}

	@Override
	public boolean close() {
		return false;
	}

	@Override
	public boolean newOredr(Order order) {
		CThostFtdcInputOrderField ctpNewOrder = newOrderConverter.convert(order);
		JctpOrderRefKeeper.put(ctpNewOrder.getOrderRef(), order.getOrdSysId());
		gateway.newOrder(ctpNewOrder);
		return true;
	}

	@Override
	public boolean cancelOrder(Order order) {
		try {
			CThostFtdcInputOrderActionField ctpCancelOrder = cancelOrderConverter.convert(order);
			String orderRef = JctpOrderRefKeeper.getOrderRef(order.getOrdSysId());
			ctpCancelOrder.setOrderRef(orderRef);
			gateway.cancelOrder(ctpCancelOrder);
			return true;
		} catch (OrderRefNotFoundException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean subscribeMarketData(SubscribeMarketData subscribeMarketData) {
		try {
			gateway.subscribeMarketData(subscribeMarketData.getInstrumentSet().stream()
					.map(instrument -> instrument.getInstrumentCode()).collect(Collectors.toSet()));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean queryPositions(Account account) {
		return false;
	}

	@Override
	public boolean queryBalance(Account account) {
		return false;
	}

}
