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

import java.net.MalformedURLException
import javax.xml.transform.Source
import javax.xml.transform.stream.StreamSource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DataURLResolverTests {

  private val resolver = DataURLResolver()

  @Test
  fun `resolve 'null' href should return 'null'`() {
    assertThat(resolver.resolve(null, null)).isNull()
  }

  @Test
  fun `resolve empty href should throw MalformedURLException`() {
    val exception = assertThrows<MalformedURLException> { resolver.resolve("", null) }
    assertThat(exception).hasMessage("Wrong protocol")
  }

  @Test
  fun `resolve non data URL should throw MalformedURLException`() {
    val exception =
        assertThrows<MalformedURLException> { resolver.resolve("https://github.com/", null) }
    assertThat(exception).hasMessage("Wrong protocol")
  }

  /**
   * RFC 2397
   * 3. Syntax
   *
   * ```
   * 		dataurl    := "data:" [ mediatype ] [ ";base64" ] "," data
   * 		mediatype  := [ type "/" subtype ] *( ";" parameter )
   * 		data       := *urlchar
   * 		parameter  := attribute "=" value
   * ```
   */
  @Test
  fun `resolve data URL with invalid 'encoding' should throw MalformedURLException`() {
    val exception =
        assertThrows<MalformedURLException> {
          resolver.resolve("data:plain/text;base32,JBSWY3DPEBFW65DMNFXAU===", null)
        }
    assertThat(exception).hasMessage("Unknown encoding \"base32\"")
  }

  @Test
  fun `resolve data URL with empty data should be successful`() {
    val actual = resolver.resolve("data:plain/text;base64,", null).asString()
    assertThat(actual).isEqualTo("")
  }

  @Test
  fun `resolve data URL with Base64 decoded data should return a StreamSource with encoded data`() {
    val actual =
        resolver
            .resolve(
                "data:plain/text;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4KPGRvY3VtZW50Lz4K",
                null)
            .asString()
    assertThat(actual).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<document/>\n")
  }

  @Test
  fun `resolve data URL should encode data by given charset`() {
    val actual = resolver.resolve("data:plain/text;charset=iso-8859-1;base64,tQ==", null).asString()
    assertThat(actual).isEqualTo("µ")
  }

  private fun Source?.asString(): String? =
      this?.let {
        when (it) {
          is StreamSource -> it.reader.readText()
          else -> null
        }
      }
}
