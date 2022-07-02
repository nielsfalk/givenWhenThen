package de.nielsfalk.givenwhenthen

import kotlinx.coroutines.runBlocking

class Scenario<Given, Actual, DataType>(
    val description: DescriptionFun<DataType>,
    val given: GivenFun<Given, DataType>,
    val `when`: WhenFun<Given, Actual, DataType>,
    val then: ThenFun<Given, Actual, DataType>,
    val where: List<DataType>
) {
    fun executables(): List<TestExecutable<DataType>> =
        where.map { row ->
            val dataContext = DataContext(row)
            dataContext.run {
                TestExecutable(description(row), row) {
                    var whenContext: WhenContext<*, *>? = null
                    var thenContext: ThenContext<*, *, *>? = null

                    try {
                        runBlocking {
                            val givenValue = dataContext.given()
                            whenContext = WhenContext(givenValue, row).apply {
                                val actual = `when`(givenValue)
                                thenContext = ThenContext(givenValue, actual, row).apply {
                                    then(actual)
                                }
                            }
                        }
                    } finally {
                        thenContext?.close()
                        whenContext?.close()
                        close()
                    }
                }
            }
        }
}

fun <Given, Actual, DataType> scenario(
    description: DescriptionFun<DataType> = { "" },
    given: GivenFun<Given, DataType>,
    `when`: WhenFun<Given, Actual, DataType>,
    then: ThenFun<Given, Actual, DataType>,
    where: List<DataType>
): List<TestExecutable<DataType>> =
    Scenario(description, given, `when`, then, where)
        .executables()

fun <Given, Actual> scenario(
    description: DescriptionFun<Unit> = { "" },
    given: GivenFun<Given, Unit>,
    `when`: WhenFun<Given, Actual, Unit>,
    then: ThenFun<Given, Actual, Unit>,
): List<TestExecutable<Unit>> =
    scenario(
        description,
        given,
        `when`,
        then,
        where(Unit)
    )

fun <Actual> scenario(
    description: DescriptionFun<Unit> = { "" },
    `when`: WhenFun<Unit, Actual, Unit>,
    then: ThenFun<Unit, Actual, Unit>,
): List<TestExecutable<Unit>> =
    scenario(description, { data }, `when`, then)

fun <Actual, DataType> scenario(
    description: DescriptionFun<DataType> = { "" },
    `when`: WhenFun<Unit, Actual, DataType>,
    then: ThenFun<Unit, Actual, DataType>,
    where: List<DataType>
): List<TestExecutable<DataType>> =
    Scenario(description, { data }, `when`, then, where)
        .executables()

fun <Given, DataType> scenario(
    description: DescriptionFun<DataType> = { "" },
    given: GivenFun<Given, DataType>,
    expect: ExpectFun<Given, DataType>,
    where: List<DataType>
): List<TestExecutable<DataType>> =
    scenario(description, given, `when` { it }, { WhenContext(this.given, data).expect(data) }, where)

fun <DataType> scenario(
    description: DescriptionFun<DataType> = { "" },
    expect: ExpectFun<Unit, DataType>,
    where: List<DataType>
): List<TestExecutable<DataType>> =
    scenario(description, { }, expect, where)

fun <Given> scenario(
    description: DescriptionFun<Unit> = { "" },
    given: GivenFun<Given, Unit>,
    expect: ExpectFun<Given, Unit>,
): List<TestExecutable<Unit>> =
    scenario(description, given,  expect, where(Unit))
fun scenario(
    description: DescriptionFun<Unit> = { "a test" },
    expect: ExpectFun<Unit, Unit>
): List<TestExecutable<Unit>> =
    scenario(description, {}, expect)

data class TestExecutable<DataType>(
    val description: String,
    val data: DataType,
    val executable: () -> Unit
)

