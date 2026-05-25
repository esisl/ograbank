
# PageCardResponseDto


## Properties

Name | Type
------------ | -------------
`totalElements` | number
`totalPages` | number
`size` | number
`content` | [Array&lt;CardResponseDto&gt;](CardResponseDto.md)
`number` | number
`sort` | [SortObject](SortObject.md)
`numberOfElements` | number
`pageable` | [PageableObject](PageableObject.md)
`first` | boolean
`last` | boolean
`empty` | boolean

## Example

```typescript
import type { PageCardResponseDto } from ''

// TODO: Update the object below with actual values
const example = {
  "totalElements": null,
  "totalPages": null,
  "size": null,
  "content": null,
  "number": null,
  "sort": null,
  "numberOfElements": null,
  "pageable": null,
  "first": null,
  "last": null,
  "empty": null,
} satisfies PageCardResponseDto

console.log(example)

// Convert the instance to a JSON string
const exampleJSON: string = JSON.stringify(example)
console.log(exampleJSON)

// Parse the JSON string back to an object
const exampleParsed = JSON.parse(exampleJSON) as PageCardResponseDto
console.log(exampleParsed)
```

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


