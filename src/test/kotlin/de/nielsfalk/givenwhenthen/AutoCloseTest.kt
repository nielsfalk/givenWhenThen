package de.nielsfalk.givenwhenthen

import strikt.api.expectThat
import strikt.assertions.containsExactly
import strikt.assertions.doesNotContain
import java.io.Closeable

class AutoCloseTest : GivenWhenThenTest(
) {
    val closed = mutableListOf<String>()

    init {
        scenarios += scenario(
            description { "autoClose is executed in every block" },

            `when` {
                runArtificially(
                    scenario(
                        description { "nothing happens here just fixture methods" },
                        expect { }
                    ),
                    scenario(
                        description { "every block has an autoClose" },

                        given {
                            ACloseable("given close").autoClose()
                            ACloseable("given close 2").autoClose { closed += "individual given close" }
                        },
                        `when` { ACloseable("when close").autoClose() },
                        then { ACloseable("then close").autoClose() }
                    ),
                    beforeEach = { ACloseable("beforeEach close").autoClose() },
                    beforeAll = { ACloseable("beforeAll close").autoClose() }
                )
            },
            then {
                expectThat(closed)
                    .containsExactly(
                        "beforeEach close",
                        "then close",
                        "when close",
                        "given close",
                        "individual given close",
                        "beforeEach close",// the second test case
                        "beforeAll close"
                    )
                    .doesNotContain("given close 2")
            }
        )
    }

    inner class ACloseable(val name: String) : Closeable {
        override fun close() {
            closed += name
        }
    }
}