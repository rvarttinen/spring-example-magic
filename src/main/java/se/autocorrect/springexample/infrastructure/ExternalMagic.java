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
package se.autocorrect.springexample.infrastructure;

import se.autocorrect.springexample.model.SemanticMagic;

public record ExternalMagic(
		String key, 
		String activity, 
		String accessibility, 
		String type,
		int participants, 
		double price,
		String link
) implements SemanticMagic{
	
	public static ExternalMagic of(String key, 
			String activity, 
			String accessibility, 
			String type,
			int participants, 
			double price,
			String link) {
		
		return new ExternalMagic(key, activity, accessibility, type, participants, price, link);
	}
	
	public static ExternalMagic of(String key, 
			String activity, 
			String type) {
		
		return new ExternalMagic(key, activity, null, type, 0, 0.0, null);
	}
}
