# 🐶 스파르타 클럽 내일배움 캠프 Plus Project
> 반려동물 용품 가격비교 웹 어플리케이션

------

## 🦴프로젝트 소개
이 프로젝트는 반려동물 용품의 최저가를 알아볼 수 있는 **가격비교 어플리케이션** 입니다.
> - 로그인 한 유저만 사용할 수 있습니다.  
> - 사용자의 권한에 따라 접근할 수 없는 컨트롤러가 존재합니다.
> - 모든 사용자는 상품에 후기를 달 수 있으며, 해당 후기에는 좋아요를 남길 수 있습니다.

> - 자주 조회되거나, 실시간 집계가 필요한 기능에 캐싱(Caching)을 적용하였습니다.

---

## ✨주요 기능

- **회원가입**
  > - 새로운 사용자가 회원가입을 진행할 수 있음.
- **JWT 인증 사용**
  > - 로그인/로그아웃 및 로그인 필터 기능 구현.
- **Spring Security**
  > - Spring Security를 이용하여 사용자 정보를 보다 안전하게 사용함.

- **Product CRUD**
  > - Role이 ADMIN인 사용자만 등록, 수정, 삭제가 가능하며, Cache 메모리에 상품에 달린 리뷰 수를 저장함.

- **Category CRUD**
  > - Role이 ADMIN인 사용자만 등록, 수정, 삭제가 가능하며, 전체 카테고리, 특정 카테고리는 물론 특정 카테고리에 속한 상품들도 같이 조회할 수 있음.

- **Review CRUD**
  > - 일반 사용자가 등록, 수정, 삭제 할 수 있으며, Cache 메모리에 리뷰 별 좋아요 개수, 별점 평균을 저장함.
- **Like Toggle**
  > - 후기에 좋아요를 등록, 삭제 할 수 있는 토글 메뉴.

- **Search**
  >- 검색 키워드, 열람한 페이지와 페이지 객체의 사이즈를 Cache 메모리에 저장함.

---

## 🪄기술 스택
<img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white">
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white">
<img src="https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens">
<img src="https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white">
<img src="https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white">
<img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white"> 

---

## 📖ERD

<img width="1322" height="802" alt="image" src="https://github.com/user-attachments/assets/56281801-d3b7-487b-aea7-d4f328d155b9" />

---

## 📄API 명세

### 공통 응답
```Http
HTTP/1.1 {code}
Content-Type: application/json;
```
```JSON
{
    "success": true,           
    "message": "성공 메시지",   
    "data": {},               
    "timestamp": "2024-03-21T10:00:00Z"
}
```

| Parameter | Type            | Description                      |
| :-------- |:----------------| :-------------------------       |
| success   | `boolean`       | **Required**. 요청 성공 여부      |
| message   | `String`        | **Required**. 성공/실패 메시지    |
| data      | `Object`        | Response Data (Null 가능)        |
| timestamp | `LocalDateTime` | **Required**.      |

---

## Auth

| 기능    | method  | Domain | EndPoint |
|:------| :---    | :----  | :----  |
| 로그인   | `POST`| `auth`| /api/auth/login |
| 회원 가입 | `POST`| `auth`| /api/auth/signup|
| 로그아웃  | `POST`| `auth`| /api/auth/logout|
| 계정 삭제 | `DELETE`| `auth`| /api/auth/me|

<details>
<summary> 로그인 </summary>
  
#### Request
```Http
POST /api/auth/login
```
```JSON
{
  "email": "test@example.com",
  "password": "password123!"
}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "로그인에 성공했습니다.",
  "data": {
    "accessToken": "eyJhbG..."
  },
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>

<details>
<summary> 회원가입 </summary>

#### Request
```Http
POST /api/auth/signup
```
```JSON
{
  "email": "test@example.com",
  "password": "password123!",
  "nickname": "홍길동"
}
```

#### Response
```Http
HTTP/1.1 201
Content-Type: application/json;
```
```JSON

