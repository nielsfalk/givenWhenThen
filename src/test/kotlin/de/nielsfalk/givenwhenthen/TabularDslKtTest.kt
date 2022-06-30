package de.nielsfalk.givenwhenthen

import strikt.assertions.containsExactly

class TabularDslKtTest : GivenWhenThenTest(
    scenario(
        description { "read tabular data" },
        `when` {
            readTabularData<Triple<String, Int, Boolean>> {
                // @formatter:off
                "foo"      ǀ  1 ǀ true
                "bar buzz" ǀ 15 ǀ false
                // @formatter:on
            }.toList()
        },
        then {
            expectActual().containsExactly(
                Triple("foo", 1, true),
                Triple("bar buzz", 15, false)
            )
        }
    )
)
