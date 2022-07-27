package org.cr.geom

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.jupiter.api.Test

internal class PointTests {
    @Test
    fun testPoint() {
        assertThat("Trivial Translation", p(0, 0) + p(10, 10), equalTo(p(10, 10)))
        assertThat("Basic Translation", p(-5, -5) + p(10, 10), equalTo(p(5, 5)))
        assertThat("y flip", p(5, -5).yFlip(), equalTo(p(5, 5)))
        assertThat("y flip and translation", p(-5, -5).yFlip() + p(10, -10), equalTo(p(5, -5)))
        assertThat("midpoint", midPoint(p(10, 10), p(20, 20)), equalTo(p(15, 15)))
        assertThat("operator midpoint", (p(10, 10) + p(20, 20)) / 2, equalTo(p(15, 15)))
    }
}
