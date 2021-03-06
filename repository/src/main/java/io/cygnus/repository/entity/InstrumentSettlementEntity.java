package io.cygnus.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.cygnus.repository.constant.ColumnDefinition;
import io.cygnus.repository.constant.CommonColumn;
import io.cygnus.repository.constant.EntityName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * InstrumentSettlement Entity
 * 
 * @author yellow013
 * 
 *
 */
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "cyg_instrument_settlement")
@Entity(name = EntityName.InstrumentSettlement)
public final class InstrumentSettlementEntity {

	@Id
	@Column(name = CommonColumn.UID)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long uid;

	/**
	 * instrumentCode
	 */
	@Column(name = CommonColumn.INSTRUMENT_CODE)
	private String instrumentCode;

	/**
	 * tradingDay
	 */
	@Column(name = CommonColumn.TRADING_DAY)
	private int tradingDay;

	/**
	 * lastPrice
	 */
	@Column(name = "last_price", columnDefinition = ColumnDefinition.DECIMAL_19_4)
	private double lastPrice;

	/**
	 * settlementPrice
	 */
	@Column(name = "settlement_price", columnDefinition = ColumnDefinition.DECIMAL_19_4)
	private double settlementPrice;

}
