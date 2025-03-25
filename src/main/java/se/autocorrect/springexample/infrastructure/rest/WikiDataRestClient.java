package se.autocorrect.springexample.infrastructure.rest;

import java.util.Optional;

import org.apache.jena.rdf.model.Model;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import se.autocorrect.springexample.infrastructure.WikiDataService;

@Service("wikiDataService")
public class WikiDataRestClient implements WikiDataService {
	
	private final RestTemplate restTemplate;
    
    @Value("${wikidata.api.baseurl}")
    private String baseUrl;
    
	WikiDataRestClient(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

// Special:EntityData/Q42

	@Override
	public Optional<Model> getWikiDataByKey(String key) {
		return Optional.empty();
	}
}
