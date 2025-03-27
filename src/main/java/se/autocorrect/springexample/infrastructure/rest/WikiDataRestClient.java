package se.autocorrect.springexample.infrastructure.rest;

import java.net.URI;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.mapstruct.util.Experimental;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import se.autocorrect.springexample.infrastructure.WikiDataService;
import se.autocorrect.springexample.rdf.LDMediaTypes;

@Experimental
@Service("wikiDataService")
public class WikiDataRestClient implements WikiDataService {
	
	private final RestTemplate restTemplate;
    
    @Value("${wikidata.api.baseurl:default}")
    private String baseUrl;
    
	WikiDataRestClient(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	@Override
	public Optional<Model> getWikiDataByKey(String key) {
		
		URI uri = URI.create(baseUrl + "Special:EntityData/" + key);
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", LDMediaTypes.TEXT_TURTLE);
		
		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
		
		Model model = null;
		
		if(response.getStatusCode().is2xxSuccessful()) {
			
			model = ModelFactory.createDefaultModel();
			model.read(IOUtils.toInputStream(response.getBody(), "UTF-8"), null, Lang.TTL.getLabel());
		}
		
		return Optional.ofNullable(model);
	}
}
