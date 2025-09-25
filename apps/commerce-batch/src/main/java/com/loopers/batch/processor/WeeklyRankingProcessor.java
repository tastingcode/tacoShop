package com.loopers.batch.processor;

import com.loopers.domain.metrics.ProductMetricsSummary;
import com.loopers.domain.ranking.RankedProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WeeklyRankingProcessor implements ItemProcessor<ProductMetricsSummary, RankedProduct> {

	private final Double LIKE_WEIGHT = 0.1;
	private final Double SALES_WEIGHT = 1.0;
	private final Double VIEWED_WEIGHT = 0.05;

    @Override
    public RankedProduct process(ProductMetricsSummary summary) {
        double score = LIKE_WEIGHT * summary.getLikeCount()
                + SALES_WEIGHT * summary.getStockCount()
                + VIEWED_WEIGHT * summary.getViewCount();

        return new RankedProduct(summary.getProductId(), score);
    }

}
