package com.traveler.board;

import com.traveler.board.service.BoardService;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

@SpringBootTest
class BoardApplicationTests {
    @Autowired
    private BoardService boardService;

    @Autowired
    private EntityManager em;

    private Statistics statistics;

    @BeforeEach
    void setUp() {
        //Statistics 객체 가져오기
        statistics = em.unwrap(Session.class).getSessionFactory().getStatistics();
        statistics.clear(); // 이전 테스트의 통계 기록 초기화

    }

//    @Test
//    void countQueriesWithoutBatchSizeTest() {
//        System.out.println("--- Fetch Join 적용 테스트 시작 ---");
//
//        long beforeQueryCount = statistics.getPrepareStatementCount();
//
//        boardService.listArticlesFetchJoin();
//
//        long afterQueryCount = statistics.getPrepareStatementCount();
//
//        System.out.println("실행된 쿼리 수: " + (afterQueryCount - beforeQueryCount));
//        System.out.println("------------------------------------");
//    }


    @Test
    void performanceComparisonTest() {
        // 1. StopWatch 객체 생성
        StopWatch stopWatch = new StopWatch("게시글 목록 조회 성능 비교");

        // 2. 시나리오 A 측정
        stopWatch.start("A: BatchSize 적용");
        boardService.listArticles();
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());
    }


}
