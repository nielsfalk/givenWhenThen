package de.nielsfalk.givenwhenthen

import org.junit.jupiter.api.*

abstract class GivenWhenThenTest(
    vararg scenario: List<TestExecutable<*>>,
    protected val scenarios: MutableList<List<TestExecutable<*>>> = mutableListOf(*scenario),
    protected var beforeEach: (AutoCloseBlock.() -> Unit)? = null,
    protected var afterEach: (() -> Unit)? = null,
    protected var beforeAll: (AutoCloseBlock.() -> Unit)? = null,
    protected var afterAll: (() -> Unit)? = null,
) {
    @TestFactory
    @DisplayName("cases")
    fun givenWhenThenTests(): List<DynamicNode> {
        val beforeAllAutoClose = AutoCloseBlock()
        val groups = scenarios.flatMap { it }.groupBy { it.description }.toList()
        return groups.flatMapIndexed { groupIndex, (name, tests) ->
            val dynamicTests = tests.mapIndexed { index, test ->
                dynamicTest(
                    name = when {
                        test.data == Unit -> test.description
                        else -> test.data.toString()
                    },
                    beforeAllAutoClose = beforeAllAutoClose,
                    test = test,
                    isFirst = index == 0 && groupIndex == 0,
                    isLast = index == tests.size - 1 && groupIndex == groups.size - 1
                )
            }
            if (tests.size == 1 && tests.first().data == Unit) {
                dynamicTests
            } else {
                listOf(DynamicContainer.dynamicContainer(name, dynamicTests))
            }
        }
    }

    private fun dynamicTest(beforeAllAutoClose: AutoCloseBlock, test: TestExecutable<*>, name: String = test.description, isFirst: Boolean, isLast: Boolean) =
        DynamicTest.dynamicTest(name) {
            if (isFirst) {
                beforeAll?.invoke(beforeAllAutoClose)
            }
            val beforeEachAutoClose = AutoCloseBlock()
            beforeEach?.invoke(beforeEachAutoClose)
            test.executable()
            afterEach?.invoke()
            beforeEachAutoClose.close()

            if (isLast) {
                afterAll?.invoke()
                beforeAllAutoClose.close()
            }
        }
}