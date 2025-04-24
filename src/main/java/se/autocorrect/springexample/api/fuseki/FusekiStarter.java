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
package se.autocorrect.springexample.api.fuseki;

import org.apache.jena.fuseki.main.FusekiServer;
import org.apache.jena.query.Dataset;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import se.autocorrect.springexample.store.tdb.JenaDataSetHolder;

@Component
public class FusekiStarter {

	@NonNull
	@Qualifier("inMemStore")
	private JenaDataSetHolder tripleStore;

	@Value("${rdf.fuseki.enabled}")
	private boolean fusekiEnabled;
	
	@Value("${rdf.fuseki.port}")
	private int fusekiPort;

	private FusekiServer fusekiServer;

	FusekiStarter(JenaDataSetHolder tripleStore) {
		this.tripleStore = tripleStore;
	}

	@PostConstruct
	void postConstruct() {

		if(fusekiEnabled) {
			
			Dataset dataset = tripleStore.getDataSet(); 
			
			fusekiServer = FusekiServer.create()
		            .port(fusekiPort)
		            .add("/ds", dataset, true)
		            .build();

			fusekiServer.start();
		}
	}

	@PreDestroy
	void preDestroy() {

		if(fusekiServer != null) {
			fusekiServer.stop();
		}
	}
}
