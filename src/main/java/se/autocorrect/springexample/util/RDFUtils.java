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

import java.util.Objects;
import java.util.Optional;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
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
    
    public static Model prepareDefaultModel() {

        Model defaultModel = ModelFactory.createDefaultModel();
        
        defaultModel.setNsPrefix("xs", "http://www.w3.org/2001/XMLSchema#");
        defaultModel.setNsPrefix("owl", OWL.getURI());
        defaultModel.setNsPrefix("rdf", RDF.getURI());
        defaultModel.setNsPrefix("rdfs", RDFS.getURI());
        defaultModel.setNsPrefix("dc", DCTerms.getURI());
        
        defaultModel.setNsPrefix("magic", Magic.getURI());

        return defaultModel;
    }
}
