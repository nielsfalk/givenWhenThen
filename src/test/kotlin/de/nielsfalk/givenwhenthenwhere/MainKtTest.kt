package de.nielsfalk.givenwhenthenwhere

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import strikt.api.ExpectationBuilder
import strikt.assertions.isEqualTo

class MainKtTest{
    @Test
    fun name() {
        strikt.api.expect { sdfsdf() }
    }

    private fun ExpectationBuilder.sdfsdf() {
        that("").isEqualTo("dsf")
    }
}