package de.nielsfalk.givenwhenthen

import strikt.api.expectThat
import strikt.assertions.isFalse
import strikt.assertions.isTrue

class HanldedExcetionsTest : GivenWhenThenTest(
    scenario(
        description { "throw in beforeAll" },
        `when` {
            ActualData().apply {
                testFailed = runArtificially(
                    scenario { testInvoked = true },
                    beforeAll = { throw TestException() },
                    beforeEach = { beforeEachCalled = true },
                    afterAll = {
                        afterAllCalled = true
                    }
                ).isFailure()
            }
        },
        then {
            actual.run {
                expectThat(testFailed).isTrue()
                expectThat(beforeEachCalled).isFalse()
                expectThat(testInvoked).isFalse()
                expectThat(afterAllCalled).isTrue()
            }
        }
    ),
    scenario(
        description { "throw in beforeEach" },
        `when` {
            ActualData().apply {
                testFailed = runArtificially(
                    scenario { testInvoked = true },
                    beforeEach = { throw TestException() },
                    afterAll = {
                        afterAllCalled = true
                    }
                ).isFailure()
            }
        },
        then {
            actual.run {
                expectThat(testFailed).isTrue()
                expectThat(testInvoked).isFalse()
                expectThat(afterAllCalled).isTrue()
            }
        }
    ),
    scenario(
        description { "throw in test" },
        `when` {
            ActualData().apply {
                testFailed = runArtificially(
                    scenario { throw TestException() },
                    afterAll = {
                        afterAllCalled = true
                    }
                ).isFailure()
            }
        },
        then {
            actual.run {
                expectThat(testFailed).isTrue()
                expectThat(afterAllCalled).isTrue()
            }
        }
    ),
    scenario(
        description { "throw in afterEach" },
        `when` {
            var afterAllCalled = false
            runArtificially(
                scenario { },
                afterEach = { throw TestException() },
                afterAll = {
                    afterAllCalled = true
                }
            )
            afterAllCalled
        },
        then {
            expectActual().isTrue()
        }
    ),
    scenario(
        description { "throw in afterAll" },
        `when` {
            var afterAllCalled = false
            runArtificially(
                scenario { },
                afterAll = {
                    afterAllCalled = true
                    throw TestException()
                }
            )
            afterAllCalled
        },
        then {
            expectActual().isTrue()
        }
    ),
    scenario(
        description { "throw in autoClose" },
        `when` {
            ActualData().apply {
                testFailed = runArtificially(
                    scenario { Unit.autoClose { throw TestException() } },
                    afterAll = {
                        afterAllCalled = true
                    }
                ).isFailure()
            }
        },
        then {
            expectThat(actual.afterAllCalled).isTrue()
            expectThat(actual.testFailed).isFalse()
        }
    )
)

private data class ActualData(
    var afterAllCalled: Boolean = false,
    var beforeEachCalled: Boolean = false,
    var testInvoked: Boolean = false,
    var testFailed: Boolean = false
)

private class TestException : Exception("for test purposes only")

