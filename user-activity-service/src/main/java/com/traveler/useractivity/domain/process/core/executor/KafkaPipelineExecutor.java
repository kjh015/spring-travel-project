package com.traveler.useractivity.domain.process.core.executor;

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
        }
    }
}
