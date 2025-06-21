package com.traveler.board.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class ElasticIndexInitializer {

    private final ElasticsearchClient elasticsearchClient;

    public ElasticIndexInitializer(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    @PostConstruct
    public void createBoardIndexIfNotExists() throws Exception {
        String indexName = "board-test5";
        boolean exists = elasticsearchClient.indices().exists(r -> r.index(indexName)).value();
        if (exists) return;

        // 인덱스 설정 및 매핑을 문자열로 선언
        String mappingJson = """
{
  "settings": {
    "index.max_ngram_diff": 18,
    "analysis": {
      "char_filter": {
        "remove_spaces": {
          "type": "pattern_replace",
          "pattern": "\\\\s+",
          "replacement": ""
        }
      },
      "tokenizer": {
        "ngram_tokenizer": {
          "type": "ngram",
          "min_gram": 2,
          "max_gram": 20,
          "token_chars": ["letter", "digit"]
        },
        "edge_ngram_tokenizer": {
          "type": "edge_ngram",
          "min_gram": 1,
          "max_gram": 20,
          "token_chars": ["letter", "digit"]
        },
        "chosung_ngram_tokenizer": {
          "type": "ngram",
          "min_gram": 1,
          "max_gram": 10,
          "token_chars": ["letter"]
        }
      },
      "analyzer": {
        "korean_nori": {
          "type": "custom",
          "tokenizer": "nori_tokenizer"
        },
        "ngram_no_space_analyzer": {
          "type": "custom",
          "char_filter": ["remove_spaces"],
          "tokenizer": "ngram_tokenizer"
        },
        "edge_ngram_no_space_analyzer": {
          "type": "custom",
          "char_filter": ["remove_spaces"],
          "tokenizer": "edge_ngram_tokenizer"
        },
        "chosung_ngram_analyzer": {
          "type": "custom",
          "char_filter": ["remove_spaces"],
          "tokenizer": "chosung_ngram_tokenizer"
        }
      }
    }
  },
  "mappings": {
    "properties": {
      "id": { "type": "long" },
      "title": {
        "type": "text",
        "analyzer": "korean_nori",
        "fields": {
          "ngram": {
            "type": "text",
            "analyzer": "ngram_no_space_analyzer"
          },
          "autocomplete": {
            "type": "text",
            "analyzer": "edge_ngram_no_space_analyzer"
          }
        }
      },
      "titleChosung": {
        "type": "text",
        "analyzer": "chosung_ngram_analyzer"
      },
      "content":    { "type": "text", "analyzer": "korean_nori" },
      "memberId":   { "type": "long" },
      "travelPlace":{ "type": "text", "analyzer": "korean_nori" },
      "address":    { "type": "text", "analyzer": "korean_nori" },
      "category":   { "type": "text", "analyzer": "korean_nori" },
      "region":     { "type": "text", "analyzer": "korean_nori" },
      "regDate":    { "type": "date" },
      "modifiedDate": { "type": "date" },
      "ratingAvg":  { "type": "double" },
      "viewCount":  { "type": "long" },
      "commentCount": { "type": "long" },
      "favoriteCount": { "type": "long" }
    }
  }
}
""";



        // RestClient 이용해서 RAW JSON으로 인덱스 생성 (ElasticsearchClient는 복잡 매핑을 지원하지 않음)
        org.elasticsearch.client.Request request = new org.elasticsearch.client.Request("PUT", "/" + indexName);
        request.setJsonEntity(mappingJson);
        // Transport에서 RestClient 추출
        org.elasticsearch.client.RestClient restClient = ((co.elastic.clients.transport.rest_client.RestClientTransport) elasticsearchClient._transport()).restClient();
        restClient.performRequest(request);
    }
}
