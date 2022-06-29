package de.nielsfalk.givenwhenthen

import strikt.assertions.isEqualTo

class ShowcaseTest : GivenWhenThenTest(
    scenario(
        description { "expect ${data.expectedWinner} to win when ${data.first} defending ${data.second}" },

        `when` { data.first defend data.second },
        then {
            expectActual()
                .isEqualTo(data.expectedWinner)
        },
        where<RockPaperScissorsTestCase>(
            """
            | first     | second   | expectedWinner |
            |-----------|----------|----------------|
            | Rock      | Rock     | null           |
            | Rock      | Scissors | Rock           |
            | Rock      | Paper    | Paper          |
            | Scissors  | Scissors | null           |
            | Scissors  | Paper    | Scissors       |
            | Scissors  | Rock     | Rock           |
            | Paper     | Paper    | null           |
            | Paper     | Spock    | Spock          |
            | Paper     | Rock     | Paper          |
            | Paper     | Scissors | Scissors       |
            """.trimIndent()
        )
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
