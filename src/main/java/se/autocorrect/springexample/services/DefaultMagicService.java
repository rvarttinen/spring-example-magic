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

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import se.autocorrect.springexample.infrastructure.BoredToMagicServciceFacade;
import se.autocorrect.springexample.infrastructure.ExternalMagic;
import se.autocorrect.springexample.infrastructure.ExternalMagicMapper;
import se.autocorrect.springexample.model.MagicStuff;

@Service("magicService")
class DefaultMagicService implements MagicService {
	
	@NonNull 
	@Qualifier("boredToMagicService")
	private BoredToMagicServciceFacade boredToMagicServiceFacade;
	
    DefaultMagicService(BoredToMagicServciceFacade boredToMagicServiceFacade) {
		this.boredToMagicServiceFacade = boredToMagicServiceFacade;
	}

	@Override
    public List<MagicStuff> listAllMagic() {

		List<ExternalMagic> extMagics = boredToMagicServiceFacade.listAllMagicInTripleStore();

        return ExternalMagicMapper.INSTANCE.map(extMagics);
    }

	@Override
	public Optional<MagicStuff> getMagicStuffByKey(String key) {

		Optional<ExternalMagic> extMagicOp = boredToMagicServiceFacade.getExternalMagicByKey(key);

		return extMagicOp.map(ExternalMagicMapper.INSTANCE::externalMagicToMagicStuff);
	}	
}
