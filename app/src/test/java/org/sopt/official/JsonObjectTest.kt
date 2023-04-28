package org.sopt.official

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.junit.Test

class JsonObjectTest {
    @Test
    fun `json에 message라는 프로퍼티가 kotlinx serialization의 JsonObject로 객체화 시켰을 때에도 잘 가져와진다`() {
        val json = """
            {
                "message": "1차 출석이 이미 종료되었습니다."
            }
        """.trimIndent()

        val message = Json.parseToJsonElement(json).jsonObject["message"]?.jsonPrimitive?.contentOrNull
        assert(message == "1차 출석이 이미 종료되었습니다.")
    }
}