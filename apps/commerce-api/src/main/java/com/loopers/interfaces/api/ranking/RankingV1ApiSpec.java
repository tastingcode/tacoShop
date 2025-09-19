package com.loopers.interfaces.api.ranking;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Tag(name = "Ranking V1 API", description = "랭킹 API 입니다.")
public interface RankingV1ApiSpec {

	@Operation(summary = "랭킹 목록 조회")
	ApiResponse<RankingV1Dto.RankingListResponse> getRankingList(@RequestParam String date,
																 @RequestParam String period,
																 @PageableDefault(page = 1, size = 20) Pageable pageable);

}
