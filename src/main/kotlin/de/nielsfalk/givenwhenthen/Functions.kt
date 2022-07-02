package de.nielsfalk.givenwhenthen

import de.nielsfalk.kotlintabulardata.readMarkdown
import org.intellij.lang.annotations.Language

typealias DescriptionFun<DataType> = DataContext<DataType>.(DataType) -> String
typealias GivenFun<Given, DataType> = suspend DataContext<DataType>.() -> Given
typealias WhenFun<Given, Actual, DataType> = suspend WhenContext<Given, DataType>.(Given) -> Actual
typealias ThenFun<Given, Actual, DataType> = suspend ThenContext<Given, Actual, DataType>.(Actual) -> Unit
typealias ExpectFun<Given, DataType> = suspend WhenContext<Given, DataType>.(DataType) -> Unit
typealias StriktExpectFun<Given, DataType> = suspend ExpectContext<Given, DataType>.(DataType) -> Unit

fun <DataType> description(function: DescriptionFun<DataType>) = function
fun <Given, DataType> given(function: GivenFun<Given, DataType>) = function
fun <Given, Actual, DataType> `when`(function: WhenFun<Given, Actual, DataType>) = function
fun <Given, Actual, DataType> then(function: ThenFun<Given, Actual, DataType>) = function
fun <Given, DataType> expect(function: StriktExpectFun<Given, DataType>): ExpectFun<Given, DataType> = {
    strikt.api.expect {
        ExpectContext(given, data, this)
            .function(data)
    }
}

fun <DataType> where(vararg data: DataType): List<DataType> = listOf(*data)

inline fun <reified DataType> where(
    @Language("Markdown") markdown: String
): List<DataType> =
    readMarkdown(markdown)

inline fun <reified DataType> where(
    function: TabularDataBuilder.() -> Unit
): List<DataType> =
    readTabularData(function)
