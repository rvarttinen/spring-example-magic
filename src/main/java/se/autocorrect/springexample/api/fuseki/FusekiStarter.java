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
