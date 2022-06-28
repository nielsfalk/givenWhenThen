package de.nielsfalk.givenwhenthen

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

abstract class GivenWhenThenTest(private vararg val scenario: List<DynamicTest>) {
    @TestFactory
    fun givenWhenThenTests(): List<DynamicTest> = scenario.flatMap { it }
}