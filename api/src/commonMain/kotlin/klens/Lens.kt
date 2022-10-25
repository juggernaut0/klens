package klens

interface Lens<R, T> {
    fun get(obj: R): T
    fun set(obj: R, value: T): R
}

@Suppress("FunctionName")
fun <R, T> Lens(
    getter: R.() -> T,
    setter: R.(T) -> R,
): Lens<R, T> {
    return object : Lens<R, T> {
        override fun get(obj: R): T = obj.getter()
        override fun set(obj: R, value: T): R = obj.setter(value)
    }
}

operator fun <R, T, U> Lens<R, T>.times(other: Lens<T, U>): Lens<R, U> {
    return Lens(
        getter = { other.get(get(this)) },
        setter = { set(this, other.set(get(this), it)) }
    )
}

fun <T, R> Lens<T, R>.nullable(): Lens<T?, R?> {
    return Lens(
        getter = { this?.let { o -> get(o) } },
        setter = { this?.also { o ->
            if (it != null) { // TODO this isn't quite right
                set(o, it)
            }
        } }
    )
}
