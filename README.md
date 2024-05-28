# 용어 및 개념 설명

다음 세 개념은 DB에 각각 테이블로 저장되어 있습니다. DB는 H2 in-memory DB를 사용하여 서버 실행시마다 데이터가 초기화됩니다.
* 카테고리(category): 의상이 가질 수 있는 카테고리를 의미합니다. '상의', '아우터', '바지' 등의 개념입니다. 각 카테고리별로 id와 categoryName ('상의' 등)을 가집니다.
* 브랜드(brand): 쇼핑몰에 입점한 브랜드를 의미합니다. 제공해주신 pdf에서 'A', 'B' 등을 의미합니다. 각 브랜드별로 id와 brandName ('A' 등)을 가집니다.
* 상품(product): 쇼핑몰에 입점한 상품을 의미합니다. 각 상품별로 카테고리, 브랜드, 가격을 가집니다.

요구사항에 보면 운영자는 상품을 추가/변경/삭제할 수 있습니다. 따라서 이 요구사항에 따라 동일한 브랜드 및 카테고리에 여러 상품이 존재할 수 있다고 가정했습니다. ('A' 브랜드의 '상의'카테고리에 11,200원의 가격을 가진 제품 이외에 다른 제품이 존재할 수 있다는 의미입니다) 다만, 동일한 브랜드 및 카테고리에 상품이 한개도 없으면 전체 브랜드의 가격 조회나 전체 카테고리의 가격 조회 시에 정합성 이슈가 생길 수 있어, 동일한 브랜드 및 카테고리에 상품이 반드시 한개는 포함되어 있어야 한다고 가정했습니다. ('A' 브랜드의 '상의' 카테고리에 상품이 하나도 없을 수는 없고 반드시 하나의 상품은 존재해아 한다는 의미입니다)


---

# 요구사항 정리
총 네 가지 요구사항을 제공해 주셨습니다.
1. 고객은 카테고리 별로 최저가격인 브랜드와 가격을 조회하고 총액이 얼마인지 확인할 수 있어야 합니다.
2. 고객은 단일 브랜드로 전체 카테고리 상품을 구매할 경우 최저가격인 브랜드와 총액이 얼마인지 확인할 수 있어야 합니다.
3. 고객은 특정 카테고리에서 최저가격 브랜드와 최고가격 브랜드를 확인하고 각 브랜드 상품의 가격을 확인할 수 있어야 합니다.
4. 운영자는 새로운 브랜드를 등록하고, 모든 브랜드의 상품을 추가, 변경, 삭제할 수 있어야 합니다.

이 중 1~3 번은 고객, 4번은 운영자가 호출하는 API입니다. 별도의 인증 및 권한 시스템은 포함하지 않아도 된다고 가이드가 제공되어 있어서, 1-3번과 4번 API는 API url로 구분하였습니다. 또한 4번 API의 경우 하나의 API 보다 기능을 분리하는 것이 적합해 보여 분리하였습니다. 따라서 구현된 API는 총 7개 입니다.

1. 고객은 카테고리 별로 최저가격인 브랜드와 가격을 조회하고 총액이 얼마인지 확인할 수 있어야 합니다. (위와 동일)
2. 고객은 단일 브랜드로 전체 카테고리 상품을 구매할 경우 최저가격인 브랜드와 총액이 얼마인지 확인할 수 있어야 합니다. (위와 동일)
3. 고객은 특정 카테고리에서 최저가격 브랜드와 최고가격 브랜드를 확인하고 각 브랜드 상품의 가격을 확인할 수 있어야 합니다. (위와 동일)
4. 운영자는 기존에 존재하는 브랜드/카테고리에 새로운 상품을 추가할 수 있습니다.
5. 운영자는 기존에 존재하는 상품의 정보를 변경할 수 있습니다.
6. 운영자는 기존에 존재하는 상품을 삭제할 수 있습니다.
7. 운영자는 새로운 브랜드를 등록할 수 있습니다.

위의 API 들을 편의상 API 1번~7번 으로 부르겠습니다. 

---

# API Specification
### 공통

서버가 시작될 때 입력되는 데이터는 다음과 같습니다. (pdf에서 제공해주신 데이터 그대로입니다)

