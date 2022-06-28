package de.nielsfalk.givenwhenthen

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.stereotype.Component
import strikt.assertions.isNotNull

@SpringBootTest
class SpringTest(
    @Autowired val aComponent: AComponent?
) : GivenWhenThenTest(
    scenario(
        description { "spring component is injected" },
        expect {
            that(aComponent).isNotNull()
        }
    )
)

@SpringBootApplication
open class DemoApplication

@Component
class AComponent