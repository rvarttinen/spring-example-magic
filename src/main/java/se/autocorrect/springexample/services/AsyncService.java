package se.autocorrect.springexample.services;

import java.util.concurrent.CompletableFuture;

import org.apache.jena.rdf.model.Model;

public interface AsyncService {

	CompletableFuture<Model> listMagic() throws InterruptedException;
	
	CompletableFuture<Model> listWikiDataMagic(String key) throws InterruptedException;
}
