package com.loopers.application.ranking;

public record RankingCommand(
		String date,
		int page,
		int size,
		long start,
		long end
) {
	public static RankingCommand from(String date, int page, int size) {
		long start = (long) size * (page - 1);
		long end = start + size - 1;
		return new RankingCommand(date, page, size, start, end);
	}
}
