package se.autocorrect.springexample.infrastructure;

import java.util.Optional;

import org.apache.jena.rdf.model.Model;
import org.mapstruct.util.Experimental;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import se.autocorrect.springexample.infrastructure.rest.WikiDataRestClient;
import se.autocorrect.springexample.store.TripleStore;

@Experimental
@Service("wikiDataToMagicService")
public class WikiDataToMagicFacade {

    private final WikiDataRestClient wikiDataService_1;

	@NonNull
	@Qualifier("wikiDataService")
	private WikiDataService wikiDataService;

	@NonNull
	@Qualifier("tdbStore")
	private TripleStore tripleStore;
	

	WikiDataToMagicFacade(WikiDataService wikiDataService, TripleStore tripleStore, WikiDataRestClient wikiDataService_1) {
		this.wikiDataService = wikiDataService;
		this.tripleStore = tripleStore;
		this.wikiDataService_1 = wikiDataService_1;
	}

	public Optional<ExternalMagic> getEntityData(String key) {

		// TODO: check if key in triple store -> if so use, otherwise query external service ... 
		
		Optional<Model> wikiData = wikiDataService.getWikiDataByKey(key);
		
		wikiData.ifPresent(data -> {
			
			// TODO: store in triple store if not present ..
			// TODO: convert to external magic for consumption
		
		/*
		 ExternalMagic resultingMagic = ExternalMagic.of(
                 uri,
                 type,
                 descr);
*/
			
		});
		
		return Optional.empty();
	}
}
