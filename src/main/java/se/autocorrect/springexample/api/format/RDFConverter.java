/*-
 *  
 * spring-example-magic
 *  
 * Copyright (C) 2025 Autocorrect Design HB
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
 *  
 */
package se.autocorrect.springexample.api.format;

import java.io.IOException;
import java.lang.reflect.Type;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.impl.ModelCom;
import org.apache.jena.riot.RDFDataMgr;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.server.ServletServerHttpResponse;

import se.autocorrect.springexample.rdf.LDMediaType;
import se.autocorrect.springexample.util.RDFUtils;

public class RDFConverter extends AbstractGenericHttpMessageConverter<Model> {

	public RDFConverter() {
		 super(LDMediaType.TEXT_TURTLE.asMediaType(), LDMediaType.JSON_LD.asMediaType(), LDMediaType.RDF_XML.asMediaType());
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		return clazz.isAssignableFrom(Model.class) || clazz.isAssignableFrom(ModelCom.class);
	}
	
	@Override
	protected void writeInternal(Model model, Type type, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {

		if (outputMessage instanceof ServletServerHttpResponse servletServerHttpResponse) {

			var contentType = outputMessage.getHeaders().getContentType();
			var lang = RDFUtils.getCorrespondingRDFLang(contentType);

			RDFDataMgr.write(servletServerHttpResponse.getBody(), model, 
					lang.orElseThrow(() -> new HttpMessageNotWritableException("Not supported content type: " + contentType)));
		}
	}

	@Override
	public Model read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		return null;
	}

	@Override
	protected Model readInternal(Class<? extends Model> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		return null;
	}
}
