package de.nielsfalk.givenwhenthen.example

import de.nielsfalk.givenwhenthen.*
import strikt.assertions.isEqualTo

/**
 * A Test for <a href="https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life">Conway's Game of Life</a>
 */
class GameOfLiveTest : GivenWhenThenTest(
    scenario(
        description {
            "a Cell will live in next generation"
        },
        given { Cell(data.wasAlive, data.livingNeighbours) },
        `when` { it.isAliveInNextGeneration },
        then {
            expectActual()
                .isEqualTo(data.expectedToBeAliveInNextGeneration)
        },
        where<GameOfLiveTestCase> {
            // @formatter:off
            //  is alive ǀ neighbours alive ǀǀ expected to be alive in next generation
                false    ǀ 0                ǀǀ  false
                false    ǀ 1                ǀǀ  false
                false    ǀ 2                ǀǀ  false
                false    ǀ 3                ǀǀ  true
                false    ǀ 4                ǀǀ  false
                false    ǀ 5                ǀǀ  false
                false    ǀ 6                ǀǀ  false
                false    ǀ 7                ǀǀ  false
                false    ǀ 8                ǀǀ  false
                false    ǀ 9                ǀǀ  false
                true     ǀ 0                ǀǀ  false
                true     ǀ 1                ǀǀ  false
                true     ǀ 2                ǀǀ  true
                true     ǀ 3                ǀǀ  true
                true     ǀ 4                ǀǀ  false
                true     ǀ 5                ǀǀ  false
                true     ǀ 6                ǀǀ  false
                true     ǀ 7                ǀǀ  false
                true     ǀ 8                ǀǀ  false
                true     ǀ 9                ǀǀ  false
            // @formatter:on
        }
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
