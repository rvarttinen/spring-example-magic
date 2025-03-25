package se.autocorrect.springexample.infrastructure.rest;

import java.util.Optional;

import org.apache.jena.rdf.model.Model;

import se.autocorrect.springexample.infrastructure.WikiDataService;

public class WikiDataRestClient implements WikiDataService {

	@Override
	public Optional<Model> getWikiDataByKey(String key) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}
}
