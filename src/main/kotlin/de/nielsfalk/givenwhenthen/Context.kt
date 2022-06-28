package de.nielsfalk.givenwhenthen

import strikt.api.ExpectationBuilder

data class DataContext<DataType>(
    val data: DataType
) : AutoCloseBlock()

data class WhenContext<Given, DataType>(
    val given: Given,
    val data: DataType
) : AutoCloseBlock()

data class ThenContext<Given, Actual, DataType>(
    val given: Given,
    val actual: Actual,
    val data: DataType
) : AutoCloseBlock()

class ExpectContext<DataType>(
    val data: DataType,
    expectationBuilder: ExpectationBuilder
) : ExpectationBuilder by expectationBuilder, AutoCloseBlock()
