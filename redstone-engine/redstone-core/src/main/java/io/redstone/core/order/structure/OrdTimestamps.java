package io.redstone.core.order.structure;

import javax.annotation.Nullable;

import io.mercury.common.datetime.EpochTimestamp;

public final class OrdTimestamps {

	private EpochTimestamp generateTime;
	private EpochTimestamp sendingTime;
	private EpochTimestamp firstReportTime;
	private EpochTimestamp finishTime;

	private OrdTimestamps() {
		this.generateTime = EpochTimestamp.now();
	}

	public static OrdTimestamps generate() {
		return new OrdTimestamps();
	}

	public EpochTimestamp generateTime() {
		return generateTime;
	}

	@Nullable
	public EpochTimestamp sendingTime() {
		return sendingTime;
	}

	@Nullable
	public EpochTimestamp firstReportTime() {
		return firstReportTime;
	}

	@Nullable
	public EpochTimestamp finishTime() {
		return finishTime;
	}

	public OrdTimestamps fillSendingTime() {
		this.sendingTime = EpochTimestamp.now();
		return this;
	}

	public OrdTimestamps fillFirstReportTime() {
		this.firstReportTime = EpochTimestamp.now();
		return this;
	}

	public OrdTimestamps fillFinishTime() {
		this.finishTime = EpochTimestamp.now();
		return this;
	}

}
