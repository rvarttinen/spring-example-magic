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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import se.autocorrect.springexample.model.MagicStuff;
import se.autocorrect.springexample.services.MagicRDFService;
import se.autocorrect.springexample.services.MagicService;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = {MagicControllerTest.Conf.class}, webEnvironment = MOCK)
class MagicControllerTest {

	 private static final String BEARER_TESTBEARERTOKEN = "Bearer testbearertoken";

	    private static final String URL_TEMPLATE = "/v1";

	    @Configuration
	    @EnableAutoConfiguration
	    @Import({MagicController.class})
	    static class Conf{
	    }

	    @Autowired
	    private MockMvc mockMvc;

	    @MockitoBean
	    @Qualifier("magicService")
	    private MagicService magicServiceMock;

	    @MockitoBean
	    @Qualifier("magicRdfService")
	    private MagicRDFService magicRdfServiceMock;
	    
	    @Test
	    @DisplayName("Test retrieving all magic there is - no element")
	    void testListAllMagicEmpty() throws Exception {

	        when(magicServiceMock.listAllMagic()).thenReturn(Collections.emptyList());

	        MockHttpServletResponse response = this.mockMvc.perform(get(URL_TEMPLATE + "/magic")
	                        .headers(getHeaders()))
	                .andExpect(status().isNoContent())
	                .andReturn()
	                .getResponse()
	                ;

	        assertAll(
	                () -> verify(magicServiceMock).listAllMagic(),
	                () -> assertEquals(Collections.emptyList().size(), response.getContentLength())
	        );
	    }

	    @Test
	    @DisplayName("Test retrieving all magic there is - one element")
	    void testListAllMagicOneMagicElement() throws Exception {
	    	
			MagicStuff magicStuff = new MagicStuff("id", "magic", "type");
	        		
			when(magicServiceMock.listAllMagic()).thenReturn(Collections.singletonList(magicStuff));

	        MockHttpServletResponse response = this.mockMvc.perform(get(URL_TEMPLATE + "/magic")
	                        .headers(getHeaders()))
	                .andExpect(status().isOk())
	                .andReturn()
	                .getResponse()
	                ;

	        assertAll(
	                () -> verify(magicServiceMock).listAllMagic(),
	                () -> assertEquals(Collections.emptyList().size(), response.getContentLength())
	        );
	    }

	    private HttpHeaders getHeaders() {
	    	
	        return new HttpHeaders() {{
	            set("Authorization", BEARER_TESTBEARERTOKEN);
	            setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
	        }};
	    }
}
