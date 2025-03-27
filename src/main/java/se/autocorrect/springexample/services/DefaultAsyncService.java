package se.autocorrect.springexample.services;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.mapstruct.util.Experimental;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import se.autocorrect.springexample.api.MagicAsynchController;
import se.autocorrect.springexample.infrastructure.BoredToMagicServciceFacade;
import se.autocorrect.springexample.infrastructure.ExternalMagic;
import se.autocorrect.springexample.infrastructure.WikiDataToMagicFacade;
import se.autocorrect.springexample.rdf.Magic;
import se.autocorrect.springexample.util.RDFUtils;

@Experimental
@Service("asyncService")
class DefaultAsyncService implements AsyncService {

	@NonNull
	@Qualifier("boredToMagicService")
	private final BoredToMagicServciceFacade boredToMagicServiceFacade;

	@NonNull
	@Qualifier("wikiDataToMagicService")
	private final WikiDataToMagicFacade wikiDataToMagicFacade;

	DefaultAsyncService(BoredToMagicServciceFacade boredToMagicServiceFacade,
			WikiDataToMagicFacade wikiDataToMagicFacade) {
		this.boredToMagicServiceFacade = boredToMagicServiceFacade;
		this.wikiDataToMagicFacade = wikiDataToMagicFacade;
	}

	@Override
	public CompletableFuture<Model> listMagic() throws InterruptedException {

		return CompletableFuture.completedFuture(listAllKnownMagic());
	}

	@Override
	public CompletableFuture<Model> listWikiDataMagic(String key) throws InterruptedException {

		CompletableFuture<Model> result;

		if (key != MagicAsynchController.NO_KEY) {

			result = CompletableFuture.completedFuture(getSomeDataFromWikiData(key));

		} else {

			result = CompletableFuture.completedFuture(ModelFactory.createDefaultModel());
		}

		return result;
	}

	private Model listAllKnownMagic() {

		List<ExternalMagic> allMagic = boredToMagicServiceFacade.listAllMagicInTripleStore();

		final Optional<Model> defaultModel = RDFUtils.prepareDefaultModel();

		defaultModel.ifPresent(model -> allMagic.forEach(externalMagic -> convertExtMagicToRDF(model, externalMagic)));

		return defaultModel.orElse(ModelFactory.createDefaultModel());
	}

	private Model getSomeDataFromWikiData(String key) {

		Model model = ModelFactory.createDefaultModel();

		if (key.startsWith("Q")) {

			Optional<ExternalMagic> wikiDataMagic = wikiDataToMagicFacade.getEntityData(key);
			wikiDataMagic.ifPresent(externalMagic -> convertExtMagicToRDF(model, externalMagic));
		}

		return model;
	}

	private void convertExtMagicToRDF(Model model, ExternalMagic externalMagic) {

		Resource magicResource = model.createResource(Magic.uri + externalMagic.key());

		magicResource.addProperty(RDF.type, Magic.Magic);
		magicResource.addProperty(Magic.magicId, externalMagic.key());
		magicResource.addProperty(Magic.magicDescription, externalMagic.activity());

		magicResource.addProperty(DCTerms.title, Magic.Magic.getLocalName() + " " + externalMagic.key());
		magicResource.addProperty(RDFS.label, Magic.Magic.getLocalName() + " " + externalMagic.activity());
		magicResource.addProperty(Magic.magicType, externalMagic.type());
	}

}
