package de.nielsfalk.givenwhenthen

import mu.KotlinLogging
import org.junit.jupiter.api.DynamicContainer
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest

private val logger = KotlinLogging.logger {}
fun runArtificially(
    vararg scenario: List<TestExecutable<*>>,
    beforeEach: (AutoCloseBlock.() -> Unit)? = null,
    afterEach: (() -> Unit)? = null,
    beforeAll: (AutoCloseBlock.() -> Unit)? = null,
    afterAll: (() -> Unit)? = null
) {
    object : GivenWhenThenTest(
        *scenario,
        beforeEach = beforeEach,
        afterEach = afterEach,
        beforeAll = beforeAll,
        afterAll = afterAll
    ) {
        fun runArtificially() {
            val tests = givenWhenThenTests()
            tests.forEach {
                it.execute()
            }
            logger.info { "artificially executed $tests" }
        }
    }.runArtificially()
}

private fun DynamicNode.execute() {
    when(this) {
        is DynamicTest-> executable.execute()
        is DynamicContainer->{
            children.forEach { it.execute() }
        }
    }
}
