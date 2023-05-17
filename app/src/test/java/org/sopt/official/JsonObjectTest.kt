package org.sopt.official

import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class JsonObjectTest {
    @Test
    @DisplayName("json에 message라는 프로퍼티가 kotlinx serialization의 JsonObject로 객체화 시켰을 때에도 잘 가져와진다")
    fun parsingJsonTest() {
        // given
        val json = """
            {
                "message": "1차 출석이 이미 종료되었습니다."
            }
        """.trimIndent()

        // when
        val message = Json.parseToJsonElement(json).jsonObject["message"]?.jsonPrimitive?.contentOrNull

        // then
        assertThat(message).isEqualTo("1차 출석이 이미 종료되었습니다.")
    }
}