# Mock Stock - 모의 주식 투자 서비스

실시간 주식 데이터를 기반으로 한 모의 투자 서비스입니다.

## 기술 스택

### Backend
- **Framework**: Spring Boot 4.0.1
- **Language**: Java 21
- **Database**: MySQL
- **ORM**: Spring Data JPA + QueryDSL
- **Security**: Spring Security + JWT
- **External API**: Yahoo Finance API (WebFlux)

### Frontend
- **Framework**: React 18 + Vite
- **State Management**: TanStack Query (React Query)
- **Styling**: Tailwind CSS 4
- **UI Components**: Radix UI, shadcn/ui
- **Charts**: Recharts
- **HTTP Client**: Axios
- **Routing**: React Router DOM

## 프로젝트 구조

```
mock-stock/
├── backend/
│   └── src/main/java/com/study/mock_sock/
│       ├── config/                      # 설정
│       │   ├── SecurityConfig.java      # Spring Security
│       │   ├── WebClientConfig.java     # Yahoo API 호출
│       │   ├── WebMvcConfig.java        # SPA 라우팅 지원
│       │   ├── QuerydslConfig.java      # QueryDSL
│       │   └── DotEnvConfig.java        # 환경변수 (.env)
│       │
│       ├── common/
│       │   └── auth/
│       │       ├── JwtProvider.java     # JWT 생성/검증
│       │       └── JwtAuthenticationFilter.java
│       │
│       └── modules/
│           ├── users/                   # 사용자 모듈
│           │   ├── domains/
│           │   │   ├── User.java
│           │   │   └── Account.java
│           │   ├── controllers/
│           │   ├── services/
│           │   └── repositories/
│           │
│           └── stocks/                  # 주식 모듈
│               ├── domains/
│               │   ├── Stock.java
│               │   ├── Trade.java
│               │   ├── HoldingStock.java
│               │   └── StockPriceHistory.java
│               ├── controllers/
│               ├── services/
│               ├── repositories/
│               ├── scheduler/           # 가격 갱신 스케줄러
│               └── initializer/         # 초기 종목 데이터
│
├── frontend/
│   └── src/
│       ├── api/                         # API 클라이언트
│       │   ├── client.js                # Axios 인스턴스
│       │   ├── auth.js
│       │   ├── stocks.js
│       │   ├── trades.js
│       │   ├── portfolio.js
│       │   └── exchange.js
│       │
│       ├── hooks/                       # React Query 훅
│       │   ├── useLogin.js
│       │   ├── useSignUp.js
│       │   ├── useMe.js
│       │   ├── useStocks.js
│       │   ├── useTrades.js             # 무한스크롤 지원
│       │   ├── usePortfolio.js
│       │   └── useExchangeRate.js
│       │
│       ├── pages/                       # 페이지 컴포넌트
│       │   ├── HomePage.jsx
│       │   ├── LoginPage.jsx
│       │   ├── SignupPage.jsx
│       │   ├── StocksPage.jsx
│       │   ├── StockDetailPage.jsx      # 차트, 매수/매도
│       │   ├── PortfolioPage.jsx
│       │   ├── TradesPage.jsx           # 무한스크롤
│       │   └── MyPage.jsx
│       │
│       └── components/
│           ├── ui/                      # shadcn/ui 컴포넌트
│           ├── BottomAppBar.jsx
│           ├── LoadingSpinner.jsx
│           └── ProtectedRoute.jsx
│
├── scripts/
│   └── ec2-setup.sh                     # EC2 초기 설정
│
└── .github/
    └── workflows/
        └── deploy.yml                   # CI/CD 파이프라인
```

## ERD

```
┌──────────────┐       ┌──────────────┐
│     User     │       │   Account    │
├──────────────┤       ├──────────────┤
│ id (PK)      │───1:1─│ id (PK)      │
│ email        │       │ balance      │
│ password     │       │ user_id (FK) │
│ alias        │       └──────────────┘
└──────────────┘
       │
       ├────────────────────┐
       │ 1:N                │ 1:N
       ▼                    ▼
┌──────────────┐       ┌──────────────┐
│    Trade     │       │ HoldingStock │
├──────────────┤       ├──────────────┤
│ id (PK)      │       │ id (PK)      │
│ user_id (FK) │       │ user_id (FK) │
│ stock_id(FK) │       │ stock_id(FK) │
│ tradeType    │       │ quantity     │
│ quantity     │       │ avgPrice     │
│ price        │       └──────────────┘
│ tradeDate    │              ▲
└──────────────┘              │
       │                      │
       │ N:1                  │ N:1
       ▼                      │
┌──────────────┐──────────────┘
│    Stock     │
├──────────────┤
│ id (PK)      │
│ ticker       │
│ name         │
│ currency     │
│ currentPrice │
│ exchange     │
└──────────────┘
       │
       │ 1:N
       ▼
┌───────────────────┐
│ StockPriceHistory │
├───────────────────┤
│ id (PK)           │
│ stock_id (FK)     │
│ price             │
│ recordedAt        │
└───────────────────┘
```