{
  "success": true,
  "message": "회원가입이 완료되었습니다.",
  "data": {
    "id": 1,
    "email": "test@example.com",
    "nickname": "이지호",
    "role": "ROLE_USER",
    "createdAt": "2025-09-24T17:31:51",
    "updatedAt": "2025-09-24T17:31:51",
    "deletedAt": null
    },
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>

<details>
<summary> 로그아웃 </summary>
  
#### Request
```Http
POST /api/auth/logout
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "로그아웃 되었습니다.",
  "data": null,
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>

<details>
<summary> 로그아웃 </summary>
  
#### Request
```Http
DELETE /api/auth/me
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "회원탈퇴가 처리되었습니다.",
  "data": null,
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>

---

## Category

| 기능    | method  | Domain | EndPoint |
|:------| :---    | :----  | :----  |
| 카테고리 생성   | `POST`| `category`| /api/admin/categories |
| 전체 카테고리 조회 | `GET`| `category`| /api/categories|
| 특정 카테고리 조회  | `GET`| `category`| /api/categories/{categoryId}|
| 카테고리 수정 | `PUT`| `category`| /api/admin/categories/{categoryId}|
| 카테고리 삭제 | `DELETE`| `category`| /api/admin/categories/{categoryId}|

<details>
<summary> 카테고리 생성 </summary>
  
#### Request
```Http
POST /api/admin/categories
```
```JSON
{
  "name": "햄스터",
  "description": "햄스터 용품 카테고리"
}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "카테고리 생성이 성공적으로 처리되었습니다..",
  "data": {
    "id": 1,
    "name": "햄스터",
    "description": "햄스터 용품 카테고리",
    "createdAt": "2025-09-24T12:30:00"
    },
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>

<details>
<summary> 전체 카테고리 조회 </summary>
  
#### Request
```Http
GET /api/categories
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "카테고리 조회가 성공적으로 완료되었습니다.",
  "data": [
      {
      "id": 1,
      "name": "햄스터",
      "description": "햄스터 관련 카테고리"
      },
      {
      "id": 2,
      "name": "강아지",
      "description": "강아지 관련 카테고리"
      }
      ],
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>

<details>
<summary> 특정 카테고리 조회 </summary>
  
#### Request
```Http
GET /api/categories/{categoryId}
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "카테고리 조회가 완료되었습니다.",
  "data": {
      "id": 1,
      "name": "햄스터",
      "description": "햄스터 용품 카테고리"
      },
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>

<details>
<summary> 카테고리 수정 </summary>
  
#### Request
```Http
PUT /api/admin/categories/{categoryId}
```
```JSON
{
  "name": "햄스터",
  "description": "햄스터 사료/간식/장난감"
}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "카테고리 수정이 성공적으로 처리되었습니다.",
  "data": {
      "id": 1,
      "name": "햄스터",
      "description": "햄스터 사료/간식/장난감",
      "updatedAt": "2025-09-24T12:40:00"
      },
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>

<details>
<summary> 카테고리 삭제 </summary>
  
#### Request
```Http
DELETE /api/admin/categories/{categoryId}
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "카테고리가 삭제되었습니다.",
  "data": null,
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>

---

## Search

| 기능    | method  | Domain | EndPoint |
|:------| :---    | :----  | :----  |
| 검색 (캐시X) | `GET`| `search`| /api/v1/search?keyword=사료&page=0&size=10 |
| 검색 (캐시O) | `GET`| `search`| /api/v2/search?keyword=사료&page=0&size=10 |
| 인기 검색어 조회 | `GET`| `search`| /api/v2/search/trending?limit=10 |

<details>
<summary> 검색 (캐시X) </summary>
  
#### Request
```Http
GET /api/v1/search?keyword=사료&page=0&size=10
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "성공적으로 검색되었습니다..",
  "data": {
      "content": [
      { "id": 10, "name": “개껌", "price": 1200000 }
      ],
      "page": 0,
      "size": 10,
      "totalElements": 1
      },
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>

<details>
<summary> 검색 (캐시O) </summary>
  
#### Request
```Http
GET /api/v1/search?keyword=사료&page=0&size=10
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "성공적으로 검색되었습니다.",
  "data": {
      "content": [
      { "id": 10, "name": “개껌", "price": 1200000 }
      ],
      "page": 0,
      "size": 10,
      "totalElements": 1
      },
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>

<details>
<summary> 인기 검색어 조회  </summary>
  
#### Request
```Http
GET /api/v2/search/trending?limit=10
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "성공적으로 검색되었습니다.",
  "data": [
      { "keyword": "사료", "count": 520 },
      { "keyword": "개껌", "count": 430 }
      ],
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>

---

## Product

| 기능    | method  | Domain | EndPoint |
|:------| :---    | :----  | :----  |
| 상품 등록 | `POST`| `product`| /api/admin/products |
| 인기 상품 조회 (리뷰순) | `GET`| `product`| /api/products/{productId}/related?sort=review&limit=5 |
| 상품 상세 조회 | `GET`| `product`| /api/products{productId} |
| 상품 수정 | `PUT`| `product`| /api/admin/products/{productId} |
| 상품 삭제 | `DELETE`| `product`| /api/admin/products{productId} |

<details>
<summary> 상품 등록 </summary>
  
#### Request
```Http
POST /api/admin/products
```
```JSON
{
  "categoryId": 1,
  "name": "유기농 강아지 사료",
  "price": 29900,
  "content": "강아지 사료입니다."
}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "상품이 성공적으로 등록되었습니다.",
  "data": {
    "id": 1,
    "categoryId": 1,
    "name": "유기농 강아지 사료",
    "price": 29900,
    "content": "강아지 사료입니다."
    "createdAt": "2025-09-24T15:36:00",
    "updatedAt": "2025-09-24T15:36:00",
    "deletedAt": "NULL"
  },
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>

<details>
<summary> 인기 상품 조회 (리뷰순) </summary>
  
#### Request
```Http
GET /api/products/{productId}/related?sort=review&limit=5
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "상품 목록이 성공적으로 조회되었습니다.",
  "data":{
      "id": 1,
      "categoryId": 1,
      "name": "유기농 강아지 사료",
      "price": 29900,
      "content": "강아지 사료입니다.",
      "reviewCount": 295, 
      "createdAt": "2025-09-24T15:36:00",
      "updatedAt": "2025-09-24T15:36:00",
      "deletedAt": "NULL"
    }, {
      "id": 2,
      "categoryId": 1,
      "name": "유기농 고양이 사료",
      "price": 30000,
      "content": "고양이 사료입니다.",
      "reviewCount": 295,
      "createdAt": "2025-09-24T15:37:00",
      "updatedAt": "2025-09-24T15:37:00",
      "deletedAt": "NULL"
    },
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>

<details>
<summary> 상품 상세 조회 </summary>
  
#### Request
```Http
GET /api/products{productId}
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "상품이 성공적으로 조회되었습니다..",
  "data": {
    "id": 1,
    "categoryId": 1,
    "name": "유기농 강아지 사료",
    "price": 29900,
    "content": "강아지 사료입니다."
    "createdAt": "2025-09-24T15:36:00",
    "updatedAt": "2025-09-24T15:36:00",
    "deletedAt": "NULL"
  },
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>

<details>
<summary> 상품 수정 </summary>
  
#### Request
```Http
PUT /api/admin/products/{productId}
```
```JSON
{
  "categoryId": 1,
  "name": "유기농 강아지 사료",
  "price": 29900,
  "content": "강아지 사료입니다."
}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "상품 정보가 성공적으로 수정되었습니다.",
  "data": {
    "id": 1,
    "categoryId": 1,
    "name": "맛있는 강아지 사료",
    "price": 29900,
    "content": "강아지 사료입니다."
    "createdAt": "2025-09-24T15:36:00",
    "updatedAt": "2025-09-24T15:46:00",
    "deletedAt": "NULL"
  },
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>

<details>
<summary> 상품 삭제 </summary>
  
#### Request
```Http
DELETE /api/admin/products/{productId}
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "상품이 성공적으로 삭제되었습니다.",
  "data": null,
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>

---

## Review

| 기능    | method  | Domain | EndPoint |
|:------| :---    | :----  | :----  |
| 리뷰 작성 | `POST`| `review`| /api/products/{productId}/reviews |
| 상품별 리뷰 목록 조회 | `GET`| `review`| /api/products/{productId}/reviews |
| 리뷰 단건 조회 | `GET`| `review`| /reviews/{reviewId} |
| 리뷰 수정 | `PUT`| `review`| /reviews/{reviewId} |
| 리뷰 삭제 | `DELETE`| `review`| /reviews/{reviewId} |

<details>
<summary> 리뷰 작성 </summary>
  
#### Request
```Http
POST /api/products/{productId}/reviews
```
```JSON
{
    "point" : "3",
    "context" : "사람이 먹어도 좋을 맛인데요?"
}
```

#### Response
```Http
HTTP/1.1 201
Content-Type: application/json;
```
```JSON
{
    "success": true,
    "message": "후기가 성공적으로 등록되었습니다.",
    "data": {
        "id": 1,
        "point": 3,
        "context": "사람이 먹어도 좋을 맛인데요?",
        "productId": 3,
        "productName": "사료_3",
        "userId": 1,
        "nickname": "박소영",
        "likes": 0,
        "createdAt": "2025-10-02T12:16:15.416709",
        "updatedAt": "2025-10-02T12:16:15.416709"
    },
    "timestamp": "2025-10-02T12:16:15.451792"
}
```
</details>

<details>
<summary> 상품별 리뷰 목록 조회 </summary>
  
#### Request
```Http
GET /api/products/{productId}/reviews
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
    "success": true,
    "message": "후기가 성공적으로 조회되었습니다.",
    "data": {
        "averagePoint": 3.0,
        "reviews": {
            "content": [{
                "id": 1,
                "point": 3,
                "context": "사람이 먹어도 좋을 맛인데요?",
                "productId": 3,
                "productName": "사료_3",
                "userId": 1,
                "nickname": "홍길동",
                "likes": 0,
                "createdAt": "2025-10-02T12:16:15.416709",
                "updatedAt": "2025-10-02T12:16:15.416709"
                },
                {
                "id": 2,
                "point": 3,
                "context": "사람이 먹어도 좋을 맛인데요?",
                "productId": 3,
                "productName": "사료_3",
                "userId": 2,
                "nickname": "아무개",
                "likes": 7,
                "createdAt": "2025-10-02T12:16:15.416709",
                "updatedAt": "2025-10-02T12:16:15.416709"
                }],
            "pageable": {
                "pageNumber": 0,
                "pageSize": 10,
                "sort": {
                    "empty": true,
                    "unsorted": true,
                    "sorted": false
                },
                "offset": 0,
                "unpaged": false,
                "paged": true
            },
            "last": true,
            "totalPages": 0,
            "totalElements": 0,
            "first": true,
            "size": 10,
            "number": 0,
            "sort": {
                "empty": true,
                "unsorted": true,
                "sorted": false
            },
            "numberOfElements": 0,
            "empty": true
        }
    },
    "timestamp": "2025-10-02T12:17:16.021512"
}
```
</details>

<details>
<summary> 리뷰 단건 조회 </summary>
  
#### Request
```Http
GET /reviews/{reviewId}
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
    "success": true,
    "message": "후기가 성공적으로 조회되었습니다.",
    "data": {
        "id": 1,
        "point": 3,
        "context": "사람이 먹어도 좋을 맛인데요?",
        "productId": 3,
        "productName": "사료_3",
        "userId": 1,
        "nickname": "박소영",
        "likes": 0,
        "createdAt": "2025-10-02T12:16:15.416709",
        "updatedAt": "2025-10-02T12:16:15.416709"
    },
    "timestamp": "2025-10-02T12:16:15.451792"
}
```
</details>

<details>
<summary> 리뷰 수정 </summary>
  
#### Request
```Http
PUT /reviews/{reviewId}
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
    "success": true,
    "message": "후기가 성공적으로 수정되었습니다.",
    "data": {
        "id": 1,
        "point": 3,
        "context": "사람이 먹어도 좋을 맛인데요?",
        "productId": 3,
        "productName": "사료_3",
        "userId": 1,
        "nickname": "박소영",
        "likes": 0,
        "createdAt": "2025-10-02T12:16:15.416709",
        "updatedAt": "2025-10-02T12:16:15.416709"
    },
    "timestamp": "2025-10-02T12:16:15.451792"
}
```
</details>

<details>
<summary> 리뷰 삭제 </summary>
  
#### Request
```Http
DELETE /reviews/{reviewId}
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "후기가 성공적으로 삭제되었습니다.",
  "data": null,
  "timestamp": "2024-03-21T10:00:00Z"
}
```
</details>

---

## Like

| 기능    | method  | Domain | EndPoint |
|:------| :---    | :----  | :----  |
| 후기 좋아요 토글 | `POST`| `like`| /api/reviews/{reviewId}/likes |

</details>

<details>
<summary> 후기 좋아요 토글 </summary>
  
#### Request
```Http
POST /api/reviews/{reviewId}/likes
```
```JSON
{

}
```

#### Response
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "좋아요가 성공적으로 등록되었습니다.",
  "data": null,
  "timestamp": "2024-03-21T10:00:00Z"
}
```

