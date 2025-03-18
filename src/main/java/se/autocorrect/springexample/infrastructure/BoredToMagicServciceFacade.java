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
package se.autocorrect.springexample.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.jena.arq.querybuilder.SelectBuilder;
import org.apache.jena.query.Query;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.vocabulary.RDF;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.google.common.hash.BloomFilter;

import se.autocorrect.springexample.rdf.Magic;
import se.autocorrect.springexample.store.TripleStore;
import se.autocorrect.springexample.util.RDFUtils;

/**
 * Retrieves data from the bored service and converts it into magic according to
 * the magic vocabulary.
 * <p>
 * This service uses a Bloom filter to check whether we may have a certain magic
 * entry stored locally or not.
 * </p>
 * 
 * @see Magic
 */
@Service("boredToMagicService")
public class BoredToMagicServciceFacade {

    @NonNull
    @Qualifier("externalService")
    private ExternalService externalService;

    @NonNull
    @Qualifier("tdbStore")
    private TripleStore tripleStore;

    @NonNull
    private BloomFilter<String> bloomFilter;
    
    BoredToMagicServciceFacade(ExternalService externalService, TripleStore tripleStore,
			BloomFilter<String> bloomFilter) {
    	
		this.externalService = externalService;
		this.tripleStore = tripleStore;
		this.bloomFilter = bloomFilter;
	}

	public Optional<ExternalMagic> getExternalMagicByKey(String key) {

        if (bloomFilter.mightContain(key)) {

            Optional<ExternalMagic> intMagicOp = getInternal(key);

            if (intMagicOp.isPresent()) {

                return intMagicOp;
            }
        }

        return getExternal(key);
    }

    public Optional<Model> getExternalRDFMagicByKey(String key) {

        if (bloomFilter.mightContain(key)) {

            Optional<Model> intMagicOp = getInternalRDF(key);

            if (intMagicOp.isPresent()) {

                return intMagicOp;
            }
        }

        return getExternalRDF(key);
    }

    public List<ExternalMagic> listAllMagicInTripleStore() {

        List<ExternalMagic> retVal = new ArrayList<>();
        final Query query = createSelectAllQuery();
        final Optional<Model> defaultModelOp = RDFUtils.prepareDefaultModel();

        tripleStore.select(query, searchResult -> {

            searchResult.forEachRemaining(solution -> {

                var rdfNode = solution.get("magicId");

                String uri = rdfNode.toString();

                rdfNode = solution.get("magicType");
                String type = rdfNode.asLiteral().getString();

                rdfNode = solution.get("magicDescription");
                String descr = rdfNode.asLiteral().getString();

                ExternalMagic resultingMagic = ExternalMagic.of(
                        uri,
                        type,
                        descr);

                retVal.add(resultingMagic);
            });

            return defaultModelOp.orElseThrow();
        });

        return retVal;
    }
	
	private Optional<ExternalMagic> getInternal(String key) {

        Query query = createSelectQueryFindByKey(key);

        Optional<Model> resultOp = tripleStore.select(query, searchResult -> {

            Optional<Model> defaultModelOp = RDFUtils.prepareDefaultModel();

            return getModel(key, defaultModelOp, searchResult);
        });

        if (resultOp.isPresent()) {

            Model resultModel = resultOp.get();
            Resource r = resultModel.getResource(Magic.uri + key);
            Statement s = r.getProperty(Magic.magicId);

            RDFNode objectNode = s.getObject();

            String objectId = key;

            // TODO: Find out what type of node we have on our hands
            if (objectNode.isLiteral()) {
                objectId = objectNode.asLiteral().getString();
            }

            s = r.getProperty(Magic.magicType);
            objectNode = s.getObject();

            String objecttype = null;

            if (objectNode.isLiteral()) {
                objecttype = objectNode.asLiteral().getString();
            }

            s = r.getProperty(Magic.magicDescription);
            objectNode = s.getObject();

            String objectDescr = null;

            if (objectNode.isLiteral()) {
                objectDescr = objectNode.asLiteral().getString();
            }

            ExternalMagic resultMagic = ExternalMagic.of(
                    objectId,
                    objecttype,
                    objectDescr);

            return Optional.of(resultMagic);
        }

        return Optional.empty();
    }
	
