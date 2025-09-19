package com.loopers.domain.ranking;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(
        name = "mv_product_rank_weekly",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"product_id", "week_start", "week_end"})
        }
)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class WeeklyRanking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rank;

	@Column(name = "product_id", nullable = false)
	private Long productId;

    private double score;

	@Column(name = "week_start", nullable = false)
	private LocalDate weekStart;

	@Column(name = "week_end", nullable = false)
	private LocalDate weekEnd;

    public static WeeklyRanking create(int rank, Long productId, double score, LocalDate weekStart, LocalDate weekEnd) {
        WeeklyRanking weeklyRanking = new WeeklyRanking();

        weeklyRanking.rank = rank;
        weeklyRanking.productId = productId;
        weeklyRanking.score = score;
        weeklyRanking.weekStart = weekStart;
        weeklyRanking.weekEnd = weekEnd;

        return weeklyRanking;
    }
}
