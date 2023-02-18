package de.nielsfalk.givenwhenthen

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*

abstract class GivenWhenThenTest(
    vararg scenario: List<TestExecutable<*>>,
    protected val scenarios: MutableList<List<TestExecutable<*>>> = mutableListOf(*scenario),
    protected var beforeEach: (suspend AutoCloseBlock.() -> Unit)? = null,
    protected var afterEach: (suspend () -> Unit)? = null,
    protected var beforeAll: (suspend AutoCloseBlock.() -> Unit)? = null,
    protected var afterAll: (suspend () -> Unit)? = null,
) {
    @TestFactory
    @DisplayName("cases")
    fun givenWhenThenTests(): List<DynamicNode> {
        val beforeAllAutoClose = AutoCloseBlock()
        val groups = scenarios.flatten().groupBy { it.description }.toList()
        return groups.flatMapIndexed { groupIndex, (name, tests) ->
            val dynamicTests = tests.mapIndexed { index, test ->
                dynamicTest(
                    name = if (test.data == Unit) test.description else test.data.shortenedToString(),
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

    private fun Any?.shortenedToString(): String =
        this?.toString()?.let {
            val prefix = "${this@shortenedToString::class.simpleName}("
            if (it.startsWith(prefix))
                it.removePrefix(prefix)
                    .removeSuffix(")")
            else null
        } ?: toString()

    private fun dynamicTest(
        beforeAllAutoClose: AutoCloseBlock,
        test: TestExecutable<*>,
        name: String = test.description,
        isFirst: Boolean,
        isLast: Boolean
    ) =
        DynamicTest.dynamicTest(name) {
            CollectExceptions {
                collectException {
                    if (isFirst) {
                        beforeAll?.invokeBlocking(beforeAllAutoClose)
                    }
                    val beforeEachAutoClose = AutoCloseBlock()
                    collectException {
                        beforeEach?.invokeBlocking(beforeEachAutoClose)

                        collectException {
                            test.executable(this)
                        }
                    }
                    collectException {
                        afterEach?.invokeBlocking()
                    }
                    beforeEachAutoClose.close(this)
                }

                if (isLast) {
                    collectException {
                        afterAll?.invokeBlocking()
                    }
                    beforeAllAutoClose.close(this)
                }
                handleExceptions {
                    throw TestExecutionException(name, exceptions)
                }
            }
        }
}

private fun  (suspend () -> Unit).invokeBlocking() {
    runBlocking { this@invokeBlocking() }
}
private fun (suspend AutoCloseBlock.() -> Unit).invokeBlocking(beforeEachAutoClose: AutoCloseBlock) {
    runBlocking { this@invokeBlocking(beforeEachAutoClose) }
}

class TestExecutionException(
    name: String,
    val exceptions: List<Exception>
) :
    Exception("$name should not have exceptions but had $exceptions", exceptions.first())
