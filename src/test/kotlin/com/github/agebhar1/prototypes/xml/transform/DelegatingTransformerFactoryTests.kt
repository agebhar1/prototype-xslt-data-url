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
import io.mockk.mockk
import io.mockk.verify
import javax.xml.transform.ErrorListener
import javax.xml.transform.Source
import javax.xml.transform.TransformerFactory
import javax.xml.transform.URIResolver
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DelegatingTransformerFactoryTests {

  private val delegate: TransformerFactory = mockk(relaxed = true)
  private val source: Source = mockk()

  @BeforeEach
  fun doBeforeEachTest() {
    clearMocks(delegate, source)
  }

  @Test
  fun `should delegate newTransformer()`() {

    val factory = DelegatingTransformerFactory(delegate)

    factory.newTransformer()

    verify { delegate.newTransformer() }
  }

  @Test
  fun `should delegate newTransformer(Source)`() {

    val factory = DelegatingTransformerFactory(delegate)

    factory.newTransformer(source)

    verify { delegate.newTransformer(eq(source)) }
  }

  @Test
  fun `should delegate newTemplates(Source)`() {

    val factory = DelegatingTransformerFactory(delegate)

    factory.newTemplates(source)

    verify { delegate.newTemplates(eq(source)) }
  }

  @Test
  fun `should delegate getAssociatedStylesheet(Source, String, String, String)`() {

    val factory = DelegatingTransformerFactory(delegate)

    val media = "any media"
    val title = "any title"
    val charset = "any charset"

    factory.getAssociatedStylesheet(source, media, title, charset)

    verify { delegate.getAssociatedStylesheet(eq(source), eq(media), eq(title), eq(charset)) }
  }

  @Test
  fun `should delegate setURIResolver(URIResolver)`() {

    val factory = DelegatingTransformerFactory(delegate)

    val resolver = mockk<URIResolver>()

    factory.setURIResolver(resolver)

    verify { delegate.setURIResolver(eq(resolver)) }
  }

  @Test
  fun `should delegate getURIResolver()`() {

    val factory = DelegatingTransformerFactory(delegate)

    factory.getURIResolver()

    verify { delegate.getURIResolver() }
  }

  @Test
  fun `should delegate setFeature(String, Boolean)`() {

    val factory = DelegatingTransformerFactory(delegate)

    val name = "any feature"

    factory.setFeature(name, true)

    verify { delegate.setFeature(eq(name), any()) }
  }

  @Test
  fun `should delegate getFeature(String)`() {

    val factory = DelegatingTransformerFactory(delegate)

    val name = "any feature"

    factory.getFeature(name)

    verify { delegate.getFeature(eq(name)) }
  }

  @Test
  fun `should delegate setAttribute(String, Any)`() {

    val factory = DelegatingTransformerFactory(delegate)

    val name = "any attribute"
    val value = Object()

    factory.setAttribute(name, value)

    verify { delegate.setAttribute(eq(name), eq(value)) }
  }

  @Test
  fun `should delegate getAttribute(String)`() {

    val factory = DelegatingTransformerFactory(delegate)

    val name = "any attribute"

    factory.getAttribute(name)

    verify { delegate.getAttribute(eq(name)) }
  }

  @Test
  fun `should delegate setErrorListener(ErrorListener)`() {

    val factory = DelegatingTransformerFactory(delegate)

    val listener = mockk<ErrorListener>()

    factory.setErrorListener(listener)

    verify { delegate.setErrorListener(eq(listener)) }
  }

  @Test
  fun `should delegate getErrorListener(String)`() {

    val factory = DelegatingTransformerFactory(delegate)

    factory.getErrorListener()

    verify { delegate.getErrorListener() }
  }
}
