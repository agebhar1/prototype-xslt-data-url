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
package com.github.agebhar1.prototypes

import com.github.agebhar1.prototypes.xml.transform.DataURLResolverPresetTransformerFactory
import eu.maxschuster.dataurl.DataUrlBuilder
import eu.maxschuster.dataurl.DataUrlEncoding
import eu.maxschuster.dataurl.DataUrlSerializer
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import org.springframework.integration.channel.DirectChannel
import org.springframework.integration.channel.QueueChannel
import org.springframework.integration.config.EnableIntegration
import org.springframework.integration.dsl.IntegrationFlows
import org.springframework.integration.dsl.MessageChannels.direct
import org.springframework.integration.dsl.MessageChannels.queue
import org.springframework.integration.support.MessageBuilder
import org.springframework.integration.xml.transformer.XsltPayloadTransformer
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.xmlunit.matchers.CompareMatcher.isIdenticalTo

@ExtendWith(SpringExtension::class)
class FlowTests {

    @Test
    fun `data URL should be resolved on XML transformation`(@Autowired input: DirectChannel, @Autowired output: QueueChannel) {

        val serializer = DataUrlSerializer()

        input.send(
            MessageBuilder
                .withPayload(
                    """
					<?xml version="1.0" encoding="UTF-8"?>
					<document/>
					""".replaceIndent()
                )
                .setHeader(
                    "data",
                    serializer.serialize(
                        DataUrlBuilder()
                            .setMimeType("application/xml")
                            .setHeader("charset", "UTF-8")
                            .setEncoding(DataUrlEncoding.BASE64)
                            .setData(
                                """
								<nodes>
									<node rank="0">
										<node rank="0"/>
									</node>
									<node rank="1"/>
								</nodes>
								""".replaceIndent().toByteArray()
                            )
                            .build()
                    )
                )
                .build()
        )

        val message = output.receive()

        assertThat(
            message?.getPayload(),
            isIdenticalTo(
                """
				<?xml version="1.0" encoding="UTF-8"?>
				<document>
					<node rank="0">
						<node rank="0"/>
					</node>
					<node rank="1"/>
				</document>
				""".replaceIndent()
            ).normalizeWhitespace()
        )
    }

    @TestConfiguration
    @EnableIntegration
    class Configuration {

        @Bean("xml.input")
        fun xmlInputChannel() = direct()

        @Bean("xml.output")
        fun xmlOutputChannel() = queue()

        @Bean
        fun stylesheet(loader: ResourceLoader) = loader.getResource("classpath:xslt/stylesheet.xsl")

        @Bean
        fun flow(resource: Resource) = IntegrationFlows
            .from("xml.input")
            .transform(
                with(
                    XsltPayloadTransformer(
                        resource,
                        DataURLResolverPresetTransformerFactory::class.java.getCanonicalName()
                    )
                ) {
                    setXsltParamHeaders("data")
                    this
                })
            .channel("xml.output")
            .get()
    }
}
