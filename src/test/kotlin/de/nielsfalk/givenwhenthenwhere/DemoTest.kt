package de.nielsfalk.givenwhenthenwhere

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Calculator {
    fun sqrt(aNumber: Int) = aNumber * aNumber
    fun sum(first: Int, second: Int) = first + second
}

class DemoTest : GivenWhenThenTest(
    scenario(
        description = "a Calculator sqrt 2",

        given { Calculator() },
        `when` { calculator ->
            calculator.sqrt(2)
        },
        then { expectThat(actual).isEqualTo(4) }
    ),

    scenario(
        description { "a Calculator calculating given numbers($it) sum is 10" },

        given { Calculator() },
        `when` { calculator ->
            calculator.sum(data.first, data.second)
        },
        then { expectThat(actual).isEqualTo(10) },
        where(
            3 to 7,
            2 to 8,
            1 to 9,
        )
    ),

    scenario(
        description { "shorter notation without given and where" },
        `when` {
            3 + 7
        },
        then { expectThat(actual).isEqualTo(10) }
    ),

    scenario(
        description { "shorter notation without given - sum of $it should be ${it.first + it.second}" },
        `when` {
            data.run { first + second }
        },
        then { expectThat(actual).isEqualTo(10) },
        where(
            3 to 7,
            2 to 8,
            1 to 9,
        )
    ),

    scenario(
        description { "even shorter notation with given + when together in expect" },
        expect {
            expectThat(3 + 7).isEqualTo(10)
        }
    ),

    scenario(
        description { "even shorter notation with given + when together in expect block but with where - sum of $it should be ${it.first + it.second}" },
        expect {
            expectThat(data.run { first + second }).isEqualTo(10)
        },
        where(
            3 to 7,
            2 to 8,
            1 to 9,
        )
    )
)

typealias DescriptionFun<DataType> = DataContext<DataType>.(DataType) -> String
typealias GivenFun<Given, DataType> = DataContext<DataType>.() -> Given
typealias WhenFun<Given, Actual, DataType> = WhenContext<Given, DataType>.(Given) -> Actual
typealias ThenFun<Given, Actual, DataType> = ThenContext<Given, Actual, DataType>.(Actual) -> Unit
typealias ExpectFun<DataType> = DataContext<DataType>.(DataType) -> Unit

fun <DataType> description(function: DescriptionFun<DataType>) = function
fun <Given, DataType> given(function: GivenFun<Given, DataType>) = function
fun <Given, Actual, DataType> `when`(function: WhenFun<Given, Actual, DataType>) = function
fun <Given, Actual, DataType> then(function: ThenFun<Given, Actual, DataType>) = function
fun <DataType> expect(function: ExpectFun<DataType>) = function
fun <DataType> where(vararg data: DataType): Array<out DataType> = data


@Suppress("ArrayInDataClass")
fun <Given, Actual> scenario(
    description: String = "",
    given: GivenFun<Given, Unit>,
    `when`: WhenFun<Given, Actual, Unit>,
    then: ThenFun<Given, Actual, Unit>,
): List<DynamicTest> =
    Scenario(
        description { description },
        given,
        `when`,
        then,
        arrayOf(Unit)
    )
        .dynamicTest()

fun <Given, Actual, DataType> scenario(
    description: DescriptionFun<DataType> = { "" },
    given: GivenFun<Given, DataType>,
    `when`: WhenFun<Given, Actual, DataType>,
    then: ThenFun<Given, Actual, DataType>,
    where: Array<out DataType>
): List<DynamicTest> =
    Scenario(description, given, `when`, then, where)
        .dynamicTest()

fun <Actual> scenario(
    description: DescriptionFun<Unit> = { "" },
    `when`: WhenFun<Unit, Actual, Unit>,
    then: ThenFun<Unit, Actual, Unit>,
): List<DynamicTest> =
    Scenario(description, { }, `when`, then, arrayOf(Unit))
        .dynamicTest()

fun <Actual, DataType> scenario(
    description: DescriptionFun<DataType> = { "" },
    `when`: WhenFun<Unit, Actual, DataType>,
    then: ThenFun<Unit, Actual, DataType>,
    where: Array<out DataType>
): List<DynamicTest> =
    Scenario(description, { }, `when`, then, where)
        .dynamicTest()

fun scenario(
    description: DescriptionFun<Unit> = { "" },
    expect: ExpectFun<Unit>
): List<DynamicTest> =
    Scenario(description, { }, { }, { DataContext(data).expect(data) }, arrayOf(Unit))
        .dynamicTest()

fun <DataType> scenario(
    description: DescriptionFun<DataType> = { "" },
    expect: ExpectFun<DataType>,
    where: Array<out DataType>
): List<DynamicTest> =
    Scenario(description, { }, { }, { DataContext(data).expect(data) }, where)
        .dynamicTest()


data class Scenario<Given, Actual, DataType>(
    val description: DescriptionFun<DataType>,
    val given: GivenFun<Given, DataType>,
    val `when`: WhenFun<Given, Actual, DataType>,
    val then: ThenFun<Given, Actual, DataType>,
    @Suppress("ArrayInDataClass") val where: Array<out DataType>
) {
    fun dynamicTest(): List<DynamicTest> =
        where.map { row ->
            val dataContext = DataContext(row)
            dynamicTest(dataContext.description(row)) {
                val givenValue = dataContext
                    .given()
                val actual = WhenContext(givenValue, row)
                    .`when`(givenValue)
                ThenContext(givenValue, actual, row)
                    .then(actual)
            }
        }
}

data class DataContext<DataType>(
    val data: DataType
)

data class WhenContext<Given, DataType>(
    val given: Given,
    val data: DataType
)

data class ThenContext<Given, Actual, DataType>(
    val given: Given,
    val actual: Actual,
    val data: DataType
)

abstract class GivenWhenThenTest(private vararg val scenario: List<DynamicTest>) {
    @TestFactory
    fun dynamicTestStream(): List<DynamicTest> = scenario.flatMap { it }
}