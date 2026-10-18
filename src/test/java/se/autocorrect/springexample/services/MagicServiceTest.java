package se.autocorrect.springexample.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

import java.util.Collections;
import java.util.List;

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

@SpringBootTest(classes = {MagicServiceTest.Conf.class}, webEnvironment = NONE)
public class MagicServiceTest {
    
    @Configuration
    @Import({DefaultMagicService.class})
    static class Conf {
    }
    
    @MockBean
    BoredToMagicServciceFacade boredToMagicServiceFacade;
    
    @Autowired
    @Qualifier("magicService")
    private MagicService eut;
    
    @Test
    void testListAllMagic() {
        
        MagicStuff expectedStuff = MagicStuff.of("key", "activity", "type");
        ExternalMagic extMagic = ExternalMagic.of("key", "activity", "type");
        
        List<MagicStuff> expected = Collections.singletonList(expectedStuff);
        List<ExternalMagic> magicList = Collections.singletonList(extMagic);
        
        when(boredToMagicServiceFacade.listAllMagicInTripleStore()).thenReturn(magicList);
        
        List<MagicStuff> actual = eut.listAllMagic();
        
        assertEquals(expected, actual);
    }
    
    @Test
    void testGetMagicStuffByKey() {
        
    }
}
