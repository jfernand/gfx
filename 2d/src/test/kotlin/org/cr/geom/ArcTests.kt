package org.cr.geom

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.jupiter.api.Test

internal class ArcTests {
    @Test
    fun testArcs() {
        assertThat(
            "Basic Translation",
            a(
                p(0, 5),
                p(10, 15),
                45,
                90
            ) + p(10, 10),
            equalTo(
                a(p(10, 15), p(20, 25), 45, 90)
            ),
        )
        assertThat(
            "y flip",
            a(
                p(0, 5),
                p(10, 15),
                45,
                90
            ).yFlip(),
            equalTo(
                a(
                    p(0, -5),
                    p(10, -15),
                    45,
                    90
                )
            )
        )
    }
}
