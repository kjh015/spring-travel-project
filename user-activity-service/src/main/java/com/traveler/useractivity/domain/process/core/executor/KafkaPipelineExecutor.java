package com.traveler.useractivity.domain.process.core.executor;

import com.traveler.useractivity.domain.process.core.logging.ProcessLogContext;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaPipelineExecutor {

    /**
     * 카프카 리스너들의 비동기 흐름 및 Ack/예외 처리를 공통 전담하는 템플릿 실행기
     */
    public CompletableFuture<Void> execute(Acknowledgment ack, CheckedSupplier<CompletableFuture<Void>> pipelineFlow) {
        try {
            return pipelineFlow
                    .get()
                    .thenAccept(result -> ack.acknowledge())
                    .exceptionallyCompose(ex ->
                            CompletableFuture.failedFuture((Exception) NestedExceptionUtils.getMostSpecificCause(ex)));
        } catch (Exception e) {
            return CompletableFuture.failedFuture((Exception) NestedExceptionUtils.getMostSpecificCause(e));
        } finally {
            // 리스너 스레드는 재사용되므로 MDC 식별자가 다음 메시지로 새지 않도록 정리
            ProcessLogContext.clear();
        }
    }
}
