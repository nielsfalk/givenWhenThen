package de.nielsfalk.givenwhenthen

import java.io.Closeable

open class AutoCloseBlock {
    private val closables= mutableListOf<()->Unit>()
    fun <T:Closeable> T.autoClose(): T = this.also {
        closables += { it.close()}
    }

    fun <T> T.autoClose(function:T.()->Unit): T = this.also {
        closables += { it.function()}
    }

    fun close(){
        closables.forEach {it()}
    }
}
