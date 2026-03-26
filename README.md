# TacoShop

이커머스 도메인을 주제로 설계한 멀티모듈 Spring Boot 프로젝트입니다.  
상품, 주문, 결제, 포인트, 쿠폰, 랭킹과 같은 핵심 흐름을 중심으로, 거래성 서비스에서 필요한 정합성, 비동기 이벤트 처리, 조회 성능 개선, 배치 집계 구조를 함께 다룹니다.

이 프로젝트는 단순 CRUD 구현보다 아래와 같은 주제를 직접 설계하고 실험하는 데 초점을 두고 있습니다.

- 도메인 중심 설계와 계층별 책임 분리
- 주문/결제 흐름에서의 정합성 관리
- Kafka 기반 비동기 이벤트 처리
- Redis를 활용한 조회 성능 및 랭킹 구조 설계
- Batch 기반 주간/월간 집계 처리
- 로깅, 메트릭, 모니터링을 고려한 운영 관점의 구성

## Project Goals

- 거래성 서비스의 핵심 도메인을 안정적으로 모델링한다.
- 실시간 요청 처리와 후행 작업을 분리해 시스템 복잡도를 관리한다.
- 캐시, 메시징, 배치 처리를 조합해 확장 가능한 구조를 검증한다.
- 개발 편의성뿐 아니라 운영 가능성까지 고려한 프로젝트 구조를 만든다.

## Architecture

저장소는 `apps`, `modules`, `supports` 세 계층으로 구성되어 있습니다.

```text
Root
├── apps
│   ├── commerce-api
│   ├── commerce-streamer
│   ├── commerce-batch
│   └── pg-simulator
├── modules
│   ├── jpa
│   ├── redis
│   └── kafka
└── supports
    ├── jackson
    ├── logging
    └── monitoring
```

### Apps

- `commerce-api`
  - 상품, 주문, 결제, 포인트, 쿠폰, 랭킹, 사용자 관련 API를 제공하는 메인 애플리케이션
- `commerce-streamer`
  - Kafka consumer 기반 이벤트 처리 애플리케이션
  - 감사 로그 적재, 상품 메트릭 집계 등 후행 작업 담당
- `commerce-batch`
  - 주간/월간 랭킹 집계를 수행하는 Batch 애플리케이션
- `pg-simulator`
  - 외부 결제대행사(PG) 연동을 가정한 시뮬레이터
  - 로컬 개발 환경에서 결제 플로우를 검증하기 위한 앱

### Modules

- `jpa`
  - JPA, Querydsl, MySQL 연동 관련 공통 설정
- `redis`
  - Redis 연동 관련 공통 설정
- `kafka`
  - Kafka producer/consumer 연동 관련 공통 설정

### Supports

- `jackson`
  - 직렬화/역직렬화 관련 공통 설정
- `logging`
  - 로깅 및 추적 관련 설정
- `monitoring`
  - Micrometer, Prometheus 기반 모니터링 설정

## Tech Stack

- Language: Java 21, Kotlin
- Framework: Spring Boot, Spring Batch
- Data: MySQL, Redis
- Messaging: Kafka
- ORM: Spring Data JPA, Querydsl
- Observability: Micrometer, Prometheus, Grafana
- Test / Load Test: JUnit 5, Testcontainers, k6
- Infra / DevOps: Docker Compose, Gradle

## Key Features

- 상품 조회 및 좋아요 기능
- 주문 생성 및 주문 상품 관리
- 결제 요청 및 결제 상태 처리
- 포인트 및 쿠폰 사용 흐름 관리
- Redis 기반 랭킹 조회
- Kafka 기반 이벤트 발행 및 소비
- 상품 메트릭 집계 및 감사 로그 적재
- Batch 기반 주간/월간 랭킹 산출
- 외부 PG 연동을 가정한 결제 시뮬레이션

## What I Tried To Learn

이 프로젝트에서는 기능 구현 자체보다, 아래 질문에 대한 답을 찾는 데 더 집중했습니다.

- 주문과 결제처럼 실패 비용이 큰 흐름을 어떻게 분리하고 관리할 것인가
- 어떤 작업을 동기 처리로 두고, 어떤 작업을 이벤트 기반 비동기로 분리할 것인가
- 조회 성능 개선을 위해 Redis와 집계 구조를 어떻게 활용할 것인가
- 실시간 처리와 Batch 처리의 경계를 어떻게 나눌 것인가
- 장애 상황에서 추적 가능하도록 로그와 메트릭을 어떻게 남길 것인가
