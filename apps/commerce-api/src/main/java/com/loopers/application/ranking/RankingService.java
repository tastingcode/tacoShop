package com.loopers.application.ranking;

import com.loopers.domain.product.ProductDomainService;
import com.loopers.domain.ranking.MonthlyRanking;
import com.loopers.domain.ranking.RankingDomainService;
import com.loopers.domain.ranking.RankingRepository;
import com.loopers.domain.ranking.WeeklyRanking;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingService {

	private final RankingRepository rankingRepository;
	private final RankingDomainService rankingDomainService;
	private final ProductDomainService productDomainService;
	private final RankingAssembler rankingAssembler;

	/*public RankingListInfo getDailyRankings(RankingCommand command) {
		// 랭킹 키 조회
		String dailyKey = rankingDomainService.getDailyKey(command.date());

		// 랭킹 목록(상품 아이디) 조회
		List<Long> productIds = rankingRepository.getRankingProductIds(dailyKey, command.start(), command.end());

		// 상품 목록 Map 조회
		Map<Long, Product> productsMap = productDomainService.getProductsMap(productIds);

		// 랭킹 상품 조회
		List<RankingInfo> rankingInfos = rankingAssembler.getRankingProducts(productIds, productsMap, command.start());

		// 토탈 카운트 조회
		Long totalCount = rankingDomainService.getTotalCountBy(dailyKey);

		// 랭킹 목록 정보
		return RankingListInfo.from(rankingInfos, command.page(), command.size(), totalCount);
	}*/

	public RankingResult getDailyRankings(RankingCommand command) {

		String dailyKey = rankingDomainService.getDailyKey(command.date());

		Long totalCount = rankingDomainService.getTotalCountBy(dailyKey);

		List<Long> productIds = rankingRepository.getRankingProductIds(dailyKey, command.start(), command.end());

		return RankingResult.of(productIds, totalCount);
	}

	public RankingResult getWeeklyRankings(RankingCommand command) {
		LocalDate date = LocalDate.parse(command.date(), DateTimeFormatter.BASIC_ISO_DATE);
		LocalDate start = date.minusWeeks(1).with(DayOfWeek.MONDAY);
		LocalDate end = date.minusWeeks(1).with(DayOfWeek.SUNDAY);

		PageRequest pageRequest = PageRequest.of(command.page(), command.size());

		Page<WeeklyRanking> pageWeeklyRankingByDate = rankingRepository.findWeeklyRankingByDate(start, end, pageRequest);

		List<Long> productIds = pageWeeklyRankingByDate.getContent().stream()
				.map(WeeklyRanking::getProductId)
				.toList();

		long totalCount = pageWeeklyRankingByDate.getTotalElements();
		return RankingResult.of(productIds, totalCount);
	}

	public RankingResult getMonthlyRankings(RankingCommand command) {
		LocalDate date = LocalDate.parse(command.date(), DateTimeFormatter.BASIC_ISO_DATE);
		YearMonth lastMonth = YearMonth.from(date).minusMonths(1);

		PageRequest pageRequest = PageRequest.of(command.page(), command.size());
		Page<MonthlyRanking> pageMonthlyRankingByDate = rankingRepository.findMonthlyRankingByDate(lastMonth, pageRequest);

		List<Long> productIds = pageMonthlyRankingByDate.getContent().stream()
				.map(MonthlyRanking::getProductId)
				.toList();

		long totalCount = pageMonthlyRankingByDate.getTotalElements();
		return RankingResult.of(productIds, totalCount);
	}

}
