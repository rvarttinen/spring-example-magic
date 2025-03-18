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
package se.autocorrect.springexample.util;

import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.springframework.http.MediaType;

import se.autocorrect.springexample.rdf.LDMediaType;
import se.autocorrect.springexample.rdf.LDMediaTypes;
import se.autocorrect.springexample.rdf.Magic;

public final class RDFUtils {
	
	private RDFUtils(){}

	public static Optional<Lang> getCorrespondingRDFLang(LDMediaType ldMediaType){
        return getCorrespondingRDFLang(ldMediaType.asMediaType());
    }

    public static Optional<Lang> getCorrespondingRDFLang(MediaType mediaType){

        Objects.requireNonNull(mediaType);

        Optional<Lang> retVal = Optional.empty();
        String mediaTypeString = mediaType.getType() + "/" + mediaType.getSubtype();

        switch (mediaTypeString){

            case LDMediaTypes.TEXT_TURTLE:
                retVal = Optional.of(Lang.TURTLE);
                break;
            case LDMediaTypes.JSON_LD:
                retVal = Optional.of(Lang.JSONLD);
                break;
            case LDMediaTypes.RDF_XML:
                retVal = Optional.of(Lang.RDFXML);
        }

        return retVal;
    }
    
    public static Optional<Model> prepareDefaultModel() {

        Model defaultModel = ModelFactory.createDefaultModel();

        InputStream resourceAsStream = RDFUtils.class.getResourceAsStream("/magic.ttl");

        return Try.of( () -> defaultModel.read(resourceAsStream, Magic.getURI(), Lang.TURTLE.getName()) ).toOptional();
    }
}