<img width="1114" alt="image" src="https://github.com/david1403/musinsa_be/assets/37106166/c03c2cc8-3354-4a48-9344-ececf454970f">

API 서버이기 때문에 모든 API Response는 Json의 형식입니다.

또한 Business Exception을 발생할 수 있습니다. 이는 예상 가능한 오류로, 구체적인 예시를 들면 운영자가 새로운 브랜드를 등록하는데 이미 해당 브랜드의 이름이 존재하는 경우, 혹은 카테고리로 상품을 검색하는데 해당 카테고리가 존재하지 않는 경우 등입니다. Business Exception이 발생한 경우, 해당 Error의 코드(errorCode)와 상세 설명(errorMessage)가 제공되며, Response 의 HttpStatusCode는 200이 됩니다.

반면 이 외에 발생하는 Exception은 시스템 오류, 혹은 예상치 못하게 발생한 오류입니다. 이러한 오류의 경우 에러 메시지가 Front에 그대로 노출되면 보안상 이슈가 발생할 수 있어 일괄적인 errorCode와 errorMessage가 제공됩니다. HttpStatusCode는 500 (Internal Server Error)로 제공됩니다. FAIL 시의 resposne는 다음과 같습니다
```
{
    "errorCode": "FAIL",
    "errorMessage": "시스템 오류입니다."
}
```

편의상 API 4-7번을 먼저 설명하고, 변경된 데이터를 바탕으로 API 1-3번의 response를 제공하겠습니다.

---

## API 4)
* 운영자는 기존에 존재하는 브랜드/카테고리에 새로운 상품을 추가할 수 있습니다.
* url: {domain}/api/admin/register/product
* method: POST
* request: DTO(including brandName, categoryName, price),
* response: registered product info

Request 예시
```
{
    "brandName": "A",
    "categoryName": "상의",
    "price": 10000
}
```

성공적인 Response 예시
```
{
    "productId": 73,
    "price": 10000,
    "categoryDto": {
        "categoryId": 1,
        "categoryName": "상의"
    },
    "brandDto": {
        "brandId": 1,
        "brandName": "A"
    }
}
``` 

admin은 브랜드 이름, 카테고리 이름, 그리고 상품의 가격을 입력합니다. 위 API가 예시대로 호출되어 성공적으로 등록된 경우, 'A'브랜드의 '상의'카테고리로 10000원의 가격을 가진 상품이 등록됩니다. API 호출 이후 상품 데이터는 다음과 같습니다. 'A'브랜드의 '상의' 카테고리로 다음과 같이 기존에 존재하던 11200원의 상품 이외에 10000원 상품이 추가되었습니다.
<img width="1114" alt="image" src="https://github.com/david1403/musinsa_be/assets/37106166/b8cd84c2-69b0-44ea-a7a4-6bae633278de">

Business Exception
* brandName으로 존재하지 않는 브랜드가 입력되는 경우 (e.g. brandName: "Z"로 입력하는 경우)
```
{
    "errorCode": "BRAND_NOT_FOUND_BY_BRAND_NAME",
    "errorMessage": "해당 이름을 가진 브랜드가 존재하지 않습니다."
}
```

* categoryName으로 존재하지 않는 카테고리가 입력되는 경우 (e.g. categoryName: "장갑"으로 입력하는 경우)
```
{
    "errorCode": "CATEGORY_NOT_FOUND_BY_CATEGORY_NAME",
    "errorMessage": "해당 이름을 가진 카테고리가 존재하지 않습니다."
}
```


## API 5)
* 운영자는 기존에 존재하는 상품의 정보를 변경할 수 있습니다.
* url: {domain}/api/admin/modify/product
* method: POST
* request: DTO(including productId, price to be changed)
* response: modified product info

Request 예시
```
{
    "productId": 72,
    "modifiedPrice": 2399
}
```

Resposne 예시
```
{
    "productId": 72,
    "price": 2399,
    "categoryDto": {
        "categoryId": 8,
        "categoryName": "액세서리"
    },
    "brandDto": {
        "brandId": 9,
        "brandName": "I"
    }
}
```

