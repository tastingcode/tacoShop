package com.loopers.infrastructure.metrics;

import com.loopers.domain.metrics.ProductMetrics;
import com.loopers.domain.metrics.ProductMetricsSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ProductMetricsJpaRepository extends JpaRepository<ProductMetrics, Long> {

    @Query("""
        SELECT new com.loopers.domain.metrics.ProductMetricsSummary(
            pm.productId,
            pm.date,
            SUM(pm.likesDelta),
            SUM(pm.salesDelta),
            SUM(pm.viewsDelta)
        )
        FROM ProductMetrics pm
        WHERE pm.date BETWEEN :startDate AND :endDate
        GROUP BY pm.productId, pm.date
    """)
    List<ProductMetricsSummary> findMetricsByDateBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

}
