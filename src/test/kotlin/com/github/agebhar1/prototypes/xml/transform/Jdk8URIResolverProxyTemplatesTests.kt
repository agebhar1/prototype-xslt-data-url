/**
 * The MIT License Copyright © 2019 Andreas Gebhardt
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.github.agebhar1.prototypes.xml.transform

import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import javax.xml.transform.Templates
import javax.xml.transform.Transformer
import javax.xml.transform.URIResolver
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class Jdk8URIResolverProxyTemplatesTests {

  private val delegate = mockk<Templates>()
  private val transformer = mockk<Transformer>(relaxed = true)
  private val resolver = mockk<URIResolver>()

  @BeforeEach
  fun doBeforeEachTest() {
    clearMocks(delegate, transformer, resolver)
  }

  @Test
  fun `newTransformer() should return a transformer with default URIResolver if transformers URI resolver is null (JDK8 Bug)`() {

    every { delegate.newTransformer() } returns transformer
    every { transformer.uriResolver } returns null

    val instance = Jdk8URIResolverFixProxyTemplates(delegate, resolver)

    val actual = instance.newTransformer()
    assertThat(actual).isEqualTo(transformer)

    verify {
      transformer.uriResolver
      transformer.uriResolver = eq(resolver)
    }
  }

  @Test
  fun `newTransformer() should return a transformer with provided URIResolver if it is not null (JDK9 and up)`() {

    every { delegate.newTransformer() } returns transformer
    every { transformer.uriResolver } returns resolver

    val instance = Jdk8URIResolverFixProxyTemplates(delegate, resolver)

    val actual = instance.newTransformer()
    assertThat(actual).isEqualTo(transformer)

    verify { transformer.uriResolver }
    verify(exactly = 0) { transformer.uriResolver = any() }
  }
}
