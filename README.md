<div align="center">
  <a href="https://impossible-scraper-904.notion.site/316dd40f05d7806dae8be53c02820534?v=316dd40f05d78054a92b000ce82d3127&source=copy_link" title="TripNow 리팩토링 일지">TripNow 리팩토링 일지</a>
</div>

# 서경 SW아카데미 25-1학기 프로젝트

로그 데이터 수집을 통한 고객 데이터 활용 및 분석

Trip Now: 실시간 인기 여행지 공유 웹사이트

## 프로젝트 개요

여러 사용자의 여행 경험을 공유받아 실시간으로 인기 있는 여행지를 보여주는 웹사이트입니다.

여행지 선택에 고민이 깊을 때 도움을 주는 것이 목적입니다.

## 주요 기능

- 실시간 사용자 로그 데이터 수집 및 정제 파이프라인
    - **로그 수집 → 포맷 처리 → 필터링 → 중복제거 → 모니터링** 프로세스 구축
- 실시간 인기 여행지 노출
    - 실시간 순위 및 점수 집계
- 여행지 공유 커뮤니티 게시판

## 시스템 구성도
<img width="738" height="834" alt="TripNow 시스템구성도" src="https://github.com/user-attachments/assets/fda865c3-299a-441a-a0cd-de319f2f5202" />

## 실시간 사용자 로그 데이터 수집 및 정제 파이프라인
<img width="1370" height="626" alt="실시간 사용자 로그 데이터 수집 및 정제 파이프라인" src="https://github.com/user-attachments/assets/3ab3cd84-8e16-4c50-b952-6776312d4e2c" />

## 사용 기술 및 도구

- **Frontend**: React, Nginx, JavaScript
- **Backend**: SpringBoot, Java, Eureka, OpenFeign
- **Database**: MySQL
- **Data Processing & Analytics**: Matomo, Fluentd, Kafka, Elasticsearch, Logstash, Kibana
- **DevOps**: Docker, KT Cloud, Jenkins, DockerHub, GitHub

## 진행 기간
2025년 4월 22일 ~ 2025년 6월 27일

## 역할
팀원 2명 중 백엔드 담당

- 백엔드 전체 개발
    - MSA 구조 설계, 게시판 및 회원 서비스 API 개발, 로그 수집 및 활용 프로세스 구현
- 프론트엔드 API 연동 구현
- Nginx 리버스 프록시 구성
    - 동적 요청은 WAS, 정적 요청은 Nginx가 처리하도록 구성
- CI/CD 및 배포 환경 구축
    - Github - Jenkins - Dockerhub - KT Cloud CI/CD 및 배포 환경 구축

## 시연 영상
https://github.com/user-attachments/assets/c0147cf6-ac75-4a2b-9989-0e6c101e64f6

https://github.com/user-attachments/assets/9e274ab7-baa6-444b-a9d6-590334c9a694

https://github.com/user-attachments/assets/62677e15-09bc-4875-beda-dcd534090273

https://github.com/user-attachments/assets/3151ec71-ab2f-4521-9814-075f17e4ac33


https://github.com/user-attachments/assets/85740613-ce1c-4dd7-b9c7-dffdc2f5380e

(README 초안)