	private Optional<ExternalMagic> getExternal(String key) {

        Optional<ExternalMagic> resultOp = externalService.getExternalMagicByKey(key);

        if (resultOp.isPresent()) {

            // Add to the Bloom filter
            bloomFilter.put(key);

            ExternalMagic externalMagic = resultOp.get();

            Optional<Model> defaultModelOp = RDFUtils.prepareDefaultModel();

            defaultModelOp.ifPresent(model -> tripleStore.insert(externalMagic, s -> {

                Resource resource = model.createResource(Magic.uri + key);
                resource.addProperty(RDF.type, Magic.Magic);
                resource.addProperty(Magic.magicId, externalMagic.key());
                resource.addProperty(Magic.magicType, "internal");
                resource.addProperty(Magic.originatingType, externalMagic.type());
                resource.addProperty(Magic.magicDescription, externalMagic.activity());

                return model;
            }));
        }

        return resultOp;
    }
	
	 private Optional<Model> getExternalRDF(String key) {

	        Optional<ExternalMagic> resultOp = externalService.getExternalMagicByKey(key);
	        Optional<Model> defaultModelOp = RDFUtils.prepareDefaultModel();

	        if (resultOp.isPresent()) {
	        	
	            // Add to the Bloom filter
	            bloomFilter.put(key);

	            ExternalMagic externalMagic = resultOp.get();

	            defaultModelOp.ifPresent(model -> tripleStore.insert(externalMagic, s -> {

	                Resource resource = model.createResource(Magic.uri + key);
	                resource.addProperty(RDF.type, Magic.Magic);
	                resource.addProperty(Magic.magicId, externalMagic.key());
	                resource.addProperty(Magic.magicType, "internal");
	                resource.addProperty(Magic.originatingType, externalMagic.type());
	                resource.addProperty(Magic.magicDescription, externalMagic.activity());

	                return model;
	            }));
	        }

	        return defaultModelOp;
	    }
	
	 private Optional<Model> getInternalRDF(String key) {

	        Query query = createSelectQueryFindByKey(key);
	        Optional<Model> defaultModelOp = RDFUtils.prepareDefaultModel();

	        tripleStore.select(query, searchResult -> getModel(key, defaultModelOp, searchResult));

	        return defaultModelOp;
	    }
	
	private Model getModel(String key, Optional<Model> defaultModelOp, ResultSet searchResult) {

        searchResult.forEachRemaining(solution -> {

            Resource r = defaultModelOp.orElseThrow().createResource(Magic.uri + key);
            r.addProperty(RDF.type, Magic.Magic);

            var rdfNode = solution.get("magicId");
            r.addProperty(Magic.magicId, rdfNode);

            rdfNode = solution.get("magicType");
            r.addProperty(Magic.magicType, rdfNode);

            rdfNode = solution.get("magicDescription");
            r.addProperty(Magic.magicDescription, rdfNode);
        });

        return defaultModelOp.orElseThrow();
    }
	
	 private Query createSelectQueryFindByKey(String key) {

	        return new SelectBuilder()
	                .setDistinct(true)
	                .addPrefix(Magic.prefix, Magic.uri)
	                .addVar(Var.alloc("magicId"))
	                .addVar(Var.alloc("magicType"))
	                .addVar(Var.alloc("magicDescription"))
	                .addVar(Var.alloc("originatingType"))

	                .addWhere("?magic", RDF.type, Magic.Magic)
	                .addWhere("?magicId", Magic.magicId, "\"" + key + "\"")
	                .addWhere("?magic", Magic.magicType, "?magicType")
	                .addWhere("?magic", Magic.magicDescription, "?magicDescription")
	                .addWhere("?magic", Magic.originatingType, "?originatingType")

	                .build();
	    }

	    private Query createSelectAllQuery() {

	        return new SelectBuilder()
	                .setDistinct(true)
	                .addPrefix(Magic.prefix, Magic.uri)
	                .addVar(Var.alloc("magicId"))
	                .addVar(Var.alloc("magicType"))
	                .addVar(Var.alloc("magicDescription"))
	                .addVar(Var.alloc("originatingType"))

	                .addWhere("?magic", RDF.type, Magic.Magic)
	                .addWhere("?magic", Magic.magicId, "?magicId")
	                .addWhere("?magic", Magic.magicType, "?magicType")
	                .addWhere("?magic", Magic.magicDescription, "?magicDescription")
	                .addWhere("?magic", Magic.originatingType, "?originatingType")

	                .build();
	    }
}