admin은 상품의 id와 해당 상품이 변경될 가격을 입력합니다. 위 예시대로 호출할 경우, 72번 상품에 해당하는 브랜드 'I'의 액세서리의 가격이 2400에서 2399로 변경됩니다.
<img width="1117" alt="image" src="https://github.com/david1403/musinsa_be/assets/37106166/f03380f5-e2b4-43ad-b878-a3dce1de1c12">

Business Exception
* 변경을 요청하는 id의 상품이 존재하지 않는 경우 (e.g. productId: 99)
```
{
    "errorCode": "PRODUCT_NOT_FOUND_BY_PRODUCT_ID",
    "errorMessage": "해당 id를 가진 상품이 존재하지 않습니다."
}
```

## API 6)
* 운영자는 기존에 존재하는 상품을 삭제할 수 있습니다.
* url: {domain}/api/admin/delete/product/{productId}
* method: POST
* request: productId (pathVariable)
* resposne: String

Request 예시
```
localhost:8080/api/admin/delete/product/73
```

Response 예시
```
Successfully deleted product with productId: 73
```

admin은 상품id를 입력하여 해당 상품을 삭제할 수 있습니다. 다만 동일한 브랜드와 카테고리를 가진 상품이 적어도 한개는 반드시 존재해야 하기 때문에, 삭제하려는 상품이 해당 카테고리와 브랜드의 유일한 상품이면 삭제가 불가능합니다.

Business Exception
* 삭제하려는 상품이 해당 브랜드와 카테고리의 유일한 상품인 경우
```
{
    "errorCode": "PRODUCT_DELETION_NOT_AVAILABLE",
    "errorMessage": "삭제하려는 상품이 해당 브랜드 및 카테고리에 존재하는 유일한 상품입니다."
}
```

* 삭제하려는 상품의 id가 존재하지 않는 경우 (e.g. productId: 99)
```
{
    "errorCode": "PRODUCT_NOT_FOUND_BY_PRODUCT_ID",
    "errorMessage": "해당 id를 가진 상품이 존재하지 않습니다."
}
```

## API 7)
* 운영자는 새로운 브랜드를 등록할 수 있습니다.
* url: {domain}/api/admin/register/brand
* method: POST
* request: DTO (brandName, list of products by category)
* response: String

Request 예시
```
{
    "brandName": "K",
    "products": {
        "상의": [5000, 4000],
        "아우터": [1000],
        "바지":[6000],
        "스니커즈":[8000],
        "가방":[2000],
        "모자":[3000],
        "양말":[1000, 2000],
        "액세서리":[2500]
    }
}
```

Resposne 예시
```
Successfully added brand with name: K
```

admin은 추가하려는 브랜드의 이름과 상품의 정보를 기입합니다. 상품의 정보를 입력할 때, 현재 DB에 존재하는 모든 카테고리를 입력해야 합니다. 또한 동일한 브랜드와 카테고리를 가진 상품이 적어도 한개는 반드시 존재해야 하기 때문에, 빈 배열이 들어올 수 없습니다.

Business Exception
* 누락된 카테고리가 있거나, 현재 존재하지 않는 카테고리가 입력된 경우, 혹은 카테고리의 상품 가격 정보가 빈 배열인 경우
```
{
    "errorCode": "CATEGORY_MISMATCH_IN_PARAMETER",
    "errorMessage": "해당 브랜드는 카테고리가 누락되어 있거나, 존재하지 않는 카테고리의 상품 정보가 등록되어 있습니다."
}
```

* 이미 존재하는 이름의 브랜드를 등록하려고 할 경우
```
{
    "errorCode": "DUPLICATE_BRAND_NAME",
    "errorMessage": "해당 이름을 가진 브랜드가 이미 존재합니다. 다른 이름의 브랜드를 등록해주세요."
}
```

## API 1)

API 1-3에 사용되는 상품 정보는 다음과 같습니다.
<img width="1120" alt="image" src="https://github.com/david1403/musinsa_be/assets/37106166/13a299e4-4474-4685-a03f-faaa4aee27c4">
* 고객은 카테고리 별로 최저가격인 브랜드와 가격을 조회하고 총액이 얼마인지 확인할 수 있어야 합니다.
* url: {domain}/api/customer/products/cheapest-in-category
* method: GET
* request: not needed
* response: DTO

