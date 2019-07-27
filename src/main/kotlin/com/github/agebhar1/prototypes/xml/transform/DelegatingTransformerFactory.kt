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

import javax.xml.transform.ErrorListener
import javax.xml.transform.Source
import javax.xml.transform.Templates
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.URIResolver

open class DelegatingTransformerFactory(protected val delegate: TransformerFactory) : TransformerFactory() {

	override fun newTransformer(): Transformer? = delegate.newTransformer()

	override fun newTransformer(source: Source?): Transformer? = delegate.newTransformer(source)

	override fun newTemplates(source: Source?): Templates? = delegate.newTemplates(source)

	override fun getAssociatedStylesheet(source: Source?, media: String?, title: String?, charset: String?): Source? =
		delegate.getAssociatedStylesheet(source, media, title, charset)

	override fun setURIResolver(resolver: URIResolver?) = delegate.setURIResolver(resolver)

	override fun getURIResolver(): URIResolver? = delegate.getURIResolver()

	override fun setFeature(name: String?, value: Boolean) = delegate.setFeature(name, value)

	override fun getFeature(name: String?): Boolean = delegate.getFeature(name)

	override fun setAttribute(name: String?, value: Any?) = delegate.setAttribute(name, value)

	override fun getAttribute(name: String?): Any? = delegate.getAttribute(name)

	override fun setErrorListener(listener: ErrorListener?) = delegate.setErrorListener(listener)

	override fun getErrorListener(): ErrorListener? = delegate.getErrorListener()

}