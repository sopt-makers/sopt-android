package org.sopt.official.util.wrapper

data class NullableWrapper<ValueType : Any>(
    val value: ValueType?,
) {
    fun get(): ValueType? = value
    fun getOrElse(value: ValueType) = this.get() ?: value

    companion object {
        fun <ValueType : Any> none(): NullableWrapper<ValueType> = NullableWrapper(null)
    }
}

fun <ValueType : Any> ValueType?.asNullableWrapper(): NullableWrapper<ValueType> = NullableWrapper(this)

fun NullableWrapper<String>.getOrEmpty(): String = getOrElse("")
fun <ValueType : Any> NullableWrapper<List<ValueType>>.getOrEmpty(): List<ValueType> = getOrElse(emptyList())
