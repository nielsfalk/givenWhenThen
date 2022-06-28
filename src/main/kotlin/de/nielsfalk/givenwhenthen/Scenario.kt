package de.nielsfalk.givenwhenthen

import kotlinx.coroutines.runBlocking

class Scenario<Given, Actual, DataType>(
    val description: DescriptionFun<DataType>,
    val given: GivenFun<Given, DataType>,
    val `when`: WhenFun<Given, Actual, DataType>,
    val then: ThenFun<Given, Actual, DataType>,
    val where: Array<out DataType>
) {
    fun dynamicTest(): List<Pair<String, () -> Unit>> =
        where.map { row ->
            val dataContext = DataContext(row)
            dataContext.description(row) to {
                runBlocking {
                    val givenValue = dataContext
                        .given()
                    val actual = WhenContext(givenValue, row)
                        .`when`(givenValue)
                    ThenContext(givenValue, actual, row)
                        .then(actual)
                }
            }
        }
}

fun <Given, Actual> scenario(
    description: String = "",
    given: GivenFun<Given, Unit>,
    `when`: WhenFun<Given, Actual, Unit>,
    then: ThenFun<Given, Actual, Unit>,
): List<Pair<String, () -> Unit>> =
    Scenario(
        description { description },
        given,
        `when`,
        then,
        arrayOf(Unit)
    )
        .dynamicTest()

fun <Given, Actual, DataType> scenario(
    description: DescriptionFun<DataType> = { "" },
    given: GivenFun<Given, DataType>,
    `when`: WhenFun<Given, Actual, DataType>,
    then: ThenFun<Given, Actual, DataType>,
    where: Array<out DataType>
): List<Pair<String, () -> Unit>> =
    Scenario(description, given, `when`, then, where)
        .dynamicTest()

fun <Actual> scenario(
    description: DescriptionFun<Unit> = { "" },
    `when`: WhenFun<Unit, Actual, Unit>,
    then: ThenFun<Unit, Actual, Unit>,
): List<Pair<String, () -> Unit>> =
    Scenario(description, { }, `when`, then, arrayOf(Unit))
        .dynamicTest()

fun <Actual, DataType> scenario(
    description: DescriptionFun<DataType> = { "" },
    `when`: WhenFun<Unit, Actual, DataType>,
    then: ThenFun<Unit, Actual, DataType>,
    where: Array<out DataType>
): List<Pair<String, () -> Unit>> =
    Scenario(description, { }, `when`, then, where)
        .dynamicTest()

fun scenario(
    description: DescriptionFun<Unit> = { "a test" },
    expect: ExpectFun<Unit>
): List<Pair<String, () -> Unit>> =
    Scenario(description, { }, { }, { DataContext(data).expect(data) }, arrayOf(Unit))
        .dynamicTest()

fun <DataType> scenario(
    description: DescriptionFun<DataType> = { "" },
    expect: ExpectFun<DataType>,
    where: Array<out DataType>
): List<Pair<String, () -> Unit>> =
    Scenario(description, { }, { }, { DataContext(data).expect(data) }, where)
        .dynamicTest()

