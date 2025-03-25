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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withResourceNotFound;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;

import se.autocorrect.springexample.infrastructure.ExternalMagic;

@TestPropertySource(locations="classpath:application-test.properties")
@RestClientTest(ExternalRestServiceClient.class)
class ExternalRestServiceClientTest {

	@Autowired
	private ExternalRestServiceClient client;

	@Autowired
	private MockRestServiceServer server;
	
	
	@Test
	void testGetExternalMagic() {
		
		ExternalMagic expected = ExternalMagic.of("3943506", "Learn Express.js", "0.25", "education", 1, 0.1, "https://expressjs.com/");
		
		this.server.expect(requestTo("https://test/bored/api/activity/")).andRespond(withSuccess(data, MediaType.APPLICATION_JSON));
		
		Optional<ExternalMagic> actualOp = client.getExternalMagic();
		
		assertAll(
				() -> assertTrue(actualOp.isPresent()),
				() -> {
					
					ExternalMagic actual = actualOp.get();
					assertEquals(expected, actual);
				}
				);
		
	}
	
	@Test
	void testGetExternalMagicByKey() {
		
		ExternalMagic expected = ExternalMagic.of("3943506", "Learn Express.js", "0.25", "education", 1, 0.1, "https://expressjs.com/");
		
		this.server.expect(requestTo("https://test/bored/api/activity/?key=key")).andRespond(withSuccess(data, MediaType.APPLICATION_JSON));
		
		Optional<ExternalMagic> actualOp = client.getExternalMagicByKey("key");
		
		assertAll(
				() -> assertTrue(actualOp.isPresent()),
				() -> {
					
					ExternalMagic actual = actualOp.get();
					assertEquals(expected, actual);
				}
				);
	}
	
	@Test
	void testGetExternalMagicServerError() {
		
		server.expect(requestTo("https://test/bored/api/activity/?key=key1")).andRespond(withServerError());
		
		Optional<ExternalMagic> actualOp = client.getExternalMagicByKey("key1");
		
		assertTrue(actualOp.isEmpty());
	}
	
	@Test
	void testGetExternalMagicByKeyNoData() {
		
		server.expect(requestTo("https://test/bored/api/activity/?key=key1")).andRespond(withResourceNotFound());
		
		Optional<ExternalMagic> actualOp = client.getExternalMagicByKey("key1");
		
		assertTrue(actualOp.isEmpty());
	}
	
	
	private String data = """
			{
	"activity": "Learn Express.js",
	"accessibility": 0.25,
	"type": "education",
	"participants": 1,
	"price": 0.1,
	"link": "https://expressjs.com/",
	"key": "3943506"
			} 
			""";
}
