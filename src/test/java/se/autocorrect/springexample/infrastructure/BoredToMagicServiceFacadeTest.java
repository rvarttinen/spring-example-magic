package se.autocorrect.springexample.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.jena.query.Query;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.google.common.hash.BloomFilter;

import se.autocorrect.springexample.rdf.Magic;
import se.autocorrect.springexample.store.MagicResultProcessor;
import se.autocorrect.springexample.store.TripleStore;
import se.autocorrect.springexample.util.RDFUtils;

@SpringBootTest(classes = {BoredToMagicServiceFacadeTest.Conf.class}, webEnvironment = NONE)
class BoredToMagicServiceFacadeTest {
	
	@Configuration
    @Import({BoredToMagicServciceFacade.class})
    static class Conf {
    }
	
	@MockitoBean
    private ExternalService externalService;

	@MockitoBean
    private TripleStore tripleStore;

	@MockitoBean
    private BloomFilter<String> bloomFilter;
	
	@Autowired
	@Qualifier("boredToMagicService")
	BoredToMagicServciceFacade eut;

//	@Test
	void testGetExternalMagicByKey() {
	}

//	@Test
	void testGetExternalRDFMagicByKey() {
	}

	@Disabled("... until fixed.!..")
	@Test
	void testListAllMagicInTripleStore() {
		
		ExternalMagic expectedStuff = ExternalMagic.of("key", "activity", "type");
		
		List<ExternalMagic> expected = Collections.singletonList(expectedStuff);
		
		final Model model = RDFUtils.prepareDefaultModel();
		
		// is this the way:
		//https://stackoverflow.com/questions/44144205/with-mockito-how-do-i-verify-my-lambda-expression-was-called
		
		// Add some magic to default model
			
			/*
			 * magic:3943506  rdf:type         magic:Magic;
        magic:magicDescription  "Learn Express.js";
        magic:magicId           "3943506";
        magic:magicType         "internal";
        magic:originatingType   "education" .
			 */
			Resource resource = model.createResource(Magic.uri + "key");
			
			resource.addProperty(Magic.magicId, ResourceFactory.createStringLiteral("key"));
			resource.addProperty(Magic.magicType, ResourceFactory.createStringLiteral("type"));
			resource.addProperty(Magic.magicDescription, ResourceFactory.createStringLiteral("activity"));
		
		ResultSet rsMock = mock(ResultSet.class);
		
		when(tripleStore.select(any(Query.class), any(MagicResultProcessor.class))).thenReturn(Optional.of(model));
		
		ArgumentCaptor<MagicResultProcessor> processorCaptor = ArgumentCaptor.forClass(MagicResultProcessor.class);
		
		List<ExternalMagic> actual = eut.listAllMagicInTripleStore();

		verify(tripleStore).select(any(Query.class), processorCaptor.capture());
		
		MagicResultProcessor value = processorCaptor.getValue();
		
		value.processResultSt(rsMock);
		
		assertEquals(expected, actual);
		
		// TODO: Add verify to correspond the when 
	}
	
	private MagicResultProcessor magicResultProcessorHandlerSpy(MagicResultProcessor processor) {

		// Create a spy of the MagicResultProcessor functional interface itself.
		MagicResultProcessor spy = Mockito.spy(MagicResultProcessor.class);
		
//		Mockito.doAnswer(it -> {
//
//			ResultSet resultSet = (ResultSet) it.getArguments()[1];
//
//			processor.processResultSt(resultSet);
//
//			return null;
//
//		}).when(spy).processResultSt(Mockito.any(ResultSet.class));

		return spy;
	}
}
