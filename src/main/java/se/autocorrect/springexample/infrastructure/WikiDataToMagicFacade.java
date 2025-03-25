package se.autocorrect.springexample.infrastructure;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import se.autocorrect.springexample.store.TripleStore;

@Service("wikiDataToMagicService")
public class WikiDataToMagicFacade {

	@NonNull
	@Qualifier("wikiDataService")
	private WikiDataService wikiDataService;

	@NonNull
	@Qualifier("tdbStore")
	private TripleStore tripleStore;
	

	WikiDataToMagicFacade(WikiDataService wikiDataService, TripleStore tripleStore) {
		this.wikiDataService = wikiDataService;
		this.tripleStore = tripleStore;
	}


	public Optional<ExternalMagic> getEntityData(String key) {

		return Optional.empty();
	}
}
