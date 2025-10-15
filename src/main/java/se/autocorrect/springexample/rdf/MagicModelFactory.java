package se.autocorrect.springexample.rdf;

import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import se.autocorrect.springexample.util.RDFUtils;

import java.io.InputStream;

class MagicModelFactory {
	
	private static final Model magicVocabulary = ModelFactory.createDefaultModel();

	static {

		InputStream resourceAsStream = MagicModelFactory.class.getResourceAsStream("/magic.ttl");
		magicVocabulary.read(resourceAsStream, Magic.getURI(), Lang.TURTLE.getName());
	}

	static Model createModelFromResource(Resource resource) {
		
		return switch (resource.getLocalName()) {
		
		case "Magic", "magicId", "magicDescription", "magicType", "originatingType" -> createModelForResource(resource);
		case null, default -> null;
		};
	}

	private static Model createModelForResource(Resource resource) {
		
		Model model = RDFUtils.prepareDefaultModel();
		
		StmtIterator statements = magicVocabulary.listStatements(resource, null, (RDFNode)null);
		model.add(statements);
		
		return model;
	}
}
