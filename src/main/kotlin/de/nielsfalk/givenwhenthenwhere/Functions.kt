package de.nielsfalk.givenwhenthenwhere

typealias DescriptionFun<DataType> = DataContext<DataType>.(DataType) -> String
typealias GivenFun<Given, DataType> = DataContext<DataType>.() -> Given
typealias WhenFun<Given, Actual, DataType> = WhenContext<Given, DataType>.(Given) -> Actual
typealias ThenFun<Given, Actual, DataType> = ThenContext<Given, Actual, DataType>.(Actual) -> Unit
typealias ExpectFun<DataType> = DataContext<DataType>.(DataType) -> Unit
typealias StriktExpectFun<DataType> = ExpectContext<DataType>.(DataType) -> Unit

fun <DataType> description(function: DescriptionFun<DataType>) = function
fun <Given, DataType> given(function: GivenFun<Given, DataType>) = function
fun <Given, Actual, DataType> `when`(function: WhenFun<Given, Actual, DataType>) = function
fun <Given, Actual, DataType> then(function: ThenFun<Given, Actual, DataType>) = function
fun <DataType> expect(function: StriktExpectFun<DataType>): ExpectFun<DataType> = {
    strikt.api.expect {
        ExpectContext(data, this)
            .function(data)
    }
}

fun <DataType> where(vararg data: DataType): Array<out DataType> = data
