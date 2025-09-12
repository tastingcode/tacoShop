package com.loopers.interfaces.api.ranking;

import com.loopers.application.ranking.RankingCommand;
import com.loopers.application.ranking.RankingListInfo;
import com.loopers.application.ranking.RankingService;
import com.loopers.interfaces.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/rankings")
public class RankingController implements RankingV1ApiSpec {

	private final RankingService rankingService;

    @GetMapping("")
    @Override
    public ApiResponse<RankingV1Dto.RankingListResponse> getRankingList(
            @RequestParam (required = false) String date,
            @PageableDefault(page = 1, size = 20) Pageable pageable
    ) {
		RankingCommand rankingCommand = RankingCommand.from(date, pageable.getPageNumber(), pageable.getPageSize());
		RankingListInfo rankingListInfo = rankingService.getRankingList(rankingCommand);
		RankingV1Dto.RankingListResponse response = RankingV1Dto.RankingListResponse.from(rankingListInfo);
		return ApiResponse.success(response);
    }

}
