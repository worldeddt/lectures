# lectures

수강 신청 api

----
## 구성
java 

jdk 17

spring boot 3.4.1

maria database

redis

---
## 데이터 구조

![img.png](src/main/java/api/lectures/images/entities.png)

수강생과 강사를 구매자와 판매자 개념으로 엔티티를 나누고

강연장과 강의 정보를 상품 정보로 설정하듯 구성합니다.

마지막으로 신청완료된 정보들을 예약 목록 처럼 구성하기 위애 엔티티를 별도로 나누었습니다.

---
## 기타

대량의 데이터가 들어올 것을 대비(수강 신청이 특정 시간에 몰리거나 기업 규모가 클 경우)하여

효율적인 대비를 위해서 r2dbc 와 reactive redis 를 기반으로 한

비동기적 처리 로직으로 구성하였습니다. 

이 과정에서 non-blocking 기반의 reactive stream 표준 스펙으로 

reactor 를 사용하게 되었습니다.




