package de.nielsfalk.givenwhenthen

import kotlinx.coroutines.runBlocking

class Scenario<Given, Actual, DataType>(
    val description: DescriptionFun<DataType>,
    val given: GivenFun<Given, DataType>,
    val `when`: WhenFun<Given, Actual, DataType>,
    val then: ThenFun<Given, Actual, DataType>,
    val where: Array<out DataType>
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

fun <Given, Actual> scenario(
    description: DescriptionFun<Unit> = { "" },
    given: GivenFun<Given, Unit>,
    `when`: WhenFun<Given, Actual, Unit>,
    then: ThenFun<Given, Actual, Unit>,
): List<TestExecutable<Unit>> =
    Scenario(
        description,
        given,
        `when`,
        then,
        arrayOf(Unit)
    )
        .executables()

fun <Given, Actual, DataType> scenario(
    description: DescriptionFun<DataType> = { "" },
    given: GivenFun<Given, DataType>,
    `when`: WhenFun<Given, Actual, DataType>,
    then: ThenFun<Given, Actual, DataType>,
    where: Array<out DataType>
): List<TestExecutable<DataType>> =
    Scenario(description, given, `when`, then, where)
        .executables()

fun <Actual> scenario(
    description: DescriptionFun<Unit> = { "" },
    `when`: WhenFun<Unit, Actual, Unit>,
    then: ThenFun<Unit, Actual, Unit>,
): List<TestExecutable<Unit>> =
    Scenario(description, { }, `when`, then, arrayOf(Unit))
        .executables()

fun <Actual, DataType> scenario(
    description: DescriptionFun<DataType> = { "" },
    `when`: WhenFun<Unit, Actual, DataType>,
    then: ThenFun<Unit, Actual, DataType>,
    where: Array<out DataType>
): List<TestExecutable<DataType>> =
    Scenario(description, { }, `when`, then, where)
        .executables()

fun scenario(
    description: DescriptionFun<Unit> = { "a test" },
    expect: ExpectFun<Unit>
): List<TestExecutable<Unit>> =
    Scenario(description, { }, { }, { DataContext(data).expect(data) }, arrayOf(Unit))
        .executables()

fun <DataType> scenario(
    description: DescriptionFun<DataType> = { "" },
    expect: ExpectFun<DataType>,
    where: Array<out DataType>
): List<TestExecutable<DataType>> =
    Scenario(description, { }, { }, { DataContext(data).expect(data) }, where)
        .executables()

data class TestExecutable<DataType>(
    val description:String,
    val data: DataType,
    val executable: ()->Unit
)

