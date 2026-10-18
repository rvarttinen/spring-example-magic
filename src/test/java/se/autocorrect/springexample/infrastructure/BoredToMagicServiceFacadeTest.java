package se.autocorrect.springexample.infrastructure;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

import java.util.Optional;

import org.apache.jena.query.Query;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.google.common.hash.BloomFilter;

import se.autocorrect.springexample.store.MagicResultProcessor;
import se.autocorrect.springexample.store.TripleStore;


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
    }
}
