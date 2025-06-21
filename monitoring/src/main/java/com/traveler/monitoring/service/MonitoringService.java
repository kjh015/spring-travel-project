    package com.traveler.monitoring.service;

    import co.elastic.clients.elasticsearch.ElasticsearchClient;
    import co.elastic.clients.elasticsearch._types.SortOrder;
    import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
    import co.elastic.clients.elasticsearch._types.aggregations.CalendarInterval;
    import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
    import co.elastic.clients.elasticsearch.core.SearchResponse;
    import co.elastic.clients.util.NamedValue;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;

    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    @Service
    @RequiredArgsConstructor
    public class MonitoringService {
        private final ElasticsearchClient esClient;

        public List<Map<String, Object>> getTop(String data) throws IOException {

            SearchResponse<Void> response = esClient.search(s -> s
                            .index("logstash-*")
                            .size(0)
                            .query(q -> q
                                    .bool(b -> b
                                            .must(m -> m.term(t -> t.field("success").value(true)))
                                    )
                            )
                            .aggregations("get_top", a -> a
                                    .terms(t -> t
                                            .field(data + ".keyword")
                                            .size(5)
                                            .order(List.of(NamedValue.of("_count", SortOrder.Desc)))

                                    )
                            ),
                    Void.class
            );
            List<Map<String, Object>> result = new ArrayList<>();

            Aggregate agg = response.aggregations().get("get_top");
            if (agg.isSterms()) {
                for (StringTermsBucket bucket : agg.sterms().buckets().array()) {
                    Map<String, Object> res = new HashMap<>();
                    res.put(data, bucket.key().stringValue());
                    res.put("count", bucket.docCount());
                    result.add(res);
                }
            }

            return result;
        }
        public List<Map<String, Object>> getPageVisitStats(String period) throws IOException {
            CalendarInterval interval;
            switch (period) {
                case "hour": interval = CalendarInterval.Hour; break;
                case "day": interval = CalendarInterval.Day; break;
                case "month": interval = CalendarInterval.Month; break;
                default: interval = CalendarInterval.Day;
            }

            SearchResponse<Void> response = esClient.search(s -> s
                            .index("logstash-*")
                            .size(0)
                            .query(q -> q
                                    .bool(b -> b
                                            .must(m -> m.term(t -> t.field("success").value(true)))
                                    )
                            )
                            .aggregations("visits_by_period", a -> a
                                    .dateHistogram(h -> h
                                            .field("방문 시간")
                                            .calendarInterval(interval)
                                            .format("yyyy-MM-dd HH:mm")
                                    )
                            ),
                    Void.class
            );

            Aggregate agg = response.aggregations().get("visits_by_period");
            return agg.isDateHistogram() ?
                    agg.dateHistogram().buckets().array().stream()
                            .map(bucket -> {
                                Map<String, Object> map = new HashMap<>();
                                map.put("period", bucket.keyAsString());
                                map.put("count", bucket.docCount());
                                return map;
                            })
                            .toList()
                    : List.of();


        }
    }



