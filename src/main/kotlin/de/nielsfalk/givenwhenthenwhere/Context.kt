package de.nielsfalk.givenwhenthenwhere

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
