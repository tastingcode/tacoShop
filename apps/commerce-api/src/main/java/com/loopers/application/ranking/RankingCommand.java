package com.loopers.application.ranking;

public record RankingCommand(
		String date,
		Period period,
		int page,
		int size,
		long start,
		long end
) {
	public static RankingCommand from(String date, String period, int page, int size) {
		long start = (long) size * (page - 1);
		long end = start + size - 1;
		return new RankingCommand(date, Period.of(period), page, size, start, end);
	}

	public enum Period {
		DAILY, WEEKLY, MONTHLY;

		public static Period of(String period) {
			return Period.valueOf(period);
		}
	}
}