#### Response2
```Http
HTTP/1.1 200
Content-Type: application/json;
```
```JSON
{
  "success": true,
  "message": "좋아요가 성공적으로 취소되었습니다.",
  "data": null,
  "timestamp": "2024-03-21T10:00:00Z"
}
```

</details>


---

## 비관적 락을 쓴 이유
현재 saveOrIncreaseKeyword 메서드는 동시에 **여러 요청이 들어올 때 문제가 생길 수 있다**. 

예를 들어, 새로운 검색어가 들어왔을 때 여러 스레드가 동시에 findByKeyword를 호출하면, 모두 “아직 없는 검색어”라고 판단해서 동시에 save를 시도할 수 있습니다. 
그런데 keyword 컬럼에 unique 제약조건이 걸려 있다면, 중복 insert 시도 때문에 DataIntegrityViolationException이 터지면서 요청이 실패할 수도 있다.

이걸 막으려면 방법이 몇 가지 있는데, 대표적으로는 비관적 락을 걸어서 안전하게 동시성을 제어하거나, 아니면 save 시 예외가 발생했을 때 다시 findByKeyword를 호출하는 식으로 **재시도 할수있습니다**.

---

## Cache를 쓴 이유

검색이라는 기능은 사용자가 **가장 많이**, 그리고 **가장 자주** 반복적으로 사용하는 기능입니다.
예를 들어, "사료" 같은 키워드는 수많은 사용자가 동일하게 입력할 수 있고, 
**짧은 시간 안에 동일한 요청이 수십 번, 수백 번 들어올 수 있습니다**. 

이 상황에서 매번 DB를 직접 조회하게 되면, 불필요하게 중복된 쿼리가 실행되고 DB 부하가 기하급수적으로 늘어나게 됩니다. 
결국 **응답 속도가 느려지거나**, 더 심각한 경우에는 **서비스 장애로 이어질 수 있습니다**.

하지만 검색 결과라는 데이터는 자주 바뀌지 않습니다.

특정 키워드로 검색한 결과는 몇 초 단위로 바뀌지 않기 때문에,
동일한 요청이 반복된다면 **굳이 매번 DB를 조회할 필요가 없어서** 캐시를 적용하는 이유가 되었습니다.
