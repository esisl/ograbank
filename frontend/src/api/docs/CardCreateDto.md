
# CardCreateDto


## Properties

Name | Type
------------ | -------------
`userId` | string
`cardNumber` | string
`ownerName` | string
`expiryDate` | Date

## Example

```typescript
import type { CardCreateDto } from ''

// TODO: Update the object below with actual values
const example = {
  "userId": null,
  "cardNumber": null,
  "ownerName": null,
  "expiryDate": null,
} satisfies CardCreateDto

console.log(example)

// Convert the instance to a JSON string
const exampleJSON: string = JSON.stringify(example)
console.log(exampleJSON)

// Parse the JSON string back to an object
const exampleParsed = JSON.parse(exampleJSON) as CardCreateDto
console.log(exampleParsed)
```

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


