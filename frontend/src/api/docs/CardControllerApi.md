# CardControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createCard**](CardControllerApi.md#createcard) | **POST** /api/cards |  |
| [**getCardsByUser**](CardControllerApi.md#getcardsbyuser) | **GET** /api/cards/user/{userId} |  |
| [**transferCards**](CardControllerApi.md#transfercards) | **POST** /api/cards/transfer |  |



## createCard

> CardResponseDto createCard(cardCreateDto)



### Example

```ts
import {
  Configuration,
  CardControllerApi,
} from '';
import type { CreateCardRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new CardControllerApi();

  const body = {
    // CardCreateDto
    cardCreateDto: ...,
  } satisfies CreateCardRequest;

  try {
    const data = await api.createCard(body);
    console.log(data);
  } catch (error) {
    console.error(error);
  }
}

// Run the test
example().catch(console.error);
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **cardCreateDto** | [CardCreateDto](CardCreateDto.md) |  | |

### Return type

[**CardResponseDto**](CardResponseDto.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: `application/json`
- **Accept**: `*/*`


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Created |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


## getCardsByUser

> PageCardResponseDto getCardsByUser(userId, page, size, sortBy, direction)



### Example

```ts
import {
  Configuration,
  CardControllerApi,
} from '';
import type { GetCardsByUserRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new CardControllerApi();

  const body = {
    // string
    userId: 38400000-8cf0-11bd-b23e-10b96e4ef00d,
    // number (optional)
    page: 56,
    // number (optional)
    size: 56,
    // string (optional)
    sortBy: sortBy_example,
    // 'ASC' | 'DESC' (optional)
    direction: direction_example,
  } satisfies GetCardsByUserRequest;

  try {
    const data = await api.getCardsByUser(body);
    console.log(data);
  } catch (error) {
    console.error(error);
  }
}

// Run the test
example().catch(console.error);
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **userId** | `string` |  | [Defaults to `undefined`] |
| **page** | `number` |  | [Optional] [Defaults to `0`] |
| **size** | `number` |  | [Optional] [Defaults to `10`] |
| **sortBy** | `string` |  | [Optional] [Defaults to `&#39;createdAt&#39;`] |
| **direction** | `ASC`, `DESC` |  | [Optional] [Defaults to `&#39;DESC&#39;`] [Enum: ASC, DESC] |

### Return type

[**PageCardResponseDto**](PageCardResponseDto.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: `*/*`


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


## transferCards

> transferCards(transferRequestDto)



### Example

```ts
import {
  Configuration,
  CardControllerApi,
} from '';
import type { TransferCardsRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const api = new CardControllerApi();

  const body = {
    // TransferRequestDto
    transferRequestDto: ...,
  } satisfies TransferCardsRequest;

  try {
    const data = await api.transferCards(body);
    console.log(data);
  } catch (error) {
    console.error(error);
  }
}

// Run the test
example().catch(console.error);
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **transferRequestDto** | [TransferRequestDto](TransferRequestDto.md) |  | |

### Return type

`void` (Empty response body)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: `application/json`
- **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)

