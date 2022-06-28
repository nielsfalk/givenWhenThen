package de.nielsfalk.givenwhenthen

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class FixtureMethodsTest {
    var beforeEachCount = 0
    var afterEachCount = 0
    var beforeAllCount=0
    var afterAllCount=0

    @Nested
    inner class TheTest : GivenWhenThenTest(
        scenario { println() },
        scenario { println() },
        scenario(description { "fixture methods are called" },
            expect {
                that(beforeEachCount).isEqualTo(3)
                that(afterEachCount).isEqualTo(2)
                that(beforeAllCount).isEqualTo(1)
                that(afterAllCount).isEqualTo(0) // not yet called
            }),
        beforeEach = { beforeEachCount++ },
        afterEach = {afterEachCount++},
        beforeAll = {beforeAllCount++},
        afterAll = {afterAllCount++},
    ){
        @AfterEach
        fun tearDown() {
            expectThat(afterAllCount).isEqualTo(1)
        }
    }
}