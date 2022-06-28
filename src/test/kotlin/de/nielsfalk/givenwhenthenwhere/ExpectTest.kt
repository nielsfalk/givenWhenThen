package de.nielsfalk.givenwhenthenwhere

import strikt.assertions.isEqualTo

class ExpectTest : GivenWhenThenWhereTest(
    scenario(
        description { "given + when together in expect" },

        expect {
            that(3 + 7).isEqualTo(10)
        }
    ),

    scenario(
        description { "given + when together in expect block but with where - sum of $it should be ${it.first + it.second}" },

        expect {
            that(data.run { first + second }).isEqualTo(10)
        },
        where(
            3 to 7,
            2 to 8,
            1 to 9,
        )
    )
)