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

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.instanceOf
import org.junit.jupiter.api.Test
import javax.xml.transform.stream.StreamSource

class DataURLResolverPresetTransformerFactoryTests {

	@Test
	fun `a fresh instance's URIResolver should be of type DataURLResolver`() {

		val instance = DataURLResolverPresetTransformerFactory()

		assertThat(instance.getURIResolver(), `is`(instanceOf(DataURLResolver::class.java)))

	}

	@Test
	fun `newTemplate(Source) should return a instance of Jdk8URIResolverFixProxyTemplates`() {

		val instance = DataURLResolverPresetTransformerFactory()

		assertThat(instance.newTemplates(AnySource), `is`(instanceOf(Jdk8URIResolverFixProxyTemplates::class.java)))

	}

	companion object {
		val AnySource = StreamSource(ClassLoader.getSystemResourceAsStream("xslt/stylesheet.xsl"))
	}

}