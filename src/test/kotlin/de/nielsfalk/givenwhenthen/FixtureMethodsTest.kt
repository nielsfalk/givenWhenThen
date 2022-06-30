package de.nielsfalk.givenwhenthen

import strikt.api.expectThat
import strikt.assertions.isEqualTo

class FixtureMethodsTest : GivenWhenThenTest() {
    var beforeEachCount = 0
    var afterEachCount = 0
    var beforeAllCount = 0
    var afterAllCount = 0

    init {
        scenarios += scenario(
            description { "fixture methods are called" },

            `when` {
                runArtificially(
                    scenario { },
                    scenario { },
                    scenario(description { "fixture methods are called" }) {},
                    beforeEach = { beforeEachCount++ },
                    afterEach = { afterEachCount++ },
                    beforeAll = { beforeAllCount++ },
                    afterAll = { afterAllCount++ }
                )
            },
            then {
                expectThat(beforeEachCount).isEqualTo(3)
                expectThat(afterEachCount).isEqualTo(3)
                expectThat(beforeAllCount).isEqualTo(1)
                expectThat(afterAllCount).isEqualTo(1)
            }
        )
    }
}