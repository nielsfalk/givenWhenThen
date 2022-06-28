package de.nielsfalk.givenwhenthen

import de.nielsfalk.givenwhenthen.RockPaperScissors.*
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class ShowcaseTest : GivenWhenThenTest(
    scenario(
        description {
            "expect ${data.expectedWinner} to win when ${data.first} defending ${data.second}"
        },
        `when` {
            data.first defend data.second
        },
        then {
            expectThat(actual)
                .isEqualTo(data.expectedWinner)
        },
        where(
            data(Rock, Rock, expectedWinner = null),
            data(Rock, Scissors, expectedWinner = Rock),
            data(Rock, Paper, expectedWinner = Paper),
            data(Scissors, Scissors, expectedWinner = null),
            data(Scissors, Paper, expectedWinner = Scissors),
            data(Scissors, Rock, expectedWinner = Rock),
            data(Paper, Paper, expectedWinner = null),
            data(Paper, Rock, expectedWinner = Paper),
            data(Paper, Scissors, expectedWinner = Scissors)
        )
    )
)

private fun data(first: RockPaperScissors, second: RockPaperScissors, expectedWinner: RockPaperScissors?): RockPaperScissorsTestCase {
    return RockPaperScissorsTestCase(first, second, expectedWinner)
}

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

