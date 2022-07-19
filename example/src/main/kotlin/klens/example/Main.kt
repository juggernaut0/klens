package klens.example

import klens.Lenses
import klens.times

@Lenses
data class Greeting(val greeting: String, val name: Name) {
    fun greet() {
        println("$greeting ${name.value}!")
    }
}

@Lenses
data class Name(val value: String)

val nameLens = GreetingLenses.name * NameLenses.value

fun main() {
    val greeting = Greeting("Hello", Name("world"))
    val greeting2 = nameLens.set(greeting, "lenses")
    greeting.greet()
    greeting2.greet()
}
