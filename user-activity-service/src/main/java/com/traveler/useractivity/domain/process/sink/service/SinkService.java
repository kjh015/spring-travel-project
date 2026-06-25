package com.traveler.useractivity.domain.process.sink.service;

import com.traveler.useractivity.domain.process.core.message.LogPayload;
import com.traveler.useractivity.domain.process.sink.dto.event.SinkEvent;
import com.traveler.useractivity.domain.process.sink.dto.message.SinkMessage;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SinkService {

    private final ApplicationEventPublisher eventPublisher;

    private static final String FIELD_EVENT_ACTION = "event_action";
    private static final String FIELD_POST_ID = "게시판 번호";
    private static final String ACTION_VIEW = "view";

    public void sinkLog(LogPayload<Map<String, String>> logPayload) {
        if (logPayload.errorInfo() != null) {
            return;
        }

        Map<String, String> logData = logPayload.data();
        String eventAction = logData.get(FIELD_EVENT_ACTION);
        String postIdStr = logData.get(FIELD_POST_ID);

        if (ACTION_VIEW.equalsIgnoreCase(eventAction) && postIdStr != null && !"null".equalsIgnoreCase(postIdStr)) {
            Long postId = Long.valueOf(postIdStr);
            SinkMessage.PostViewedDTO message = new SinkMessage.PostViewedDTO(postId, logPayload.traceId());

            eventPublisher.publishEvent(new SinkEvent.PostViewed(message));
        }
    }
}
