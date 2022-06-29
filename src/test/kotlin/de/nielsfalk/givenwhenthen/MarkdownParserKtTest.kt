package de.nielsfalk.givenwhenthen

import de.nielsfalk.givenwhenthen.TestEnum.Rock
import org.intellij.lang.annotations.Language
import strikt.assertions.containsExactly

class MarkdownParserKtTest : GivenWhenThenTest(
    scenario(
        description { "parse markdown" },

        given {
            @Language("Markdown")
            val markdown = """
                | enum  | int  | string |
                |-------|------|--------|
                | null  | 1    | null   |
                | Rock  | null | sdfgd  |
                """.trimIndent()
            markdown
        },
        `when` {
            readMd<TestData>(it)
        },
        then {
            expectActual()
                .containsExactly(
                    TestData(null, 1, null),
                    TestData(Rock, null, "sdfgd")
                )
        }
    )
)

data class TestData(val enum: TestEnum?, val int: Int?, val string: String?)

enum class TestEnum {
    Rock
}