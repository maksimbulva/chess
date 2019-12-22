package extensions

fun String.toUpperCaseIf(predicate: () -> Boolean): String {
    return if (predicate()) {
        toUpperCase()
    } else {
        this
    }
}
