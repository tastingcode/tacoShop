package com.loopers.infrastructure.metrics;

import com.loopers.domain.metrics.ProductMetricsRepository;
import com.loopers.domain.metrics.ProductMetricsSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductMetricsRepositoryImpl implements ProductMetricsRepository {

    private final ProductMetricsJpaRepository productMetricsJpaRepository;

    @Override
    public List<ProductMetricsSummary> findMetricsByDateBetween(LocalDate startDate, LocalDate endDate) {
        return productMetricsJpaRepository.findMetricsByDateBetween(startDate, endDate);
    }

}
