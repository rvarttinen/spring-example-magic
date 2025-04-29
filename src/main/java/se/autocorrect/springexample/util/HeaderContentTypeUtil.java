package se.autocorrect.springexample.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import se.autocorrect.springexample.rdf.LDMediaType;
import se.autocorrect.springexample.rdf.LDMediaTypes;

public class HeaderContentTypeUtil {
	
	private HeaderContentTypeUtil() {}

	public static HttpHeaders calculateLDContentTypeHeader(String accept) {

		var headers = new HttpHeaders();

		switch (accept) {

		case LDMediaTypes.TEXT_TURTLE -> headers.setContentType(LDMediaType.TEXT_TURTLE.asMediaType());
		case LDMediaTypes.JSON_LD -> headers.setContentType(LDMediaType.JSON_LD.asMediaType());
		case LDMediaTypes.RDF_XML -> headers.setContentType(LDMediaType.RDF_XML.asMediaType());
		}

		return headers;
	}
	
	public static HttpHeaders calculateContentTypeHeader(String accept) {

		var headers = new HttpHeaders();

		switch (accept) {

		case MediaType.TEXT_XML_VALUE -> headers.setContentType(MediaType.TEXT_XML);
		case MediaType.APPLICATION_JSON_VALUE -> headers.setContentType(MediaType.APPLICATION_JSON);
		default -> headers.setContentType(MediaType.APPLICATION_JSON);
		}

		return headers;
	}
}
