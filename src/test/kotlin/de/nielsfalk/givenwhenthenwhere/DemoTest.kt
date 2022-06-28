package de.nielsfalk.givenwhenthenwhere

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Calculator {
    fun sqrt(aNumber: Int) = aNumber * aNumber
}

class DemoTest : GivenWhenThenTest(
    scenario(
        description = "a Calculator sqrt 2",

        given { Calculator() },
        `when` { calculator ->
            calculator.sqrt(2)
        },
        then { expectThat(actual).isEqualTo(4) }
    )
)

fun <Given> given(function: () -> Given): () -> Given = function
fun <Given, Actual> `when`(function: (Given) -> Actual): (Given) -> Actual = function
fun <Given, Actual> then(function: ThenContext<Given, Actual>.(Actual) -> Unit): ThenContext<Given, Actual>.(Actual) -> Unit = function

fun <Given, Actual> scenario(
    description: String = "",
    given: () -> Given,
    `when`: (Given) -> Actual,
    then: ThenContext<Given, Actual>.(Actual) -> Unit
): DynamicTest {
    val scenario = Scenario(description, given, `when`, then)
    return dynamicTest(scenario.description) {
        val givenValue = scenario.given()
        val actual = scenario.`when`(givenValue)
        scenario.run {
            val thenContext = ThenContext(givenValue, actual)
            thenContext.then(actual)
        }
    }
}


data class Scenario<Given, Actual>(
    val description: String = "",
    val given: () -> Given,
    val `when`: (Given) -> Actual,
    val then: ThenContext<Given, Actual>.(Actual) -> Unit,
)

data class ThenContext<Given, Actual>(
    val given: Given,
    val actual: Actual
)

abstract class GivenWhenThenTest(vararg val scenario: DynamicTest) {
    @TestFactory
    fun dynamicTestStream(): Array<out DynamicTest> = scenario
}
