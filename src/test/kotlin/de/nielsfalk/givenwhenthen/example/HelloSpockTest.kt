package de.nielsfalk.givenwhenthen.example

import de.nielsfalk.givenwhenthen.*
import strikt.assertions.hasLength

/*
* The <a href="https://github.com/spockframework/spock-example/blob/master/src/test/groovy/HelloSpockSpec.groovy">standard example</a>
* of the spockframework now here
* */
class HelloSpock : GivenWhenThenTest(
    scenario(
        description { "length of Spock's and his friends' names" },

        expect {
            val (name, expectedLength) = data
            that(name).hasLength(expectedLength)
        },
        where(
            "Spock" to 5,
            "Kirk" to 4,
            "Scotty" to 6,
        )
    ),
    scenario(
        description { "length of Spock's and his friends' names with tabular data" },

        expect {
            that(data.name).hasLength(data.expectedLength)
        },
        where<HelloSpockTestCase>(
            """
                | name   | expected length |
                |--------|-----------------|
                | Spock  | 5               |
                | Kirk   | 4               |
                | Scotty | 6               |
            """.trimIndent()
        )
    )
)

data class HelloSpockTestCase(val name:String, val expectedLength:Int)