/*-
 *  
 * spring-example-magic
 *  
 * Copyright (C) 2025 Autocorrect Design HB
 *  
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *  
 */
package se.autocorrect.springexample.api;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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

import se.autocorrect.springexample.rdf.LDMediaTypes;
import se.autocorrect.springexample.services.AsyncService;
import se.autocorrect.springexample.util.HeaderContentTypeUtil;

@Experimental
@RestController
@RequestMapping("/v2")
public class MagicAsyncController {

	public static final String NO_KEY = null;
	
	private final AsyncService asyncService;

    MagicAsyncController(@Qualifier("asyncService") AsyncService asyncService/*, @Qualifier("asyncExecutor") Executor asyncExecutor*/) {
		this.asyncService = asyncService;
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

		return headers.isEmpty() ? new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE) : createOkResponse(union, headers);
	}

	private ResponseEntity<Model> createOkResponse(Model union, HttpHeaders headers) {
		
		return union.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(union, headers, HttpStatus.OK);
	}

	private Model combineResults(Model listModel, Model wikiDataModel) {
		
		Model union = ModelFactory.createDefaultModel();
		
		union.add(listModel);
		union.add(wikiDataModel);
		
		return union;
	}
	
	private HttpHeaders calculateLDContentTypeHeader(String accept) {

        return HeaderContentTypeUtil.calculateLDContentTypeHeader(accept);
	}
}