package de.nielsfalk.givenwhenthenwhere

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

abstract class GivenWhenThenWhereTest(private vararg val scenario: List<DynamicTest>) {
    @TestFactory
    fun dynamicTestStream(): List<DynamicTest> = scenario.flatMap { it }
}