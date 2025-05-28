# Vibe Board

바이브  코딩으로 만드는 게시판. 

## 생성 프롬프트

### ✅ Kotlin + Spring Boot 기반 게시판 백엔드 개발 프롬프트

> Kotlin 언어와 Spring Boot 프레임워크를 사용해 다음과 같은 기능의 게시판 백엔드 시스템을 Vibe 코딩 방식으로 구현해주세요.  
> SOLID 원칙을 반드시 준수하고, 구조적인 일관성과 유지보수성을 고려한 설계를 적용해주세요.

---

### 🛠️ 기술 스택

- **Language**: Kotlin
- **Framework**: Spring Boot
- **DB**: H2 (개발 환경용)
- **ORM**: Spring Data JPA
- **Build Tool**: Gradle
- **인증 방식**: Spring Security + JWT

---

### ✅ 기능 명세

#### 1. **회원 기능**

- 회원가입 (중복 체크 및 유효성 검사 포함)
- 로그인 (JWT 기반 인증 및 토큰 발급)
- 회원정보 조회
- 회원정보 수정 (비밀번호 변경 포함)

#### 2. **게시판 기능**

- 게시물 목록 조회 (페이징 기능 포함)
- 게시물 상세 조회
- 게시물 작성
- 게시물 수정 (작성자만 가능하도록 권한 체크)

---

### ✅ 설계 및 개발 가이드

#### 🔐 SOLID 원칙 준수

- **SRP (단일 책임 원칙)**: 각 클래스/모듈은 하나의 책임만 갖도록 분리
- **OCP (개방-폐쇄 원칙)**: 기능 확장이 가능하고, 기존 코드를 수정하지 않도록 설계
- **LSP (리스코프 치환 원칙)**: 상속 구조 시 일관성 유지
- **ISP (인터페이스 분리 원칙)**: 필요한 인터페이스만 구현
- **DIP (의존 역전 원칙)**: 상위 모듈이 하위 모듈에 의존하지 않도록 의존성 주입 적용

#### 📦 계층 구조

- **Controller**: HTTP 요청 수신, DTO 변환 담당
- **Service**: 비즈니스 로직 처리
- **Repository**: 데이터베이스 접근 (Spring Data JPA)
- **Domain/Entity**: 도메인 모델, 엔티티 정의
- **DTO/Request/Response**: API 통신용 데이터 구조 정의

#### 🧠 Vibe 코딩 시 주의사항

- 빠르게 작동하는 최소 기능부터 구현하되, **리팩토링 포인트는 명시적으로 주석으로 남길 것**
- **임시 구현이라도 구조적으로 나중에 SOLID 원칙을 적용하기 쉽게 짜야 함**
- DTO → Entity, Entity → DTO 간 변환은 명확한 책임자가 있도록 설계
- 테스트 가능한 구조 유지 (예: 서비스 레이어에 비즈니스 로직 집중)

#### 🧪 테스트 (선택 사항)

- 가능하면 Service 계층에 대한 단위 테스트 예시 포함

---

### 💡 추가 조건

- 모든 API는 JSON 형식으로 통신
- 에러 응답은 **일관된 구조로 설계** (에러 코드, 메시지, 상세정보 포함)
- 인증이 필요한 API에는 JWT 토큰을 헤더로 전달
- Swagger 또는 REST Docs는 선택 (필요시 명시)

---

### Reference Documentation

For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.0/gradle-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.5.0/gradle-plugin/packaging-oci-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.5.0/reference/web/servlet.html)

### Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

### Additional Links

These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

