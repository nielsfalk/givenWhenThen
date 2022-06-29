# GivenWhenThe

provides [Spock](https://spockframework.org/)syntax in [Kotlin](https://kotlinlang.org/) with [JUnit](https://junit.org/junit5/) and [Strikt](https://strikt.io/)-assertions


## Example

A Dsl is provided so Testcode can look like this

```kotlin
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
```

Or even [cooler](screenshotShowcase.png), when the markdown-table is syntax-highlighted by IntelliJ

[ShowcaseTest.kt](https://raw.githubusercontent.com/nielsfalk/givenWhenThen/master/src/test/kotlin/de/nielsfalk/givenwhenthen/example/ShowcaseTest.kt)


## Dsl

The Dsl is inspired by [Spock](https://spockframework.org/).
What makes [Spock](https://spockframework.org/) stand out from the crowd is its beautiful and highly expressive specification language. 