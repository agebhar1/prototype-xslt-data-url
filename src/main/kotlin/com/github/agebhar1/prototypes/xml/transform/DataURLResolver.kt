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

import eu.maxschuster.dataurl.DataUrlSerializer
import java.io.StringReader
import java.nio.charset.Charset
import javax.xml.transform.Source
import javax.xml.transform.URIResolver
import javax.xml.transform.stream.StreamSource

class DataURLResolver : URIResolver {

    private val serializer = DataUrlSerializer()

    override fun resolve(href: String?, base: String?): Source? {

        if (href == null) {
            return null
        }

        val dataUrl = serializer.unserialize(href)
        return StreamSource(
            StringReader(
                String(
                    dataUrl.getData(),
                    Charset.forName(dataUrl.getHeaders()?.get("charset") ?: "UTF-8")
                )
            )
        )
    }
}
