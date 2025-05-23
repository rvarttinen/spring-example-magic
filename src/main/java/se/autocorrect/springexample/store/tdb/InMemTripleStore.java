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
package se.autocorrect.springexample.store.tdb;

import java.io.InputStream;
import java.util.Optional;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.system.Txn;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import se.autocorrect.springexample.model.SemanticMagic;
import se.autocorrect.springexample.rdf.Magic;
import se.autocorrect.springexample.store.MagicResultProcessor;
import se.autocorrect.springexample.store.MagicToRDFConverter;
import se.autocorrect.springexample.store.TripleStore;
import se.autocorrect.springexample.util.Try;

@Component("inMemStore")
public class InMemTripleStore implements TripleStore, JenaDataSetHolder {

	private Dataset dataSet;

	@PostConstruct
	void init() {
		
		this.dataSet = DatasetFactory.createTxnMem();
		addMagicVocabulary();
	}
	
	@PreDestroy
	void tearDown() {
		dataSet.close();
	}
	
	@Override
	public Dataset getDataSet() {
		return dataSet;
	}

	@Override
	public Optional<Model> select(Query query, MagicResultProcessor processor) {
		
		var wrapper = new Object(){ Model model = null; };

		Txn.executeRead(dataSet, () -> 	{
			
			ResultSet resultSet = select(query);
			Model model = processor.processResultSt(resultSet);
			
			wrapper.model = model;
		});

		return Optional.ofNullable(wrapper.model);
	}

	@Override
	public ResultSet select(Query query) {

		var wrapper = new Object(){ ResultSet resultSet; };

		Txn.executeRead(dataSet, () -> {

			QueryExecution qexec = QueryExecutionFactory.create(query, dataSet);
			wrapper.resultSet = qexec.execSelect();
		});

		return wrapper.resultSet;
	}

	@Override
	public void insert(SemanticMagic m, MagicToRDFConverter<?> converter) {
		
		Txn.executeWrite(dataSet, () -> {

			Model model = converter.processMagic(m);
			Model defaultModel = dataSet.getDefaultModel();
			
			defaultModel.add(model);
		});
	}
	
	@Override
	public void update(MagicToRDFConverter<?> converter) {
		// TODO: implement ... 
	}

	@Override
	public void delete(int id) {
		// TODO: implement ... 
	}

	private void addMagicVocabulary() {

		Model defaultModel = dataSet.getDefaultModel();

		InputStream resourceAsStream = InMemTripleStore.class.getResourceAsStream("/magic.ttl");

		Try.of(() -> defaultModel.read(resourceAsStream, Magic.getURI(), Lang.TURTLE.getName()));
	}
}
