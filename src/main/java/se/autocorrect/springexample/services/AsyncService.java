package se.autocorrect.springexample.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.jena.rdf.model.Model;
import se.autocorrect.springexample.model.MagicStuff;
import se.autocorrect.springexample.rdf.Magic;

public interface AsyncService {

	CompletableFuture<Model> listRDFMagic() throws InterruptedException;
	
	CompletableFuture<Model> listWikiDataMagic(String key) throws InterruptedException;

	CompletableFuture<List<MagicStuff>> listMagic();

	CompletableFuture<MagicStuff> getMagicStuffByKey(String key);
}
