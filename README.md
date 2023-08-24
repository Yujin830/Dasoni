<div align="center">
<a href="https://lab.ssafy.com/s09-webmobile1-sub2/S09P12A307"><img src="frontend/src/assets/image/logo.png"
  alt="다소니 로고"
/>
</a>
<p>☝로고를 클릭하면 UCC로 이동합니다</p>
</div>

<br>

# 프로젝트 소개

### 💗 프로젝트 개요

- N:N 랜덤 화상 미팅 / 매칭 서비스
- 서비스 명 : [다소니](https://i9a307.p.ssafy.io/)

<br>

### 📆 프로젝트 일정

- 2023.7.4 ~ 2023.8.18 ( 7주)

<br>

### ✨팀

| **강한(팀장**)                                    | **이명우**                                        | **권현우**                                                    | **김유진**                                        | **김선희**                                              | **민경현**                                        |
| ------------------------------------------------- | ------------------------------------------------- | ------------------------------------------------------------- | ------------------------------------------------- | ------------------------------------------------------- | ------------------------------------------------- |
| <p align="left" style="color:skyblue">Backend</p> | <p align="left" style="color:skyblue">Backend</p> | <p align="left" style="color:skyblue">Backend <br> DevOps</p> | <p align="left" style="color:skyblue">Backend</p> | <p align="left" style="color:pink">Frontend</p>         | <p align="left" style="color:pink">Frontend</p>   |
| WebSocket <br> 채팅,매칭,알람 서비스              | 매칭, 레이팅 알고리즘 <br> REST API               | CI/CD <br> 프로필 사진 API                                    |                                                   | WebSocket 대기방, 미팅 시스템 <br > WebRTC <br> UX / UI | 회원가입 및 로그인 <br> 모달 및 컴포넌트 <br> CSS |
| [@27kanghan](https://github.com/27kanghan)        | [@Fishphobiagg](https://github.com/Fishphobiagg)  | [@mycodeisnoob](https://github.com/mycodeisnoob)              | [@Yujin830](https://github.com/Yujin830)          | [@KimSeonHui](https://github.com/KimSeonHui)            | [@minaldo15](https://github.com/minaldo15)        |

<br><br>

### 🔧 기술 스택

### Back-end & Front-end

![Java](https://img.shields.io/badge/Java-yellow.svg?&style=for-the-badge&logo=java&logoColor=#3776AB)
![Spring](https://img.shields.io/badge/Spring-6DB33F.svg?&style=for-the-badge&logo=Spring&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F.svg?&style=for-the-badge&logo=Spring%20Boot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F.svg?&style=for-the-badge&logo=Spring%20Security&logoColor=white)

![React](https://img.shields.io/badge/React-61DAFB.svg?&style=for-the-badge&logo=React&logoColor=blue)
![TypeScript](https://img.shields.io/badge/TypeScript-3178C6.svg?&style=for-the-badge&logo=Typescript&logoColor=white)
![Redux](https://img.shields.io/badge/Redux-764ABC.svg?&style=for-the-badge&logo=Redux&logoColor=white)

### DataBase

![MySQL](https://img.shields.io/badge/MySQL-4479A1.svg?&style=for-the-badge&logo=MySQL&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D.svg?&style=for-the-badge&logo=Redis&logoColor=white)

### CI/CD & Infra Tools

![Jenkins](https://img.shields.io/badge/Jenkins-D24939.svg?&style=for-the-badge&logo=Jenkins&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED.svg?&style=for-the-badge&logo=Docker&logoColor=white)
![Nginx](https://img.shields.io/badge/Nginx-009639.svg?&style=for-the-badge&logo=Nginx&logoColor=white)

### Collaboration Tools

![Notion](https://img.shields.io/badge/Notion-000000.svg?&style=for-the-badge&logo=Notion&logoColor=로고색상)
![Mattermost](https://img.shields.io/badge/Mattermost-0058CC.svg?&style=for-the-badge&logo=Mattermost&logoColor=로고색상)

### Issue & Configuration Management

![GitLab](https://img.shields.io/badge/Gitlab-FC6D26.svg?&style=for-the-badge&logo=Gitlab&logoColor=#FC6D26)
![Jira](https://img.shields.io/badge/Jira-0052CC.svg?&style=for-the-badge&logo=Jira&logoColor=Blue)

---

<br><br>

# 목차

1. [**기획 배경**](#🎉기획-배경)
2. [**기능 소개**](#🔗기능-소개)
3. [**시연 영상**](#🎵시연-영상)
4. [**트러블 슈팅**](#⚽트러블-슈팅)
5. [**프로젝트 산출물**](#🎈프로젝트-산출물)

<br><br>

# 🎉기획 배경

## 배경

일반적으로 미팅/소개팅을 할 때는 상대방의 사진과 간단한 정보 등을 전달 받게 됩니다. 하지만 막상 실제로 만나보면 이전에 받은 사진과 실물이 달라 실망할 때 많습니다.

그래서 [다소니](https://i9a307.p.ssafy.io/)는 미리 주고받는 정보 없이 다양한 사람들과의 랜덤 미팅으로 실망감 대신 설레고 즐길 수 있는 미팅을 만들고자 했습니다.

<br>

## 의의

랜덤 미팅을 통해 3가지 기대를 할 수 있습니다.

1. 미팅 전 상대방에 대한 아무런 정보가 없어 선입견을 가지지 않게 됩니다.
2. 미팅을 진행하면서 얼굴, 직업, 나이 정보 등이 공개되어 정보가 공개될수록 다른 사람들과 소통 했을 때 호기심이 극대화 되고 설렘을 느낄 수 있습니다.
3. 미팅과 매칭 서비스를 통해 돈과 시간 소비하는 것을 줄이고 다양한 사람들과 만남을 가질 수 있습니다.

<br><br>

# 🔗기능 소개

## 시스템 아키텍쳐

![image](https://github.com/KimSeonHui/cs-study/assets/44824456/2193db0f-9258-4402-923e-6fd39588a487)

<br><br>

## 핵심 기능

### 실시간 매칭

![16 매칭 버튼 클릭](https://github.com/KimSeonHui/cs-study/assets/44824456/b5be85f1-b268-493a-a5ef-050046aa51b8)

- 빠른 매칭 버튼, 메기 매칭 버튼 클릭 시 매칭 중 모달창 등장
- 매칭 완료 되면 안내 문구 출력 후 미팅 페이지로 이동

<br>

### 다대다 화상 미팅

![8 미팅방페이지 - 미팅 시작](https://github.com/KimSeonHui/cs-study/assets/44824456/7f2a614c-a70d-4201-9554-9d604270d00a)

![9 미팅방페이지 - 참여자 화면](https://github.com/KimSeonHui/cs-study/assets/44824456/acd0aa76-f05a-4b9e-8504-3cf447ce69e2)

![10 미팅방페이지 - 첫인상 투표](https://github.com/KimSeonHui/cs-study/assets/44824456/5fcd7ecc-2a2c-4b8e-ad65-cb6870035ebd)

![11 미팅방페이지 - 정보 공개](https://github.com/KimSeonHui/cs-study/assets/44824456/cf9d84cb-503d-45b6-8c40-f22d0ae9f1b1)

![12 미팅방페이지 - 질문](https://github.com/KimSeonHui/cs-study/assets/44824456/1f860c18-9a89-4f0e-982a-189cbe54e1f4)

![13 미팅방페이지 - 최종 시그널](https://github.com/KimSeonHui/cs-study/assets/44824456/86b157d7-d72d-4d0d-afb6-dfec65662d45)

### 레이팅 시스템

- 시연 시나리오
- 로직

<br><br>

# 🎵시연 영상

## 핵심 기능

### 실시간 매칭

- (GIF)

### 다대다 화상 미팅

- (GIF)

### 레이팅 시스템

- (GIF)
- 로직

<br><br>

# ⚽트러블 슈팅

- [x] [SSE와 비동기 처리](https://github.com/YeryunJung)
- [x] [매칭 알고리즘 구조 개선](https://github.com/Fishphobiagg)
- [x] [리덕스 스토어 새로고침 상태유지](https://github.com/minaldo15)
- [x] [WebSocket과 STOMP, SSE 비동기 처리](https://velog.io/@27kanghan/WebSocket-STOMP-React%EB%A5%BC-%ED%99%9C%EC%9A%A9%ED%95%9C-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%A4%91-%ED%8A%B8%EB%9F%AC%EB%B8%94-%EC%8A%88%ED%8C%85)
- [x] [커스텀 훅으로 웹 소켓 연결 중복 코드 제거, 비동기 상태 설정으로 Stomp client 에러 해결](https://sunsunny.tistory.com/93)

<br><br>

# 🎈프로젝트 산출물

- [📃 요구사항 정의서](https://plump-sailor-daa.notion.site/4d2fb2d9fa3049e6a0689b2c40d1cd52?pvs=4)
- [🎨 와이어프레임](https://www.figma.com/file/lMRXAT3HPBflT8HtT5Z20T/%EC%8B%9C%EA%B7%B8%EB%8B%88%EC%97%98?type=design&node-id=102%3A567&mode=design&t=cq2dlHwKx8UW5FVs-1)
- [📕 API 명세서](https://plump-sailor-daa.notion.site/API-57a6d9c4a1f24f96bcd6625c08a53d73?pvs=4)
- [💿 ERD](/img/ERD.png)
