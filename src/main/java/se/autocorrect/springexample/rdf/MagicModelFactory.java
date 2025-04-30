package se.autocorrect.springexample.rdf;

import java.io.InputStream;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.Lang;

import se.autocorrect.springexample.util.RDFUtils;

class MagicModelFactory {
	
	private static Model magicVocabulary = ModelFactory.createDefaultModel();

	static {

		InputStream resourceAsStream = RDFUtils.class.getResourceAsStream("/magic.ttl");
		magicVocabulary.read(resourceAsStream, Magic.getURI(), Lang.TURTLE.getName());
	}

	static Model createModelFromResouce(Resource resource) {
		
		return switch (resource.getLocalName()) {
		
		case "Magic", "magicId", "magicDescription", "magicType", "originatingType" -> createModelForResource(resource);
		default -> null;
		};
	}

	private static Model createModelForResource(Resource resource) {
		
		Model model = RDFUtils.prepareDefaultModel();
		
		StmtIterator statements = magicVocabulary.listStatements(resource, (Property)null, (RDFNode)null);
		model.add(statements);
		
		return model;
	}
}
