package se.autocorrect.springexample.infrastructure;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

import java.util.List;
import java.util.Optional;

import org.apache.jena.query.Query;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.junit.jupiter.api.Test;
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
	
	// TODO: Have the RDF related test cases in a separate test class. 
	
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

	@Test
	void testGetExternalMagicByKeyFromExternalSource() {
		
		ExternalMagic expected = ExternalMagic.of("key", "activity", "type");
		
		when(bloomFilter.mightContain(eq("key"))).thenReturn(false);
		when(bloomFilter.put(eq("key"))).thenReturn(true);
		when(externalService.getExternalMagicByKey(eq("key"))).thenReturn(Optional.of(expected));
		
		ExternalMagic actual = eut.getExternalMagicByKey("key").orElseThrow();
		
		assertAll(
				() -> assertEquals(expected, actual),
				() -> verify(bloomFilter).mightContain(eq("key")),
				() -> verify(bloomFilter).put(eq("key")),
				() -> verify(externalService).getExternalMagicByKey(eq("key"))
				);
	}
	
	@Test
	void testGetExternalMagicByKeyFromInternalSourceNotExist() {
		
		ExternalMagic expected = ExternalMagic.of("key", "activity", "type");
		
		when(bloomFilter.mightContain(eq("key"))).thenReturn(true);
		when(bloomFilter.put(eq("key"))).thenReturn(true);
		when(externalService.getExternalMagicByKey(eq("key"))).thenReturn(Optional.of(expected));
		
		when(tripleStore.select(any(Query.class), any(MagicResultProcessor.class))).thenReturn(Optional.empty());
		
		ExternalMagic actual = eut.getExternalMagicByKey("key").orElseThrow();
		
		assertAll(
				() -> assertEquals(expected, actual),
				() -> verify(bloomFilter).mightContain(eq("key")),
				() -> verify(bloomFilter).put(eq("key")),
				() -> verify(externalService).getExternalMagicByKey(eq("key")),
				() -> verify(tripleStore).select(any(Query.class), any(MagicResultProcessor.class))
				);
	}
	
	@Test
	void testGetExternalMagicByKeyFromInternalSourceExist() {
		
		ExternalMagic expected = ExternalMagic.of("key", "activity", "type");
		
		when(bloomFilter.mightContain(eq("key"))).thenReturn(true);
		when(tripleStore.select(any(Query.class), any(MagicResultProcessor.class))).thenReturn(Optional.of(createModelFor(expected)));
		
		ExternalMagic actual = eut.getExternalMagicByKey("key").orElseThrow();
		
		assertAll(
				() -> assertEquals(expected, actual),
				() -> verify(bloomFilter).mightContain(eq("key")),
				() -> verify(tripleStore).select(any(Query.class), any(MagicResultProcessor.class))
				);
	}

	@Test
	void testListAllMagicInTripleStore() {
		
		// TODO: Is this the way to test this "untestable" code: 
		// https://stackoverflow.com/questions/44144205/with-mockito-how-do-i-verify-my-lambda-expression-was-called
		
		when(tripleStore.select(any(Query.class), any(MagicResultProcessor.class))).thenReturn(Optional.empty());
		
		List<ExternalMagic> actual = eut.listAllMagicInTripleStore();
		
		verify(tripleStore).select(any(), any(MagicResultProcessor.class));
	}
	
	private Model createModelFor(ExternalMagic magic) {
		
		Model model = RDFUtils.prepareDefaultModel();
		
		Resource r = model.createResource(Magic.uri + magic.key());
		r.addProperty(RDF.type, Magic.Magic);

		r.addProperty(Magic.magicId, magic.key());

		r.addProperty(Magic.magicType, magic.type());

		r.addProperty(Magic.magicDescription, magic.activity());
		
		return model;
	}
}
