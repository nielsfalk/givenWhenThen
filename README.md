# GivenWhenThen
![example workflow](https://github.com/nielsfalk/givenWhenThen/actions/workflows/gradle.yml/badge.svg)
[![](https://jitpack.io/v/nielsfalk/givenWhenThen.svg)](https://jitpack.io/#nielsfalk/givenWhenThen)


provides [Spock](https://spockframework.org/)syntax in [Kotlin](https://kotlinlang.org/) with [JUnit](https://junit.org/junit5/), [Strikt](https://strikt.io/)-assertions and testdata defined in markdown.


## Example

A Dsl is provided so Testcode can look like this

```kotlin
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
```

It looks even cooler in IntelliJ

![IntelliJ screenshot](screenshotShowcase.png "Title")

Other examples can be found [here](https://github.com/nielsfalk/givenWhenThen/tree/master/src/test/kotlin/de/nielsfalk/givenwhenthen/example)


## Dsl

The Dsl is inspired by [Spock](https://spockframework.org/).
What makes [Spock](https://spockframework.org/) stand out from the crowd is its beautiful and highly expressive specification language.


## Getting started

Add the following to your ```build.gradle.kts```

```kotlin
repositories {
    maven { setUrl("https://jitpack.io") }
}

dependencies {
    testImplementation("com.github.nielsfalk:givenWhenThen:0+")
    implementation("io.strikt:strikt-core:0.34.1")
}
```

or follow the Instructions on [jitpack](https://jitpack.io/#nielsfalk/givenWhenThen)

and write your first Test

```kotlin
import de.nielsfalk.givenwhenthen.*

class FirstTest : GivenWhenThenTest(
    scenario(
        description { "first test" },

        given { },
        `when` { },
        then { expectActual().isEqualTo(Unit) },
        where(Unit)
    )
)
```

[<div align="right"><img src="KotlinMascot.png" width="50%" /></div>](https://kotlinlang.org/docs/kotlin-mascot.html)
