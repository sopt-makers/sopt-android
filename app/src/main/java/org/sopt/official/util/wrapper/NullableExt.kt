package org.sopt.official.util.wrapper

data class NullableWrapper<ValueType : Any>(
    val value: ValueType?,
) {
    fun get(): ValueType? = value
    fun getOrElse(value: ValueType) = this.get() ?: value

    fun getOrThrow(): ValueType =
        try {
            value as ValueType
        } catch (exception: Exception) {
            throw TypeCastException()
        }

    companion object {
        fun <ValueType : Any> none(): NullableWrapper<ValueType> = NullableWrapper(null)
    }
}

fun <ValueType : Any> ValueType?.asNullableWrapper(): NullableWrapper<ValueType> = NullableWrapper(this)

fun NullableWrapper<String>.getOrEmpty(): String = getOrElse("")
fun <ValueType : Any> NullableWrapper<List<ValueType>>.getOrEmpty(): List<ValueType> = getOrElse(emptyList())

fun NullableWrapper<Boolean>.getOrFlase() : Boolean = getOrElse(false)
fun NullableWrapper<Boolean>.getOrTrue() : Boolean = getOrElse(true)