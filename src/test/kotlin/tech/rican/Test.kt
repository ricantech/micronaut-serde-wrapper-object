package tech.rican

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import io.micronaut.serde.ObjectMapper
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import tech.rican.model.Message
import tech.rican.model.Request
import tech.rican.model.TextMessage

@MicronautTest
class Test(
    private val objectMapper: ObjectMapper
) : StringSpec({
    "should serialize the payload" {
        val request = Request().also {
            it.projectId = "project-id"
            it.message = Message().also {
                it.id = "message-id"
                it.direction = "direction"
                it.conversationId = "conversation-id"
                it.contactId = "contact-id"
                it.contactMessage = TextMessage().also { it.text = "Body" }
            }
        }

        //Attributes miss-placed
        // conversation-id within wrapper object "text-response"
        // contact-id within wrapper object "text-response"
        val result = objectMapper.writeValueAsString(request)

        //If deserialized back to Request.class it actually creates the correct object
    }

    "should deserialize the payload - broken" {
        val payload = this::class.java.classLoader.getResourceAsStream("broken-payload-text.json")

        val result = objectMapper.readValue(payload, Request::class.java)

        result.should {
            it.projectId.shouldBe("project-id")
            it.message.should {
                it.id.shouldBe("message-id")
                it.direction.shouldBe("direction")
                it.contactMessage.shouldBeTypeOf<TextMessage>().should {
                    it.text.shouldBe("body")
                }
                it.conversationId.shouldBe("conversation-id")
                it.contactId.shouldBe("contact-id")
            }
        }
    }

    "should deserialize the payload - working" {
        val payload = this::class.java.classLoader.getResourceAsStream("payload-text-diff-order.json")

        val result = objectMapper.readValue(payload, Request::class.java)

        result.should {
            it.projectId.shouldBe("project-id")
            it.message.should {
                it.id.shouldBe("message-id")
                it.direction.shouldBe("direction")
                it.contactMessage.shouldBeTypeOf<TextMessage>().should {
                    it.text.shouldBe("body")
                }
                it.conversationId.shouldBe("conversation-id")
                it.contactId.shouldBe("contact-id")
            }
        }
    }
})
