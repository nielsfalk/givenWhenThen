package de.nielsfalk.givenwhenthen.example

import de.nielsfalk.givenwhenthen.*
import de.nielsfalk.givenwhenthen.example.RockPaperScissors.*
import strikt.assertions.isEqualTo

class ShowcaseTest : GivenWhenThenTest(
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
            Rock     ǀ Rock     ǀ null
            Rock     ǀ Scissors ǀ Rock
            Rock     ǀ Paper    ǀ Paper
            Scissors ǀ Scissors ǀ null
            Scissors ǀ Paper    ǀ Scissors
            Scissors ǀ Rock     ǀ Rock
            Paper    ǀ Paper    ǀ null
            Paper    ǀ Rock     ǀ Paper
            Paper    ǀ Scissors ǀ Scissors
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
