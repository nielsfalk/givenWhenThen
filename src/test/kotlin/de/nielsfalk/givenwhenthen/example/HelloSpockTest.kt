package de.nielsfalk.givenwhenthen.example

import de.nielsfalk.givenwhenthen.*
import strikt.assertions.hasLength

/*
* The <a href="https://github.com/spockframework/spock-example/blob/master/src/test/groovy/HelloSpockSpec.groovy">standard example</a>
* of the spockframework now here
* */
class HelloSpock : GivenWhenThenTest(
    scenario(
        description { "length of Spock's and his friends names" },

        expect {
            that(data.name).hasLength(data.expectedLength)
        },
        where<HelloSpockTestCase> {
            "Spock"  ǀ 5
            "Kirk"   ǀ 4
            "Scotty" ǀ 6
        }
    )
)

data class HelloSpockTestCase(val name:String, val expectedLength:Int)