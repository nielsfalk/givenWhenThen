@file:Suppress("SimplifyBooleanWithConstants")

package de.nielsfalk.givenwhenthen.example

import de.nielsfalk.givenwhenthen.*
import kotlin.math.max

class PowerAssertionTest : GivenWhenThenTest(
    scenario(
        description { "the maximum of 2 ints" },
        expect {
            val (first, second, expectedResult) = data
            assert(max(first, second) == expectedResult)
        },
        where<Triple<Int, Int, Int>> {
            // @formatter:off
            3  ǀ 15 ǀǀ 15
            2  ǀ 15 ǀǀ 15
            9  ǀ 1  ǀǀ 9
            23 ǀ 15 ǀǀ 23
            // @formatter:on
        }
    )
)
