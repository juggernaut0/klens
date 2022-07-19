package klens

interface Lens<R: Any, T> {
    fun get(obj: R): T
    fun set(obj: R, value: T): R
}

@Suppress("FunctionName")
fun <R: Any, T> Lens(
    getter: R.() -> T,
    setter: R.(T) -> R,
): Lens<R, T> {
    return object : Lens<R, T> {
        override fun get(obj: R): T = obj.getter()
        override fun set(obj: R, value: T): R = obj.setter(value)
    }
}

operator fun <R: Any, T: Any, U> Lens<R, T>.times(other: Lens<T, U>): Lens<R, U> {
    return Lens(
        getter = { other.get(get(this)) },
        setter = { set(this, other.set(get(this), it)) }
    )
}
