package de.nielsfalk.givenwhenthen

import kotlin.reflect.jvm.isAccessible

inline fun <reified T> where(function: TabularDataBuilder.() -> Unit): Array<T> = readTabularData(function)

inline fun <reified T> readTabularData(function: TabularDataBuilder.() -> Unit): Array<T> =
    TabularDataBuilder().run {
        function()
        val constructors = T::class.constructors

        rowBuilders.map { rowBuilder ->
            constructors.first { it.parameters.size == rowBuilder.cells.size }
                .run {
                    isAccessible = true
                    call(*rowBuilder.cells.toTypedArray())
                }
        }.toTypedArray()
    }

class TabularDataBuilder {
    val rowBuilders = mutableListOf<RowBuilder>()

    @Suppress("FunctionName", "NonAsciiCharacters")
    infix fun Any?.ǀ(nextCell: Any?): RowBuilder = if (this is RowBuilder) {
        cells.add(nextCell)
        this
    } else RowBuilder(firstCell = this, nextCell).also {
        rowBuilders += it
    }
}

class RowBuilder(firstCell: Any?, secondCell: Any?) {
    val cells = mutableListOf(firstCell, secondCell)
}