<details><summary>Response 예시 (dropdown 을 클릭하세요)</summary>
현재 Response는 admin API로 여러 데이터를 추가해서 기존의 응답과 다를 수 있습니다.

```javascript
{
    "productInfoMap": {
        "액세서리": [
            {
                "productId": 48,
                "price": 1900,
                "categoryDto": {
                    "categoryId": 8,
                    "categoryName": "액세서리"
                },
                "brandDto": {
                    "brandId": 6,
                    "brandName": "F"
                }
            }
        ],
        "양말": [
            {
                "productId": 80,
                "price": 1000,
                "categoryDto": {
                    "categoryId": 7,
                    "categoryName": "양말"
                },
                "brandDto": {
                    "brandId": 10,
                    "brandName": "K"
                }
            }
        ],
        "아우터": [
            {
                "productId": 34,
                "price": 5000,
                "categoryDto": {
                    "categoryId": 2,
                    "categoryName": "아우터"
                },
                "brandDto": {
                    "brandId": 5,
                    "brandName": "E"
                }
            }
        ],
        "바지": [
            {
                "productId": 27,
                "price": 3000,
                "categoryDto": {
                    "categoryId": 3,
                    "categoryName": "바지"
                },
                "brandDto": {
                    "brandId": 4,
                    "brandName": "D"
                }
            }
        ],
        "스니커즈": [
            {
                "productId": 77,
                "price": 8000,
                "categoryDto": {
                    "categoryId": 4,
                    "categoryName": "스니커즈"
                },
                "brandDto": {
                    "brandId": 10,
                    "brandName": "K"
                }
            }
        ],
        "가방": [
            {
                "productId": 5,
                "price": 2000,
                "categoryDto": {
                    "categoryId": 5,
                    "categoryName": "가방"
                },
                "brandDto": {
                    "brandId": 1,
                    "brandName": "A"
                }
            },
            {
                "productId": 78,
                "price": 2000,
                "categoryDto": {
                    "categoryId": 5,
                    "categoryName": "가방"
                },
                "brandDto": {
                    "brandId": 10,
                    "brandName": "K"
                }
            }
        ],
        "상의": [
            {
                "productId": 74,
                "price": 4000,
                "categoryDto": {
                    "categoryId": 1,
                    "categoryName": "상의"
                },
                "brandDto": {
                    "brandId": 10,
                    "brandName": "K"
                }
            }
        ],
        "모자": [
            {
                "productId": 30,
                "price": 1500,
                "categoryDto": {
                    "categoryId": 6,
                    "categoryName": "모자"
                },
                "brandDto": {
                    "brandId": 4,
                    "brandName": "D"
                }
            }
        ]
    },
    "totalPrice": 26400
}
```
</details>

한 카테고리 아래에 가장 저렴한 상품이 여러개일 경우, 모든 상품을 나열합니다. (위 예시 Response의 가방 항목이 예시입니다)

주요 동작 원리는 다음과 같습니다
* 모든 카테고리 정보를 가져옵니다
* 각 카테고리별로 해당 카테고리 내의 상품들 중 가장 저렴한 상품을 찾습니다
* 해당 카테고리 내에서 최저가 상품과 같은 가격을 가진 제품을 모두 반환합니다
* 카테고리에 여러개의 최저가 상품이 존재하더라도, 최종 합계 금액(totalPrice)에는 한번만 더해서 최종 합계 금액을 도출합니다

## API 2)
* 고객은 단일 브랜드로 전체 카테고리 상품을 구매할 경우 최저가격인 브랜드와 총액이 얼마인지 확인할 수 있어야 합니다.
* url: {domain}/api/customer/product/cheapest-by-brand
* method: GET
* request: none
* response: DTO

<details><summary>Response 예시</summary>
현재 Response는 admin API로 여러 데이터를 추가해서 기존의 응답과 다를 수 있습니다.

