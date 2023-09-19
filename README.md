# Repository to reproduce the issue when using `JsonTypeInfo.As.WRAPPER_OBJECT`

## Description

If `WRAPPER_OBJECT` is used the correct deserialization depends on attributes order.
1) See [broken-payload](src/test/resources/broken-payload-text.json) - if `contact_message` is followed by any other attributes values are not deserialized (== null)
2) See [valid-payload](src/test/resources/payload-text-diff-order.json) - if `contact_message` is placed as last attribute object is deserialized fully and correctly
3) If `contact_message` is defined not as last within the code (or by definition of Order). The resulting json object having miss-placed the later attributes. In my example e.g. conversation_id and contact_id as serialized JSON looks as follows:
```json
{
  "project_id": "project-id",
  "message": {
    "id": "message-id",
    "direction": "direction",
    "contact_message": {
      "text_message": {
        "text": "Body"
      },
      "conversation_id": "conversation-id",
      "contact_id": "contact-id"
    }
  }
}
```

## Assumptions
- I did some investigation and it seems when we de-serialize the wrapper object and call the `io.micronaut.serde.Decoder.finishStructure(boolean)` when we are increment remaining depth we only increment by +1 which breaks the `objectDecoder` logic defined at `io/micronaut/serde/support/deserializers/SimpleObjectDeserializer.java:90` as it returns `null` on decodeKey() which stops the `Message` object de-serialization.

## Resources

The models can be found [here](src/main/kotlin/tech/rican/model/Request.kt)

The test to simulate the scenarios [here](src/test/kotlin/tech/rican/Test.kt)
