package io.mercury.redstone.engine.barrier;

import org.slf4j.Logger;

import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.redstone.core.order.ActualChildOrder;
import io.mercury.redstone.core.risk.OrderBarrier;

public final class HighFrequencyBarrier implements OrderBarrier<ActualChildOrder> {

	private static final Logger log = CommonLoggerFactory.getLogger(HighFrequencyBarrier.class);

	@Override
	public boolean filter(ActualChildOrder order) {
		switch (order.direction()) {
		case Long:
			
			return false;
		
		case Short:

			return false;

		default:
			log.error("");
			return false;
		}
	}
	
	public static void main(String[] args) {
		
		System.out.println(Integer.MAX_VALUE);
		
		
	}

}