```javascript
{
    "brandName": "K",
    "products": [
        {
            "productId": 74,
            "price": 4000,
            "categoryDto": {
                "categoryId": 1,
                "categoryName": "상의"
            },
            "brandDto": {
                "brandId": 10,
                "brandName": "K"
            }
        },
        {
            "productId": 75,
            "price": 5500,
            "categoryDto": {
                "categoryId": 2,
                "categoryName": "아우터"
            },
            "brandDto": {
                "brandId": 10,
                "brandName": "K"
            }
        },
        {
            "productId": 76,
            "price": 4000,
            "categoryDto": {
                "categoryId": 3,
                "categoryName": "바지"
            },
            "brandDto": {
                "brandId": 10,
                "brandName": "K"
            }
        },
        {
            "productId": 77,
            "price": 8000,
            "categoryDto": {
                "categoryId": 4,
                "categoryName": "스니커즈"
            },
            "brandDto": {
                "brandId": 10,
                "brandName": "K"
            }
        },
        {
            "productId": 79,
            "price": 3000,
            "categoryDto": {
                "categoryId": 6,
                "categoryName": "모자"
            },
            "brandDto": {
                "brandId": 10,
                "brandName": "K"
            }
        },
        {
            "productId": 80,
            "price": 1000,
            "categoryDto": {
                "categoryId": 7,
                "categoryName": "양말"
            },
            "brandDto": {
                "brandId": 10,
                "brandName": "K"
            }
        },
        {
            "productId": 78,
            "price": 2000,
            "categoryDto": {
                "categoryId": 5,
                "categoryName": "가방"
            },
            "brandDto": {
                "brandId": 10,
                "brandName": "K"
            }
        },
        {
            "productId": 82,
            "price": 2500,
            "categoryDto": {
                "categoryId": 8,
                "categoryName": "액세서리"
            },
            "brandDto": {
                "brandId": 10,
                "brandName": "K"
            }
        }
    ],
    "totalPrice": 30000
}
```
</details>

주요 동작 원리는 다음과 같습니다
* 모든 브랜드 정보를 가져옵니다.
* 브랜드 별로 다음의 과정을 거칩니다
	* 해당 브랜드의 모든 상품을 가져와서 카테고리 별로 나눕니다.
	* 한 카테고리에 여러 상품이 존재할 경우 가장 저렴한 상품을 찾습니다.
	* 가져온 카테고리별 최저가 상품들의 가격을 더해서 '해당 브랜드로 일괄 구매했을 때의 비용'을 구합니다.
* '일괄 구매시 비용'이 가장 저렴한 브랜드를 반환합니다.

## API 3)
* 고객은 특정 카테고리에서 최저가격 브랜드와 최고가격 브랜드를 확인하고 각 브랜드 상품의 가격을 확인할 수 있어야 합니다.
* url: {domain}/api/customer/products/by-category
* method: GET
* request: categoryName
* response: DTO

Request 예시 - GET method parameter로 한글이 들어와서 url encoding이 필요합니다. 예시는 '가방'을 인코딩한 url 입니다.
```
localhost:8080/api/customer/products/by-category?categoryName=%EA%B0%80%EB%B0%A9
```

<details><summary>Response 예시</summary>
현재 Response는 admin API로 여러 데이터를 추가해서 기존의 응답과 다를 수 있습니다.

```javascript
{
    "categoryName": "가방",
    "cheapestProducts": [
        {
            "productId": 5,
            "price": 2000,
            "categoryDto": {
                "categoryId": 5,
                "categoryName": "가방"
            },
            "brandDto": {
                "brandId": 1,
                "brandName": "A"
            }
        },
        {
            "productId": 78,
            "price": 2000,
            "categoryDto": {
                "categoryId": 5,
                "categoryName": "가방"
            },
            "brandDto": {
                "brandId": 10,
                "brandName": "K"
            }
        }
    ],
    "mostExpensiveProducts": [
        {
            "productId": 29,
            "price": 2500,
            "categoryDto": {
                "categoryId": 5,
                "categoryName": "가방"
            },
            "brandDto": {
                "brandId": 4,
                "brandName": "D"
            }
        }
    ]
}
```
</details>

