package de.nielsfalk.givenwhenthen

import kotlinx.coroutines.runBlocking
import java.io.Closeable

open class AutoCloseBlock {
    private val closables= mutableListOf<suspend ()->Unit>()
    fun <T:Closeable> T.autoClose(): T = this.also {
        closables += { it.close()}
    }

    fun <T> T.autoClose(function:suspend T.()->Unit): T = this.also {
        closables += { it.function()}
    }

    fun close(collectExceptions: CollectExceptions) {
        closables.forEach {
            collectExceptions.collectException { runBlocking { it() } }
        }
    }
}
