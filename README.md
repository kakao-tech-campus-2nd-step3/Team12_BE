# Team12_BE

<p align="center"><a href="https://www.kakaotechcampus.com/">카카오 테크 캠퍼스</a> 2기 부산대 12조 프로젝트</p>

<p align="center">스터디 관리 플랫폼, 이제 경쟁을 곁들인 Ditto의 백엔드 서버입니다.</p>

## Team 럭키비키쿠키

<h3 align="center">Backend</h3>

<div align="center">

|                          조장                           |                          테크 리더                          |                           타임 키퍼                           |
|:-----------------------------------------------------:|:-------------------------------------------------------:|:---------------------------------------------------------:|
|           [신예준](https://github.com/pnuece)            |            [황수환](https://github.com/HSHwan)             |           [원윤서](https://github.com/yoonsu0325)            |
| <img src="https://github.com/pnuece.png" width="100"> | <img src="https://github.com/HSHwan.png" width="100"> | <img src="https://github.com/yoonsu0325.png" width="100"> |

</div>

<h3 align="center">Frontend</h3>

<div align="center">

|                           테크리더                           |                         리마인더                          |                            리액셔너                             |
|:--------------------------------------------------------:|:----------------------------------------------------------:|:-----------------------------------------------------------:|
|           [이세형](https://github.com/cla6shade)            |           [이민경](https://github.com/mingkyeongg)            |              [차기은](https://github.com/harugi7)              |
| <img src="https://github.com/cla6shade.png" width="100"> | <img src="https://github.com/mingkyeongg.png" width="100"> | <img src="https://github.com/harugi7.png" width="100"> |

</div>

## Introduction

---

### 문서

- **[API 문서]()**

### 깃허브 레포지토리

- **[FrontEnd Repository](https://github.com/kakao-tech-campus-2nd-step3/Team12_FE)**
- **[BackEnd Repository](https://github.com/kakao-tech-campus-2nd-step3/Team12_BE)**

## System Structure

### 전체 구성도

### 백엔드 구성도

### ERD(ER - Diagram)

<br />

## Tech Stack

<div align="center">

![java 21](https://img.shields.io/badge/Java%2021-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![spring boot 3.1.4](https://img.shields.io/badge/Spring%20boot%203.1.4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![spring security](https://img.shields.io/badge/Spring%20security-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![JPA](https://img.shields.io/badge/JPA-AE3002?style=for-the-badge&logoColor=white)
![LOMBOK](https://img.shields.io/badge/LOMBOK-FF1111?style=for-the-badge&logoColor=white)

![postgresql 13.16](https://img.shields.io/badge/PostgreSQL%2013.16-005C84?style=for-the-badge&logo=postgresql&logoColor=white)
![AWS S3](https://img.shields.io/badge/AWS%20S3-569A31?style=for-the-badge&logo=amazons3&logoColor=white)

![nginx](https://img.shields.io/badge/nginx-009639?style=for-the-badge&logo=nginx&logoColor=white)
![docker](https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

</div>

## How to Start

---

## FEATURES

### 인증

Sping Security + OAuth2 + JWT

- Spring Security와 OAuth2를 사용해서 카카오 소셜 회원가입 및 로그인을 구현하였습니다.
- 로그인 성공 시, JWT ACCESS TOKEN과 REFRESH TOKEN을 반환합니다.

### 스터디

특정 주제에 대해 함께 공부하려는 사람들의 모임입니다.

- 스터디를 생성하면, 다른 사용자들이 해당 스터디에 가입 신청을 할 수 있습니다.
- 스터디 가입 희망자는 가입 신청 기능을 이용하여 스터디에 가입 신청을 할 수 있고, 스터디장이 가입 신청 수락 여부를 결정할 수 있습니다.
- 스터디장은 스터디 초대 링크로 다른 사용자들을 초대할 수 있습니다.

### 스터디 랭킹

회원은 전체 스터디 랭킹을 통해 다른 스터디들의 목표 달성률과 출석률을 확인할 수 있습니다. 

- 목표 달성률과 출석률에 기반을 둔 지표를 기준으로 스터디 랭킹을 산정합니다.

### 공지

스터디 멤버들이 공지를 올릴 수 있습니다.

- 스터디별로 별도의 공지 게시판이 존재합니다.
- 스터디원은 공지를 등록, 수정 및 삭제할 수 있습니다.

### 과제

스터디 내에서 과제를 수행하여 목표를 달성합니다.

- 스터디장은 과제를 등록, 수정 및 삭제할 수 있습니다.
- 스터디원은 마감기한 전까지 과제를 제출할 수 있습니다.
- 스터디원은 다른 스터디원들이 올린 과제를 다운로드 받을 수 있습니다.

### 출석

스터디원들과 함께 공부할 날짜를 정하고, 해당 일자에 출석하여 기록을 남길 수 있습니다.

- 스터디장이 출석일자를 정할 수 있고, 출석 당일에 스터디원은 스터디장에게 전달받은 출석 코드로 출석할 수 있습니다.
- 만약 스터디원이 늦게 출석하여 출석일자에 출석 못했다면, 스터디장에게 요청하여 해당 일자의 출석 처리를 수정할 수 있습니다.

#### 파일 업로드

외부 저장소 `AWS S3`를 사용하였습니다.

- S3에 프로필 이미지와 과제 파일을 업로드하여 이후 재활용할 수 있습니다. 
- 파일의 사이즈는 5MB로 제한하였습니다.

### 알림
