package de.nielsfalk.givenwhenthen

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

abstract class GivenWhenThenTest(
    private vararg val scenario: List<Pair<String, () -> Unit>>,
    private val beforeEach: (AutoCloseBlock.() -> Unit)? = null,
    private val afterEach: (() -> Unit)? = null,
    private val beforeAll: (AutoCloseBlock.() -> Unit)? = null,
    private val afterAll: (() -> Unit)? = null,
) {
    @TestFactory
    fun givenWhenThenTests(): List<DynamicTest> {
        val allTestFlat = scenario.flatMap { it }
        val beforeAllAutoClose = AutoCloseBlock()
        return allTestFlat.mapIndexed { index, (name, test) ->
            DynamicTest.dynamicTest(name) {
                if (index == 0) {
                    beforeAll?.invoke(beforeAllAutoClose)
                }
                val beforeEachAutoClose = AutoCloseBlock()
                beforeEach?.invoke(beforeEachAutoClose)
                test()
                afterEach?.invoke()
                beforeEachAutoClose.close()
                if (index == allTestFlat.size - 1) {
                    afterAll?.invoke()
                    beforeAllAutoClose.close()
                }
            }
        }
    }
}