주요 동작 원리는 다음과 같습니다.
* 입력으로 주어진 카테고리 이름의 카테고리에 속한 모든 상품을 찾습니다.
* 최저가 상품과 최고가 상품을 찾고, 최저가 상품과 가격이 일치하는 모든 상품 / 최고가 상품과 가격이 일치하는 모든 상품을 반환합니다.

Business Excpetion
* 카테고리 이름으로 존재하지 않는 카테고리가 들어올 경우 (e.g. categoryName="장갑")
```
{
    "errorCode": "CATEGORY_NOT_FOUND_BY_CATEGORY_NAME",
    "errorMessage": "해당 이름을 가진 카테고리가 존재하지 않습니다."
}
```


# System Architecture

서버 구조, 및 사용한 기술 스택에 대해 간단하게 정리하겠습니다.

서버는 Controller - Service - Repository 의 3 layer architecture 로 구성했습니다.

DB에 테이블로 저장된 Brand, Category, Product Entity 객체를 생성하고, 각각을 조작하는 Repository, Service를 만들었습니다. 

사용자의 Request, 혹은 admin의 Request는 여러 테이블에 걸친 데이터의 조회 및 조작이 필요하므로 별도의 상위 Service로 분류하였습니다. 

또한 Entity가 Controller의 Response로 노출되어 Entity의 변경이 API Spec의 변경으로 이어지는 결과를 막기 위해 Response용 DTO를 생성하고, DTO -> Entity / Entity -> DTO 변환을 위한 Converter 를 두었습니다. 대략적인 class diagram 은 다음과 같습니다.


<img width="1092" alt="image" src="https://github.com/david1403/musinsa_be/assets/37106166/ac47c552-b320-411c-a09a-b800726dec1a">

예외를 효율적으로 처리하기 위해 별도의 Exception Class를 생성하고 (MusinsaApiException) 해당 예외를 처리하기 위한 ExceptionHandler를 구현하였습니다. messaging은 MessageSource를 이용하여 별도의 파일 (messages.properties)에서 일괄적으로 처리할 수 있게 하였습니다.

데이터 조작은 JPA / H2 database를 이용하였습니다. Spring Data JPA의 기술들 (Entity, JpaRepository 등)을 활용했습니다. in-memory DB이기 때문에 서버가 실행될 때마다 테이블 생성 및 초기 데이터를 입력해야 합니다. 이를 처리하기 위해 서버가 뜰 때 schema.sql 에서 테이블 생성 등의 DML을, data.sql에서 초기 데이터 입력의 DDL을 실행합니다. 테이블이 정상적으로 생성되었을 때에만 서버가 뜨도록 spring.jpa.hibernate.ddl-auto 옵션을 validate로 설정하였습니다. 

모든 API 통신은 서버를 띄워 localhost:8080 환경 아래에서 테스트하였으며, 테스트할 수 있는 최대한 많은 경우에 대해서 테스트하고, 결과를 API specification 항목으로 공유드렸습니다. Postman을 이용하여 API를 테스트한 예시 화면입니다

<img width="1073" alt="image" src="https://github.com/david1403/musinsa_be/assets/37106166/eaa1b3c8-e208-4049-ae67-5303be23adb6">

모든 service / controller 에 대해서 unit test를 작성하였습니다. Mockito기반의 given/when/then pattern을 이용하였으며, 테스트하는 클래스의 하위 클래스를 mocking 하였고, 최대한 많은 예외 클래스까지 테스트항목에 포함시켰습니다.

빌드는 gradle을 사용하였고
```
./gradlew build
```
명령어를 통하여 빌드, 
```
 java -jar ./build/libs/musinsa-0.0.1-SNAPSHOT.jar
```
를 통해 빌드된 jar 파일을 실행하여 서버를 띄울 수 있습니다.

DB는
```
https://localhost:8080/h2-console/
```
를 통해 h2 console에 접속할 수 있으며

<img width="447" alt="image" src="https://github.com/david1403/musinsa_be/assets/37106166/e4a56bda-0719-488a-aaab-fcac3e26bb2a">

다음 정보를 입력하면 Connect 하여 콘솔로 DB 내 테이블 데이터를 확인할 수 있습니다.
