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
package se.autocorrect.springexample.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MagicStuffMapperTest {

	@Test
	void testMagicStuffToMagicStuffDto() {

		MagicStuff source = new MagicStuff("id",  "magicString", "type");

		MagicStuffMapper eut = MagicStuffMapper.INSTANCE;

		MagicStuffDto actual = eut.magicStuffToMagicStuffDto(source);

		assertAll(() -> assertEquals("id", actual.identity()),
				() -> assertEquals("magicString", actual.magicString()),
				() -> assertEquals("type", actual.typeOfMagic()));
	}

	@Test
	void testMagicStuffDtoToMagicStuff() {

		MagicStuffDto source = new MagicStuffDto("identity", "magicString", "typeOfMagic");

		MagicStuffMapper eut = MagicStuffMapper.INSTANCE;

		MagicStuff actual = eut.magicStuffDtoToMagicStuff(source);

		assertAll(() -> assertEquals("identity", actual.id()),
				() -> assertEquals("magicString", actual.magicString()),
				() -> assertEquals("typeOfMagic", actual.type()));
	}
}
