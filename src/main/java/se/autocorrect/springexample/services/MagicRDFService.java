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

import org.apache.jena.rdf.model.Model;

/**
 * A service for retrieving known {@code MagicStuff} with its semantics. 
 */
public interface MagicRDFService {

	/**
	 * List all known RDF {@code MagicStuff}. As the data is "packed" into a graph,
	 * {@code Model}, it is up to the client to extract data of interest.
	 * 
	 * @return all known {@code MagicStuff}
	 */
    Model listAllMagic();

    /**
     * Retrieve a RDF {@code MagicStuff} by its key. As the data is "packed" into a graph,
	 * {@code Model}, it is up to the client to extract data of interest.
	 * 
	 * @param key the key 
	 * @return an {@code Optional} holding the RDF {@code MagicStuff} if present, an {@code Optional#empty()} otherwise
     */
    Model getMagicByKey(String key);
}
