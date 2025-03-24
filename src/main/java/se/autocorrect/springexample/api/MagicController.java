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

import java.util.List;
import java.util.Optional;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import se.autocorrect.springexample.model.MagicStuff;
import se.autocorrect.springexample.rdf.LDMediaType;
import se.autocorrect.springexample.rdf.LDMediaTypes;
import se.autocorrect.springexample.services.MagicRDFService;
import se.autocorrect.springexample.services.MagicService;

@RestController
@RequestMapping("/v1")
public class MagicController {

	private final MagicService magicService;
	private final MagicRDFService magicRdfService;

	MagicController(
			@Qualifier("magicService") MagicService magicService,
			@Qualifier("magicRdfService") MagicRDFService magicRdfService) {
		
		this.magicService = magicService;
		this.magicRdfService = magicRdfService;
	}

	@GetMapping(value = "/magic", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_XML_VALUE })
	public ResponseEntity<?> getAllMagicThereIs(
			@RequestHeader("Accept") String accept,
			@RequestParam("key") Optional<String> key) {

		if (key.isPresent()) {
			return getMagicByKey(accept, key.get());
		}

		return listAllMagic(accept);
	}

	@GetMapping(value = "/magic", produces = { LDMediaTypes.TEXT_TURTLE, LDMediaTypes.RDF_XML, LDMediaTypes.JSON_LD })
	public ResponseEntity<Model> getAllRDFMagicThereIs(
			@RequestHeader("Accept") String accept,
			@RequestParam("key") Optional<String> key) {

		Optional<Model> magicStuff;

		if (key.isPresent()) {

			magicStuff = magicRdfService.getMagicByKey(key.get());

		} else {

			magicStuff = Optional.of(magicRdfService.listAllMagic());
		}

		HttpHeaders headers = calculateLDContentTypeHeader(accept);

		return headers.isEmpty() ? new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE) : createOkReponse(magicStuff, headers);
	}

	private ResponseEntity<MagicStuff> getMagicByKey(String accept, String key) {

		Optional<MagicStuff> magicStuff = magicService.getMagicStuffByKey(key);

		HttpHeaders headers = calculateContentTypeHeader(accept);

		return magicStuff
				.map(stuff -> new ResponseEntity<>(stuff, headers, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
	}

	private ResponseEntity<List<MagicStuff>> listAllMagic(String accept) {

		List<MagicStuff> magicStuffList = magicService.listAllMagic();

		HttpHeaders headers = calculateContentTypeHeader(accept);

		return magicStuffList.isEmpty() ?
				new ResponseEntity<>(HttpStatus.NO_CONTENT) : 
				new ResponseEntity<>(magicStuffList, headers, HttpStatus.OK);
	}
	
	private ResponseEntity<Model> createOkReponse(Optional<Model> magicStuff, HttpHeaders headers) {
		
		Model model = magicStuff.orElse(ModelFactory.createDefaultModel());	
		
		return model.isEmpty() ? new ResponseEntity<Model>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(model, headers, HttpStatus.OK);
	}

	private HttpHeaders calculateContentTypeHeader(String accept) {

		var headers = new HttpHeaders();

		switch (accept) {

		case MediaType.TEXT_XML_VALUE -> headers.setContentType(MediaType.TEXT_XML);
		case MediaType.APPLICATION_JSON_VALUE -> headers.setContentType(MediaType.APPLICATION_JSON);
		default -> headers.setContentType(MediaType.APPLICATION_JSON);
		}

		return headers;
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
