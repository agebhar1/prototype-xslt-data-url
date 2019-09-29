/**
 * The MIT License
 * Copyright © 2019 Andreas Gebhardt
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.agebhar1.prototypes.xml.transform

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import javax.xml.transform.Templates
import javax.xml.transform.Transformer
import javax.xml.transform.URIResolver

class Jdk8URIResolverProxyTemplatesTests {

    @Test
    fun `newTransformer() should return a transformer with default URIResolver if transformers URI resolver is null (JDK8 Bug)`() {

        val delegate = mockk<Templates>()
        val transformer = mockk<Transformer>(relaxed = true)
        val resolver = mockk<URIResolver>()

        every { delegate.newTransformer() } returns transformer
        every { transformer.getURIResolver() } returns null

        val instance = Jdk8URIResolverFixProxyTemplates(delegate, resolver)

        val actual = instance.newTransformer()
        assertThat(actual, `is`(equalTo(transformer)))

        verify {
            transformer.getURIResolver()
            transformer.setURIResolver(eq(resolver))
        }
    }

    @Test
    fun `newTransformer() should return a transformer with provided URIResolver if it is not null (JDK9 and up)`() {

        val delegate = mockk<Templates>()
        val transformer = mockk<Transformer>()
        val resolver = mockk<URIResolver>()

        every { delegate.newTransformer() } returns transformer
        every { transformer.getURIResolver() } returns resolver

        val instance = Jdk8URIResolverFixProxyTemplates(delegate, resolver)

        val actual = instance.newTransformer()
        assertThat(actual, `is`(equalTo(transformer)))

        verify { transformer.getURIResolver() }
        verify(exactly = 0) {
            transformer.setURIResolver(any())
        }
    }
}
