package org.sopt.official.domain.sopletter.model

data class SopletterDefaultMessages(
    val topicId: Long,
    val title: String,
    val totalCount: Int,
    val nextCursor: Long?,
    val hasNext: Boolean,
    val messages: List<SopletterMessage>,
)

data class SopletterMessage(
    val messageId: Long,
    val previewContent: String,
    val colorCode: String,
    val rotationDegree: Double,
    val shapeType: SopletterShapeType,
)

enum class SopletterShapeType {
    SMOOTH,
    SHARP,
    POINT,
    CLOUD;

    companion object {
        fun from(value: String): SopletterShapeType =
            entries.find { it.name == value } ?: SHARP
    }
}
