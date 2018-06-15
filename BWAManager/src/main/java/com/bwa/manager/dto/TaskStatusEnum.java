package com.bwa.manager.dto;

/**
 * The Enum TaskStatusEnum.
 */
public enum TaskStatusEnum {

	NOT_STARTED("NOT_STARTED"),

	STARTED("STARTED"),

	COMPLETED("COMPLETED"),

	FAILED("FAILED");

	private String value;

	private TaskStatusEnum(String value) {
		this.value = value;
	}

	/**
	 * From value.
	 *
	 * @param value the value
	 * @return the task status enum
	 */
	public static TaskStatusEnum fromValue(String value) {
		for (TaskStatusEnum category : values()) {
			if (category.value.equalsIgnoreCase(value)) {
				return category;
			}
		}
		return TaskStatusEnum.FAILED;
	}

	public String getValue() {
		return value;
	}
}
