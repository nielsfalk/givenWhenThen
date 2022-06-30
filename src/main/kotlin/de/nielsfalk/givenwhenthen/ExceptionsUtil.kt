package de.nielsfalk.givenwhenthen

import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

fun <T> justLogExceptions(action: () -> Any?, function: () -> T): Exception? = try {
    function()
    null
} catch (e: Exception) {
    logger.error(e) { "${action()} failed" }
    e
}

class CollectExceptions(
    function: CollectExceptions.() -> Unit
) {
    private val internalExceptions = mutableListOf<Exception>()
    val exceptions: List<Exception>
        get() = internalExceptions.toList()

    init {
        this.function()
        if (internalExceptions.isNotEmpty()) {
            throw Exception("There are still unhandled Exceptions $internalExceptions")
        }
    }

    fun collectException(function: () -> Unit) {
        try {
            function()
        } catch (e: Exception) {
            internalExceptions += e
        }
    }

    fun <T> handleExceptions(
        function: CollectExceptions.() -> T
    ) {
        if (internalExceptions.isNotEmpty()) {
            function()
        }
        internalExceptions.clear()
    }
}