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
package se.autocorrect.springexample.services;

import java.util.List;
import java.util.Optional;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import se.autocorrect.springexample.infrastructure.BoredToMagicServciceFacade;
import se.autocorrect.springexample.infrastructure.ExternalMagic;
import se.autocorrect.springexample.rdf.Magic;
import se.autocorrect.springexample.util.RDFUtils;

@Service("magicRdfService")
class DefaultRDFMagicService implements MagicRDFService {

	@NonNull
	@Qualifier("boredToMagicService")
	private BoredToMagicServciceFacade boredToMagicServiceFacade;
	
    public DefaultRDFMagicService(BoredToMagicServciceFacade boredToMagicServiceFacade) {
		this.boredToMagicServiceFacade = boredToMagicServiceFacade;
	}

	@Override
    public Model listAllMagic() {

        List<ExternalMagic> allMagic = boredToMagicServiceFacade.listAllMagicInTripleStore();

        final Optional<Model> defaultModel = RDFUtils.prepareDefaultModel();

        defaultModel.ifPresent(model -> allMagic.forEach(externalMagic -> {

            Resource magicResource = model.createResource(Magic.uri + externalMagic.key());
            
            magicResource.addProperty(RDF.type, Magic.Magic);
            magicResource.addProperty(Magic.magicId, externalMagic.key());
            magicResource.addProperty(Magic.magicDescription, externalMagic.activity());

            magicResource.addProperty(DCTerms.title, Magic.Magic.getLocalName() + " " + externalMagic.key());
            magicResource.addProperty(RDFS.label, Magic.Magic.getLocalName() + " " + externalMagic.activity());
            magicResource.addProperty(Magic.magicType, externalMagic.type());
        }));

        return defaultModel.orElse(ModelFactory.createDefaultModel());
    }

    @Override
    public Optional<Model> getMagicByKey(String key) {
    	
    	return boredToMagicServiceFacade.getExternalRDFMagicByKey(key); 
    }
}
