
# CardResponseDto


## Properties

Name | Type
------------ | -------------
`id` | string
`maskedNumber` | string
`ownerName` | string
`expiryDate` | Date
`status` | string
`balance` | number
`createdAt` | Date

## Example

```typescript
import type { CardResponseDto } from ''

// TODO: Update the object below with actual values
const example = {
  "id": null,
  "maskedNumber": null,
  "ownerName": null,
  "expiryDate": null,
  "status": null,
  "balance": null,
  "createdAt": null,
} satisfies CardResponseDto

console.log(example)

// Convert the instance to a JSON string
const exampleJSON: string = JSON.stringify(example)
console.log(exampleJSON)

// Parse the JSON string back to an object
const exampleParsed = JSON.parse(exampleJSON) as CardResponseDto
console.log(exampleParsed)
```

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


