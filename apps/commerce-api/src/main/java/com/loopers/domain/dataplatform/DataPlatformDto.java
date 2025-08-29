package com.loopers.domain.dataplatform;

public class DataPlatformDto {
	public record Result(
			DataResultStatus status,
			Long userId,
			String message
	) {
		public static Result success(Long userId, String message) {
			return new Result(DataResultStatus.SUCCESS, userId, message);
		}

		public static Result fail(Long userId, String message) {
			return new Result(DataResultStatus.FAILURE, userId, message);
		}
	}
}
