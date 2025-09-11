package com.loopers.interfaces.api.ranking;

import com.loopers.application.ranking.RankingCommand;
import com.loopers.application.ranking.RankingInfo;
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
    public ApiResponse<List<RankingV1Dto.RankingResponse>> getRankingList(
            @RequestParam (required = false) String date,
            @PageableDefault(page = 1, size = 20) Pageable pageable
    ) {
		RankingCommand rankingCommand = RankingCommand.from(date, pageable.getPageNumber(), pageable.getPageSize());
		List<RankingInfo> rankingInfos = rankingService.getRankingList(rankingCommand);
		List<RankingV1Dto.RankingResponse> responses = RankingV1Dto.RankingResponse.from(rankingInfos);
        return ApiResponse.success(responses);
    }

}
