package se.autocorrect.springexample.services;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import se.autocorrect.springexample.infrastructure.BoredToMagicServciceFacade;
import se.autocorrect.springexample.infrastructure.ExternalMagic;
import se.autocorrect.springexample.model.MagicStuff;
import se.autocorrect.springexample.rdf.Magic;
import se.autocorrect.springexample.util.RDFUtils;

@SpringBootTest(classes = {MagicRDFServiceTest.Conf.class}, webEnvironment = NONE)
public class MagicRDFServiceTest {

    @Configuration
    @Import({DefaultRDFMagicService.class})
    static class Conf {
    }
    
    @MockBean
    BoredToMagicServciceFacade boredToMagicServiceFacade;
    
    @Autowired
    @Qualifier("magicRdfService")
    private MagicRDFService eut;
    
    @Test
    void testListAllMagic() {
        
        MagicStuff expectedStuff = MagicStuff.of("key", "activity", "type");
        ExternalMagic extMagic = ExternalMagic.of("key", "activity", "type");
        
        List<ExternalMagic> magicList = Collections.singletonList(extMagic);
        
        when(boredToMagicServiceFacade.listAllMagicInTripleStore()).thenReturn(magicList);
        
        Model actual = eut.listAllMagic();
        
        Model expected = createModel(Collections.singletonList(expectedStuff));
        
        StatementDiff statementsDiff = getStatementsDiff(expected, actual);
        
        assertTrue(statementsDiff.noDiff());
    }
    
    @Test
    void testListAllMagicEmptyResult() {
        
        Model expected = RDFUtils.prepareDefaultModel();
        List<ExternalMagic> magicList = Collections.emptyList();
        
        when(boredToMagicServiceFacade.listAllMagicInTripleStore()).thenReturn(magicList);
        
        Model actual = eut.listAllMagic();
        
        StatementDiff statementsDiff = getStatementsDiff(expected , actual);
        
        assertTrue(statementsDiff.noDiff());
    }

    @Test
    void testGetMagicStuffByKey() {
        
        String key = "key";
        
        MagicStuff expectedStuff = MagicStuff.of("key", "activity", "type");
        Model expected = createModel(Collections.singletonList(expectedStuff));
        
    }
}