## 주요 기능

### 1. 인증 (JWT)
- 회원가입 시 초기 잔액 10,000,000원 지급
- JWT 토큰 기반 인증 (유효시간: 1시간)

### 2. 실시간 주식 가격 갱신
- **Yahoo Finance API** 연동
- **1분 간격** 스케줄러로 모든 종목 가격 갱신
- WebFlux를 활용한 비동기 API 호출 (500ms 딜레이로 Rate Limit 방지)

### 3. 환율 서비스
- USD/KRW 환율 **1시간 간격** 자동 갱신
- 매수/매도 시 KRW로 환산하여 계좌 잔액 처리

### 4. 주식 거래
- **매수**: 계좌 잔액 차감, 보유 주식 추가/업데이트 (평균 단가 계산)
- **매도**: 계좌 잔액 증가, 보유 주식 차감 (전량 매도 시 삭제)

### 5. 차트 데이터 (가격 히스토리)
- 기간별 조회: 1시간, 1일, 1주, 1개월
- 동적 샘플링으로 약 14개 데이터 포인트 반환
- **매일 03시** 1개월 이상 된 히스토리 자동 삭제

### 6. 거래 내역 무한스크롤
- Slice 기반 페이지네이션
- IntersectionObserver를 활용한 무한스크롤

## API 엔드포인트

### 인증
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/sign-up` | 회원가입 |
| POST | `/api/auth/login` | 로그인 |
| GET | `/api/auth/me` | 내 정보 조회 |

### 주식
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/stocks` | 전체 종목 조회 |
| GET | `/api/stocks/search?keyword=` | 종목 검색 |
| GET | `/api/stocks/{stockId}/chart?range=1h` | 차트 데이터 (1h, 1d, 1w, 1m) |

### 거래
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/trades/buy` | 주식 매수 |
| POST | `/api/trades/sell` | 주식 매도 |
| GET | `/api/trades?page=0&size=20` | 거래 내역 (페이지네이션) |
| GET | `/api/portfolio` | 보유 주식 조회 |

### 환율
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/exchange` | 현재 환율 조회 |

## 스케줄러

| 스케줄러 | 주기 | 설명 |
|---------|------|------|
| `updateAllStockPrices` | 1분 | 모든 종목 실시간 가격 갱신 |
| `updateExchangeRate` | 1시간 | USD/KRW 환율 갱신 |
| `cleanupOldData` | 매일 03시 | 1개월 이상 된 가격 히스토리 삭제 |

## 로컬 실행 방법

### Backend
```bash
cd backend
./gradlew bootRun
```

### Frontend
```bash
cd frontend
npm install
npm run dev
```

## 환경 변수

### Backend (.env)
```
DB_URL=jdbc:mysql://localhost:3306/mock_stock
DB_USERNAME=root
DB_PASSWORD=password
```

### Frontend (.env.development)
```
VITE_API_URL=http://localhost:8080
```

## AWS EC2 배포

### 1. EC2 초기 설정

```bash
# EC2 인스턴스에서 실행
curl -O https://raw.githubusercontent.com/YOUR_REPO/main/scripts/ec2-setup.sh
chmod +x ec2-setup.sh
./ec2-setup.sh
```

### 2. GitHub Secrets 설정

Repository Settings > Secrets and variables > Actions에서 다음 설정:

| Secret | Description | Example |
|--------|-------------|---------|
| `EC2_HOST` | EC2 퍼블릭 IP 또는 도메인 | `12.34.56.78` |
| `EC2_USERNAME` | EC2 사용자명 | `ec2-user` 또는 `ubuntu` |
| `EC2_SSH_KEY` | EC2 SSH 개인키 (pem 파일 내용) | `-----BEGIN RSA...` |
| `DB_URL` | MySQL 접속 URL | `jdbc:mysql://rds-endpoint:3306/mock_stock` |
| `DB_USERNAME` | DB 사용자명 | `admin` |
| `DB_PASSWORD` | DB 비밀번호 | `your-password` |

### 3. 배포

`main` 브랜치에 push하면 자동 배포됩니다.

### 배포 구조

```
[GitHub Actions]
     │
     ├─ 1. Frontend 빌드 (npm run build)
     ├─ 2. Backend static 폴더에 복사
     ├─ 3. Backend JAR 빌드
     │
     ▼
[EC2 Instance]
     │
     └─ Spring Boot (8080)
          ├─ API 요청 처리
          └─ React 정적 파일 서빙
```

### 서비스 관리 명령어

```bash
# 서비스 시작
sudo systemctl start mock-stock

# 서비스 중지
sudo systemctl stop mock-stock

# 서비스 상태 확인
sudo systemctl status mock-stock

# 로그 확인
tail -f ~/app/app.log
```
