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
package se.autocorrect.springexample.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import se.autocorrect.springexample.infrastructure.BoredToMagicServciceFacade;
import se.autocorrect.springexample.infrastructure.ExternalMagic;
import se.autocorrect.springexample.model.MagicStuff;

@SpringBootTest(classes = {MagicServiceTest.Conf.class}, webEnvironment = NONE)
public class MagicServiceTest {
	
	@Configuration
    @Import({DefaultMagicService.class})
    static class Conf {
    }
	
	@MockitoBean
	BoredToMagicServciceFacade boredToMagicServiceFacade;
	
	@Autowired
	@Qualifier("magicService")
	private MagicService eut;
	
	@Test
	void testListAllMagic() {
		
		MagicStuff expectedStuff = MagicStuff.of("key", "activity", "type");
		ExternalMagic extMagic = ExternalMagic.of("key", "activity", "type");
		
		List<MagicStuff> expected = Collections.singletonList(expectedStuff);
		List<ExternalMagic> magicList = Collections.singletonList(extMagic);
		
		when(boredToMagicServiceFacade.listAllMagicInTripleStore()).thenReturn(magicList);
		
		List<MagicStuff> actual = eut.listAllMagic();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetMagicStuffByKey() {
		
	}
}
