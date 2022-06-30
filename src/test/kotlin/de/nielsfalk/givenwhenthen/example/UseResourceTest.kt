@file:Suppress("BlockingMethodInNonBlockingContext")

package de.nielsfalk.givenwhenthen.example

import de.nielsfalk.givenwhenthen.*
import strikt.api.Assertion
import strikt.assertions.isA
import strikt.assertions.isNotNull
import java.awt.image.BufferedImage
import java.io.InputStream
import javax.imageio.ImageIO

class UseResourceTest : GivenWhenThenTest(
    scenario(
        description { "a test that uses a resource" },
        `when` {
            UseResourceTest::class.java.getResource("/sample.jpg")
                ?.openStream()
                ?.autoClose()
        },
        then {
            expectActual().isAnImage()
        }
    )
)

fun Assertion.Builder<InputStream?>.isAnImage(): Assertion.Builder<InputStream?> =
    apply {
        isNotNull()
            .get { ImageIO.read(this) }
            .isNotNull()
            .isA<BufferedImage>()
    }