package com.loopers.interfaces.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchScheduler {

	private final JobLauncher jobLauncher;
	private final Job weeklyRankingJob;
	private final Job monthlyRankingJob;

	/**
	 * 매주 월요일 오전 3시에 주간 랭킹 배치
	 */
	@Scheduled(cron = "0 0 3 * * MON")
	public void executeWeeklyRankingJob(){
		LocalDate endDate = LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1);
		LocalDate startDate = endDate.minusDays(6);

		try {
			JobParameters jobParameters = new JobParametersBuilder()
					.addString("startDate", startDate.toString())
					.addString("endDate", endDate.toString())
					.toJobParameters();

			jobLauncher.run(weeklyRankingJob, jobParameters);
		} catch (Exception e) {
			log.error("주간 랭킹 배치 실패", e);
		}

	}



}
