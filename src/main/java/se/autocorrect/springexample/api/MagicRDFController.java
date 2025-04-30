package se.autocorrect.springexample.api;

import java.util.Optional;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import se.autocorrect.springexample.rdf.LDMediaTypes;
import se.autocorrect.springexample.rdf.Magic;
import se.autocorrect.springexample.util.HeaderContentTypeUtil;

@RestController
@RequestMapping("/**")
public class MagicRDFController {
	
	private ResponseEntity<Model> responseEntity;

	@GetMapping(value = "{path}", produces = {LDMediaTypes.TEXT_TURTLE, LDMediaTypes.RDF_XML, LDMediaTypes.JSON_LD} )
	public ResponseEntity<Model> getMagicRef(
			@RequestHeader("Accept") String accept,
			@PathVariable("path") String path,
			HttpServletRequest request) {

		// TODO: the # needs to be encoded to %23 in order for Spring to accept is as part of the path and an regular HTML anchor ...
		// TODO: parse path retrieve whatever is after any #
		
		responseEntity = ResponseEntity.notFound().build();
		
		if(path != null && path.contains("#")) {
			
			String[] splitPath = path.split("#");
			
			if(splitPath.length == 2 && splitPath[0].equals("magic")) {

				// Ok - we have a potential property of the vocabulary to match in the second element
				Optional<Resource> resourceOp = Magic.getResourceByName(splitPath[1]);
				
				resourceOp.ifPresent(resource -> {
					
					Optional<Model> modelOp = Magic.getModelByResource(resource);
					
					HttpHeaders headers = HeaderContentTypeUtil.calculateLDContentTypeHeader(accept);
					
					modelOp.ifPresent(model -> {
						
						responseEntity = ResponseEntity
								.status(HttpStatus.OK)
								.headers(headers)
								.body(model);
					});
				});
				
			}
		}
		
		return responseEntity;
	}
}
