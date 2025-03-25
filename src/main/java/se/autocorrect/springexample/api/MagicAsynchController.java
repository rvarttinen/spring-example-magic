package se.autocorrect.springexample.api;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.mapstruct.util.Experimental;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import se.autocorrect.springexample.rdf.LDMediaType;
import se.autocorrect.springexample.rdf.LDMediaTypes;
import se.autocorrect.springexample.services.AsyncService;

@Experimental
@RestController
@RequestMapping("/v2")
public class MagicAsynchController {

	public static final String NO_KEY = null;
	
	private final AsyncService asyncService;
	private final Executor asyncExecutor;

	MagicAsynchController(@Qualifier("asyncService") AsyncService asyncService, @Qualifier("asyncExecutor") Executor asyncExecutor) {
		this.asyncService = asyncService;
		this.asyncExecutor = asyncExecutor;
	}

	@GetMapping(value = "/magic", produces = { LDMediaTypes.TEXT_TURTLE, LDMediaTypes.RDF_XML, LDMediaTypes.JSON_LD })
	public ResponseEntity<Model> getMagicAsync(
			@RequestParam("key") Optional<String> key, 
			@RequestHeader("Accept") String accept) throws InterruptedException, ExecutionException {

		CompletableFuture<Model> listFuture = key.isPresent() ? CompletableFuture.completedFuture(ModelFactory.createDefaultModel())  : asyncService.listMagic();
		CompletableFuture<Model> wikiDataFuture = asyncService.listWikiDataMagic(key.orElse(NO_KEY));

		CompletableFuture.allOf(listFuture, wikiDataFuture).join();
		
		Model union = combineResults(listFuture.get(), wikiDataFuture.get());
		
		HttpHeaders headers = calculateLDContentTypeHeader(accept);

		return headers.isEmpty() ? new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE) : createOkReponse(union, headers);
	}

	private ResponseEntity<Model> createOkReponse(Model union, HttpHeaders headers) {
		
		return union.isEmpty() ? new ResponseEntity<Model>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(union, headers, HttpStatus.OK);
	}

	private Model combineResults(Model listModel, Model wikiDataModel) {
		
		Model union = ModelFactory.createDefaultModel();
		
		union.add(listModel);
		union.add(wikiDataModel);
		
		return union;
	}
	
	private HttpHeaders calculateLDContentTypeHeader(String accept) {
		
		var headers = new HttpHeaders();

		switch (accept) {

		case LDMediaTypes.TEXT_TURTLE -> headers.setContentType(LDMediaType.TEXT_TURTLE.asMediaType());
		case LDMediaTypes.JSON_LD -> headers.setContentType(LDMediaType.JSON_LD.asMediaType());
		case LDMediaTypes.RDF_XML -> headers.setContentType(LDMediaType.RDF_XML.asMediaType());
		}

		return headers;
	}
}