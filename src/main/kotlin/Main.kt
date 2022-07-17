data class Greeting(val name: String)

fun greet(greeting: Greeting) {
    println("Hello ${greeting.name}!")
}

fun main() {
    greet(Greeting("world"))
}
