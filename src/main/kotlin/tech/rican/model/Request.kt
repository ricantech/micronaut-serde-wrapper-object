package tech.rican.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import io.micronaut.serde.annotation.Serdeable

@Serdeable
class Request {
    @JsonProperty("project_id") lateinit var projectId: String
    @JsonProperty("message") lateinit var message: Message
}

@Serdeable
class Message {
    @JsonProperty("id") lateinit var id: String
    @JsonProperty("direction") lateinit var direction: String
    @JsonProperty("contact_message") lateinit var contactMessage: ContactMessage
    @JsonProperty("conversation_id") var conversationId: String? = null
    @JsonProperty("contact_id") var contactId: String? = null
}

@Serdeable
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes(
    JsonSubTypes.Type(value = TextMessage::class),
    JsonSubTypes.Type(value = UrlMessage::class)
)
open class ContactMessage

@Serdeable
@JsonTypeName("text_message")
class TextMessage: ContactMessage() {
    @JsonProperty("text") lateinit var text: String
}

@Serdeable
@JsonTypeName("url_message")
class UrlMessage: ContactMessage() {
    @JsonProperty("url") lateinit var url: String
}