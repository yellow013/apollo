package io.ffreedom.redstone.adaptor.ctp;

import ctp.thostapi.CThostFtdcInputOrderActionField;
import ctp.thostapi.CThostFtdcInputOrderField;
import io.ffreedom.common.functional.Converter;
import io.ffreedom.common.param.ParamMap;
import io.ffreedom.jctp.Gateway;
import io.ffreedom.redstone.adaptor.base.AdaptorParams;
import io.ffreedom.redstone.adaptor.ctp.converter.outbound.CtpOutboundCancelOrderConverter;
import io.ffreedom.redstone.adaptor.ctp.converter.outbound.CtpOutboundNewOrderConverter;
import io.ffreedom.redstone.adaptor.ctp.dto.outbound.CtpOutboundCancelOrder;
import io.ffreedom.redstone.adaptor.ctp.dto.outbound.CtpOutboundNewOrder;
import io.ffreedom.redstone.core.account.Account;
import io.ffreedom.redstone.core.adaptor.OutboundAdaptor;
import io.ffreedom.redstone.core.adaptor.dto.QueryBalance;
import io.ffreedom.redstone.core.adaptor.dto.QueryPositions;
import io.ffreedom.redstone.core.adaptor.dto.ReplyBalance;
import io.ffreedom.redstone.core.adaptor.dto.ReplyPositions;
import io.ffreedom.redstone.core.adaptor.dto.SubscribeMarketData;
import io.ffreedom.redstone.core.order.Order;
import io.ffreedom.transport.rabbitmq.RabbitMqPublisher;
import io.ffreedom.transport.rabbitmq.config.RmqPublisherConfigurator;

public class CtpOutboundAdaptor implements OutboundAdaptor {

	private Converter<Order, CThostFtdcInputOrderField> newOrderConverter = new CtpOutboundNewOrderConverter();

	private Converter<Order, CThostFtdcInputOrderActionField> cancelOrderConverter = new CtpOutboundCancelOrderConverter();

	private Gateway gateway;

	public CtpOutboundAdaptor(Gateway gateway) {
		this.gateway = gateway;

		init();
	}

	@Override
	public void init() {

	}

	@Override
	public String getAdaptorName() {
		return "Ctp_Outbound_Adaptor";
	}

	@Override
	public int getAdaptorId() {
		return 0;
	}

	@Override
	public boolean close() {

		return false;
	}

	@Override
	public boolean newOredr(Order order) {
		CThostFtdcInputOrderField ctpNewOrder = newOrderConverter.convert(order);
		CtpOrderRefLogger.put(ctpNewOrder.getOrderRef(), order.getOrdSysId());
		gateway.newOrder(ctpNewOrder);
		return true;
	}

	@Override
	public boolean cancelOrder(Order order) {
		try {
			CThostFtdcInputOrderActionField ctpCancelOrder = cancelOrderConverter.convert(order);
			String orderRef = CtpOrderRefLogger.getOrderRef(order.getOrdSysId());
			ctpCancelOrder.setOrderRef(orderRef);
			gateway.cancelOrder(ctpCancelOrder);
			return true;
		} catch (CtpOrderRefNotFoundException e) {
			// TODO log
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean subscribeMarketData(SubscribeMarketData subscribeMarketData) {

		return false;
	}

	@Override
	public ReplyPositions queryPositions(Account account) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReplyBalance queryBalance(Account account) {
		// TODO Auto-generated method stub
		return null;
	}

}
