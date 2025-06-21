package com.traveler.realtimepopular.service;

import com.traveler.realtimepopular.dto.PopularScoreResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PopularService {
    private final AggregationService aggregationService;

    public List<PopularScoreResult> getTopPopularBoards(int topN) throws IOException {
        Map<String, Long> all = aggregationService.getPopularBoards(); // boardNo, 기존 집계 점수

        // 1. boardNo로 ratingAvg를 같이 조회해서 최종 점수 계산
        return all.entrySet().stream()
                .map(entry -> {
                    String boardNo = entry.getKey();
                    long baseScore = entry.getValue();
                    double ratingAvg = 0.0;
                    try {
                        ratingAvg = aggregationService.getBoardRatingAvg(boardNo);
                    } catch (Exception e) {
                        // 예외 발생 시 별점 0 처리
                    }
                    double totalScore = baseScore + ratingAvg * 4;

                    return PopularScoreResult.builder()
                            .boardId(boardNo)
                            .score((long) totalScore) // score를 long으로 맞출 때
                            .build();
                })
                .sorted((a, b) -> Long.compare(b.getScore(), a.getScore()))
                .limit(topN)
                .toList();
    }


    public List<PopularScoreResult> getTopPopularCategories(int topN) throws IOException {
        Map<String, Long> all = aggregationService.getPopularCategories();

        return all.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .limit(topN)
                .map(entry -> PopularScoreResult.builder()
                        .category(entry.getKey())
                        .score(entry.getValue())
                        .build()
                ).toList();
    }

    public List<PopularScoreResult> getTopPopularRegions(int topN) throws IOException {
        Map<String, Long> all = aggregationService.getPopularRegions();

        return all.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .limit(topN)
                .map(entry -> PopularScoreResult.builder()
                        .region(entry.getKey())
                        .score(entry.getValue())
                        .build()
                ).toList();
    }
}
