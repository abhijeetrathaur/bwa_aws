package com.bwa.manager.dto;

/**
 * The Enum WorkerStatusEnum.
 */
public enum WorkerStatusEnum {

	AVAILABLE("AVAILABLE"),

	UNAVAILABLE("UNAVAILABLE");

	private String value;

	private WorkerStatusEnum(String value) {
		this.value = value;
	}

	/**
	 * From value.
	 *
	 * @param value the value
	 * @return the worker status enum
	 */
	public static WorkerStatusEnum fromValue(String value) {
		for (WorkerStatusEnum category : values()) {
			if (category.value.equalsIgnoreCase(value)) {
				return category;
			}
		}
		return WorkerStatusEnum.UNAVAILABLE;
	}

	public String getValue() {
		return value;
	}
}
