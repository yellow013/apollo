/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package io.mercury.simulator.persistence.avro.entity;

@org.apache.avro.specific.AvroGenerated
public enum PackTitle implements org.apache.avro.generic.GenericEnumSymbol<PackTitle> {
	MarketDataSubscribe, MarketDataLevel1, Order, Next;

	public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse(
			"{\"type\":\"enum\",\"name\":\"PackTitle\",\"namespace\":\"io.mercury.simulator.persistence.avro.entity\",\"symbols\":[\"MarketDataSubscribe\",\"MarketDataLevel1\",\"Order\",\"Next\"]}");

	public static org.apache.avro.Schema getClassSchema() {
		return SCHEMA$;
	}

	public org.apache.avro.Schema getSchema() {
		return SCHEMA$;
	}
}