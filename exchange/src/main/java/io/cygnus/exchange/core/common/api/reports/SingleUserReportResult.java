package io.cygnus.exchange.core.common.api.reports;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.collections.api.map.primitive.MutableIntLongMap;
import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;
import org.slf4j.Logger;

import io.cygnus.exchange.core.common.Order;
import io.cygnus.exchange.core.common.enums.PositionDirection;
import io.cygnus.exchange.core.common.enums.UserStatus;
import io.cygnus.exchange.core.utils.SerializationUtils;
import io.mercury.common.log.CommonLoggerFactory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.openhft.chronicle.bytes.BytesIn;
import net.openhft.chronicle.bytes.BytesOut;
import net.openhft.chronicle.bytes.WriteBytesMarshallable;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@Getter
public final class SingleUserReportResult implements ReportResult {

	private static final Logger log = CommonLoggerFactory.getLogger(SingleUserReportResult.class);
	
	public static final SingleUserReportResult IDENTITY = new SingleUserReportResult(0L, null, null, null, null,
			QueryExecutionStatus.OK);

	private final long uid;

	// risk engine: user profile from
	// private final UserProfile userProfile;

	private final UserStatus userStatus;
	private final MutableIntLongMap accounts;
	private final IntObjectHashMap<Position> positions;

	// matching engine: orders placed by user
	// symbol -> orders
	private final IntObjectHashMap<List<Order>> orders;

	// status
	private final QueryExecutionStatus queryExecutionStatus;

	public static SingleUserReportResult createFromMatchingEngine(long uid, IntObjectHashMap<List<Order>> orders) {
		return new SingleUserReportResult(uid, null, null, null, orders, QueryExecutionStatus.OK);
	}

	public static SingleUserReportResult createFromRiskEngineFound(long uid, UserStatus userStatus,
			MutableIntLongMap accounts, IntObjectHashMap<Position> positions) {
		return new SingleUserReportResult(uid, userStatus, accounts, positions, null, QueryExecutionStatus.OK);
	}

	public static SingleUserReportResult createFromRiskEngineNotFound(long uid) {
		return new SingleUserReportResult(uid, null, null, null, null, QueryExecutionStatus.USER_NOT_FOUND);
	}

	public Map<Long, Order> fetchIndexedOrders() {
		return orders.stream().flatMap(Collection::stream).collect(Collectors.toMap(Order::getOrderId, ord -> ord));
	}

	private SingleUserReportResult(final BytesIn<?> bytesIn) {
		this.uid = bytesIn.readLong();
//        this.userProfile = bytesIn.readBoolean() ? new UserProfile(bytesIn) : null;
		this.userStatus = bytesIn.readBoolean() ? UserStatus.of(bytesIn.readByte()) : null;
		this.accounts = bytesIn.readBoolean() ? SerializationUtils.readIntLongHashMap(bytesIn) : null;
		this.positions = bytesIn.readBoolean() ? SerializationUtils.readIntHashMap(bytesIn, Position::new) : null;
		this.orders = bytesIn.readBoolean()
				? SerializationUtils.readIntHashMap(bytesIn, b -> SerializationUtils.readList(b, Order::new))
				: null;
		this.queryExecutionStatus = QueryExecutionStatus.of(bytesIn.readInt());
	}

	@Override
	public void writeMarshallable(@SuppressWarnings("rawtypes") final BytesOut bytes) {

		bytes.writeLong(uid);

//        bytes.writeBoolean(userProfile != null);
//        if (userProfile != null) {
//            userProfile.writeMarshallable(bytes);
//        }

		bytes.writeBoolean(userStatus != null);
		if (userStatus != null) {
			bytes.writeByte(userStatus.getCode());
		}

		bytes.writeBoolean(accounts != null);
		if (accounts != null) {
			SerializationUtils.marshallIntLongHashMap(accounts, bytes);
		}

		bytes.writeBoolean(positions != null);
		if (positions != null) {
			SerializationUtils.marshallIntHashMap(positions, bytes);
		}

		bytes.writeBoolean(orders != null);
		if (orders != null) {
			SerializationUtils.marshallIntHashMap(orders, bytes,
					symbolOrders -> SerializationUtils.marshallList(symbolOrders, bytes));
		}
		bytes.writeInt(queryExecutionStatus.code);

	}

	public enum QueryExecutionStatus {

		OK(0),

		USER_NOT_FOUND(1),

		;

		private final int code;

		QueryExecutionStatus(int code) {
			this.code = code;
		}

		public static QueryExecutionStatus of(int code) {
			switch (code) {
			case 0:
				return OK;
			case 1:
				return USER_NOT_FOUND;
			default:
				throw new IllegalArgumentException("unknown ExecutionStatus:" + code);
			}
		}
	}

	public static SingleUserReportResult merge(final Stream<BytesIn<?>> pieces) {

		log.info("Merge Stream...");

		return pieces.map(SingleUserReportResult::new).reduce(IDENTITY, (a, b) -> new SingleUserReportResult(a.uid,
//                                SerializationUtils.preferNotNull(a.userProfile, b.userProfile),
				SerializationUtils.preferNotNull(a.userStatus, b.userStatus),
				SerializationUtils.preferNotNull(a.accounts, b.accounts),
				SerializationUtils.preferNotNull(a.positions, b.positions),
				SerializationUtils.mergeOverride(a.orders, b.orders),
				a.queryExecutionStatus != QueryExecutionStatus.OK ? a.queryExecutionStatus : b.queryExecutionStatus));
	}

	@RequiredArgsConstructor
	@Getter
	public static class Position implements WriteBytesMarshallable {

		public final int quoteCurrency;
		// open positions state (for margin trades only)
		public final PositionDirection direction;
		public final long openVolume;
		public final long openPriceSum;
		public final long profit;

		// pending orders total size
		public final long pendingSellSize;
		public final long pendingBuySize;

		private Position(BytesIn<?> bytes) {
			this.quoteCurrency = bytes.readInt();
			this.direction = PositionDirection.of(bytes.readByte());
			this.openVolume = bytes.readLong();
			this.openPriceSum = bytes.readLong();
			this.profit = bytes.readLong();
			this.pendingSellSize = bytes.readLong();
			this.pendingBuySize = bytes.readLong();
		}

		@Override
		public void writeMarshallable(@SuppressWarnings("rawtypes") BytesOut bytes) {
			bytes.writeInt(quoteCurrency);
			bytes.writeByte((byte) direction.getMultiplier());
			bytes.writeLong(openVolume);
			bytes.writeLong(openPriceSum);
			bytes.writeLong(profit);
			bytes.writeLong(pendingSellSize);
			bytes.writeLong(pendingBuySize);
		}
	}

	@Override
	public String toString() {
		return "SingleUserReportResult{" + "userProfile=" + userStatus + ", accounts=" + accounts + ", orders=" + orders
				+ ", queryExecutionStatus=" + queryExecutionStatus + '}';
	}
}
