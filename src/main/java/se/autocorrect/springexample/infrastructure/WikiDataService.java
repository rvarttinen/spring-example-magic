package se.autocorrect.springexample.infrastructure;

import java.util.Optional;

import org.apache.jena.rdf.model.Model;

public interface WikiDataService {

	Optional<Model> getWikiDataByKey(String key);
}
