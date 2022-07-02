package de.nielsfalk.givenwhenthen.example

import de.nielsfalk.givenwhenthen.*
import de.nielsfalk.givenwhenthen.example.RockPaperScissors.*
import strikt.assertions.isEqualTo

class RockPaperScissorsTest : GivenWhenThenTest(
    scenario(
        description {
            "Rock Paper Scissors expectedWinner=${data.expectedWinner}"
        },
        `when` { data.first defend data.second },
        then {
            expectActual()
                .isEqualTo(data.expectedWinner)
        },
        where<RockPaperScissorsTestCase> {
            // @formatter:off
            Rock     ǀ Rock     ǀǀ null
            Rock     ǀ Scissors ǀǀ Rock
            Rock     ǀ Paper    ǀǀ Paper
            Scissors ǀ Scissors ǀǀ null
            Scissors ǀ Paper    ǀǀ Scissors
            Scissors ǀ Rock     ǀǀ Rock
            Paper    ǀ Paper    ǀǀ null
            Paper    ǀ Rock     ǀǀ Paper
            Paper    ǀ Scissors ǀǀ Scissors
            // @formatter:on
        }
    )
)

private data class RockPaperScissorsTestCase(
    val first: RockPaperScissors,
    val second: RockPaperScissors,
    val expectedWinner: RockPaperScissors?
)

enum class RockPaperScissors {
    Rock, Paper, Scissors;

    infix fun defend(other: RockPaperScissors): RockPaperScissors? =
        when {
            rules[this] == other -> this
            rules[other] == this -> other
            else -> null
        }

    companion object {
        val rules = mapOf(
            Rock to Scissors,
            Scissors to Paper,
            Paper to Rock
        )
    }
}
