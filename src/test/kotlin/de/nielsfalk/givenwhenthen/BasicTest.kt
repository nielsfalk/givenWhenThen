package de.nielsfalk.givenwhenthen

import strikt.api.expectThat
import strikt.assertions.isEqualTo

class BasicTest : GivenWhenThenTest(
    scenario(
        description { "a Calculator sqrt 2" },

        given { Calculator() },
        `when` { calculator ->
            calculator.sqrt(2)
        },
        then { expectThat(actual).isEqualTo(4) }
    ),

    scenario(
        description { "a Calculator calculating given numbers sum is 10" },

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
        description { "shorter notation without given - sum should be ${it.first + it.second}" },
        `when` {
            data.run { first + second }
        },
        then { expectThat(actual).isEqualTo(10) },
        where(
            3 to 7,
            2 to 8,
            1 to 9,
        )
    )
)

class Calculator {
    fun sqrt(aNumber: Int) = aNumber * aNumber
    fun sum(first: Int, second: Int) = first + second
}
