package com.loopers.domain.dataplatform;

public class DataPlatformDto {
	public record Result<T>(
			DataResultStatus status,
			T userId,
			String message
	) {
		public static <T>Result<T> success(T userId, String message) {
			return new Result(DataResultStatus.SUCCESS, userId, message);
		}

		public static <T>Result<T> fail(T userId, String message) {
			return new Result(DataResultStatus.FAILURE, userId, message);
		}
	}
}
