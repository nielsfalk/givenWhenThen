package de.nielsfalk.givenwhenthen

import strikt.api.expectThat
import strikt.assertions.containsExactly

class FixtureMethodsTest : GivenWhenThenTest() {
    val recordedFixtureMethodCalls = mutableListOf<String>()

    init {
        scenarios += scenario(
            description { "fixture methods are called" },

            `when` {
                runArtificially(
                    scenario { },
                    scenario { },
                    beforeEach = { recordedFixtureMethodCalls += "beforeEach" },
                    afterEach = { recordedFixtureMethodCalls += "afterEach" },
                    beforeAll = { recordedFixtureMethodCalls += "beforeAll" },
                    afterAll = { recordedFixtureMethodCalls += "afterAll" }
                )
            },
            then {
                expectThat(recordedFixtureMethodCalls).containsExactly(
                    "beforeAll",
                    "beforeEach",
                    "afterEach",
                    "beforeEach",
                    "afterEach",
                    "afterAll"
                )
            }
        )
    }
}