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
package se.autocorrect.springexample.infrastructure.rest;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import se.autocorrect.springexample.infrastructure.ExternalMagic;
import se.autocorrect.springexample.infrastructure.ExternalService;
import se.autocorrect.springexample.util.Try;

@Service("externalService")
public class BoredApiRestServiceClient implements ExternalService {
	
    private final RestTemplate restTemplate;
    
    @Value("${bored.api.baseurl:default}")
    private String baseUrl;
    
    BoredApiRestServiceClient(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	@Override
    public Optional<ExternalMagic> getExternalMagic() {

		Try<ResponseEntity<ExternalMagic>> response = Try.of(() -> restTemplate.getForEntity(baseUrl, ExternalMagic.class));

		Optional<ExternalMagic> returnValue = Optional.empty();
		
		if(response.isSuccess()) {
			
			ResponseEntity<ExternalMagic> responseEntity = response.get();
			
			if(responseEntity.getStatusCode() == HttpStatus.OK) {
				returnValue = Optional.of(responseEntity.getBody());
			}
		}
		
        return returnValue;
    }

	@Override
	public Optional<ExternalMagic> getExternalMagicByKey(String key) {
		
		String urlTemplate = UriComponentsBuilder.fromUriString(baseUrl)
		        .queryParam("key", "{key}")
		        .encode()
		        .toUriString();
		
		Map<String, String> params = Collections.singletonMap("key", key);
		
		Try<ResponseEntity<ExternalMagic>> response = Try.of(() -> restTemplate.getForEntity(urlTemplate, ExternalMagic.class, params));

		Optional<ExternalMagic> returnValue = Optional.empty();
		
		if(response.isSuccess()) {
			
			ResponseEntity<ExternalMagic> responseEntity = response.get();
			
			if(responseEntity.getStatusCode() == HttpStatus.OK) {
				returnValue = Optional.of(responseEntity.getBody());
			}
		}
		
        return returnValue;
	}
}
