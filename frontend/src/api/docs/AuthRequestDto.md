
# AuthRequestDto


## Properties

Name | Type
------------ | -------------
`email` | string
`password` | string

## Example

```typescript
import type { AuthRequestDto } from ''

// TODO: Update the object below with actual values
const example = {
  "email": null,
  "password": null,
} satisfies AuthRequestDto

console.log(example)

// Convert the instance to a JSON string
const exampleJSON: string = JSON.stringify(example)
console.log(exampleJSON)

// Parse the JSON string back to an object
const exampleParsed = JSON.parse(exampleJSON) as AuthRequestDto
console.log(exampleParsed)
```

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


