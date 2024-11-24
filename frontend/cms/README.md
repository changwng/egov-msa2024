# 전자정부 프레임워크 CMS

전자정부 프레임워크 기반의 콘텐츠 관리 시스템(CMS) 프론트엔드입니다.

## 기술 스택

- Next.js 14
- TypeScript
- Material-UI
- Redux Toolkit
- React Query
- Next-Auth

## 시작하기

1. 의존성 설치
```bash
npm install
# or
yarn install
```

2. 개발 서버 실행
```bash
npm run dev
# or
yarn dev
```

3. 빌드
```bash
npm run build
# or
yarn build
```

4. 프로덕션 서버 실행
```bash
npm run start
# or
yarn start
```

## 주요 기능

- 콘텐츠 관리
  - 콘텐츠 목록 조회
  - 콘텐츠 생성/수정/삭제
  - 엑셀 업로드
  - 검색 및 필터링
- 사용자 관리
- 권한 관리
- 설정

## 프로젝트 구조

```
src/
├── components/     # 재사용 가능한 컴포넌트
├── pages/         # Next.js 페이지
├── store/         # Redux 스토어
└── styles/        # 스타일 관련 파일
```

## 환경 설정

프로젝트 루트에 `.env.local` 파일을 생성하고 다음 환경 변수를 설정하세요:

```
NEXT_PUBLIC_API_URL=http://localhost:8080
```
