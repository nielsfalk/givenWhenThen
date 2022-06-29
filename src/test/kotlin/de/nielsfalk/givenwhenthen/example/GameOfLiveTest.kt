package de.nielsfalk.givenwhenthen.example

import de.nielsfalk.givenwhenthen.*
import strikt.assertions.isEqualTo

/**
 * A Test for <a href="https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life">Conway's Game of Life</a>
 */
class GameOfLiveTest : GivenWhenThenTest(
    scenario(
        description {
            "a Cell(alive=${data.wasAlive}, livingNeighbours=${data.livingNeighbours}) " +
                    "will ${if (data.expectedToBeAliveInNextGeneration) "live" else "not live"} in next generation"
        },
        given { Cell(data.wasAlive, data.livingNeighbours) },
        `when` { it.isAliveInNextGeneration },
        then {
            expectActual()
                .isEqualTo(data.expectedToBeAliveInNextGeneration)
        },
        where<GameOfLiveTestCase>(
            """
            | is alive | neighbours alive | expected to be alive in next generation |
            |----------|------------------|-----------------------------------------|
            | false    | 0                | false                                   |
            | false    | 1                | false                                   |
            | false    | 2                | false                                   |
            | false    | 3                | true                                    |
            | false    | 4                | false                                   |
            | false    | 5                | false                                   |
            | false    | 6                | false                                   |
            | false    | 7                | false                                   |
            | false    | 8                | false                                   |
            | false    | 9                | false                                   |
            | true     | 0                | false                                   |
            | true     | 1                | false                                   |
            | true     | 2                | true                                    |
            | true     | 3                | true                                    |
            | true     | 4                | false                                   |
            | true     | 5                | false                                   |
            | true     | 6                | false                                   |
            | true     | 7                | false                                   |
            | true     | 8                | false                                   |
            | true     | 9                | false                                   |
            """.trimIndent()
        )
    )
)

private data class GameOfLiveTestCase(
    val wasAlive: Boolean,
    val livingNeighbours: Int,
    val expectedToBeAliveInNextGeneration: Boolean
)

data class Cell(
    val alive: Boolean,
    val livingNeighbours: Int
) {
    val isAliveInNextGeneration: Boolean
        get() =
            if (alive) {
                livingNeighbours in 2..3
            } else {
                livingNeighbours == 3
            }
}
