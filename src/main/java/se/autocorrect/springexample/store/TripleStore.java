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
package se.autocorrect.springexample.store;

import java.util.Optional;

import org.apache.jena.query.Query;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

import se.autocorrect.springexample.model.SemanticMagic;

/**
 * A triple store for storing RDF data. 
 */
public interface TripleStore {

	/*
	 * Usually when dealing with triple stores we have the following operations:
	 *  - select -done
	 *  - construct
	 *  - insert/delete (can be performed in the same query as an update) -done
	 *  - ask
	 *  - describe
	 *
	 * TODO: update the interface to reflect these operations
	 */

	/**
	 * Select, find, an entries in the store.
	 * 
	 * @param query the query to execute 
	 * @param processor a processor for processing data in found entries
	 * @return the result set holding the queried data
	 */
	Optional<Model> select(Query query, MagicResultProcessor processor);
	
	/**
	 * Select, find, an entries in the store. 
	 * 
	 * @param query the query to execute 
	 * @return the result set holding the queried data
	 */
	ResultSet select(Query query);
	
	/**
	 * Insert an entry into the store. 
	 * 
	 * @param magic the magic entry to store
	 * @param converter the converter of the insert data
	 */
	void insert(SemanticMagic magic, MagicToRDFConverter<?> converter);
	
	/**
	 * Update an entry.  
	 * 
	 * @param converter the converter for processing update data
	 */
	void update(MagicToRDFConverter<?> converter);
	
	/**
	 * Delete an entry in the store. 
	 * 
	 * @param id the id, key, of the entry
	 */
	void delete(int id);
}
