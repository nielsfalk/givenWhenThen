# GivenWhenThe

provides [Spock](https://spockframework.org/)syntax in [Kotlin](https://kotlinlang.org/) with [JUnit](https://junit.org/junit5/) and [Strikt](https://strikt.io/)-assertions

## Dsl

A Dsl is provided so Testcode can look like [this](https://raw.githubusercontent.com/nielsfalk/givenWhenThen/master/src/test/kotlin/de/nielsfalk/givenwhenthen/ShowcaseTest.kt)

```kotlin
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
            data( Rock,     Rock,     expectedWinner = null     ),
            data( Rock,     Scissors, expectedWinner = Rock     ),
            data( Rock,     Paper,    expectedWinner = Paper    ),
            data( Scissors, Scissors, expectedWinner = null     ),
            data( Scissors, Paper,    expectedWinner = Scissors ),
            data( Scissors, Rock,     expectedWinner = Rock     ),
            data( Paper,    Paper,    expectedWinner = null     ),
            data( Paper,    Rock,     expectedWinner = Paper    ),
            data( Paper,    Scissors, expectedWinner = Scissors )
        )
    )
)
```

The Dsl is inspired by [Spock](https://spockframework.org/).
What makes [Spock](https://spockframework.org/) stand out from the crowd is its beautiful and highly expressive specification language. 