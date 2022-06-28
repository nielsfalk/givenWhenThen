package de.nielsfalk.givenwhenthen

import strikt.api.ExpectationBuilder

data class DataContext<DataType>(
    val data: DataType
)

data class WhenContext<Given, DataType>(
    val given: Given,
    val data: DataType
)

data class ThenContext<Given, Actual, DataType>(
    val given: Given,
    val actual: Actual,
    val data: DataType
)

class ExpectContext<DataType>(
    val data: DataType,
    expectationBuilder: ExpectationBuilder
) : ExpectationBuilder by expectationBuilder
