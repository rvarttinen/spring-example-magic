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

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

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
	
	@MockitoBean
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
		
		when(boredToMagicServiceFacade.getExternalRDFMagicByKey(eq(key))).thenReturn(expected);
		
		Model actual = eut.getMagicByKey(key);
		
		assertEquals(expected, actual);
	}
	
	private Model createModel(List<MagicStuff> someMagicStuff) {

		Model model = RDFUtils.prepareDefaultModel();

		someMagicStuff.forEach(magic -> {

			Resource magicResource = model.createResource(Magic.uri + magic.id());

			magicResource.addProperty(RDF.type, Magic.Magic);
			magicResource.addProperty(Magic.magicId, magic.id());
			magicResource.addProperty(Magic.magicDescription, magic.magicString());

			magicResource.addProperty(DCTerms.title, Magic.Magic.getLocalName() + " " + magic.id());
			magicResource.addProperty(RDFS.label, Magic.Magic.getLocalName() + " " + magic.magicString());
			magicResource.addProperty(Magic.magicType, magic.type());
		});

		return model;
	}
	
	// TODO: move to util package and RDFUtils
    public static StatementDiff getStatementsDiff( final Model expectedModel,  final Model actualModel) {
    	
        final StatementDiff diff = new StatementDiff();
        
        final List<Statement> expectedStatements = new ArrayList<>();
        expectedModel.listStatements().forEachRemaining(expectedStatements::add);

        final List<Statement> actualStatements = new ArrayList<>();
        actualModel.listStatements().forEachRemaining(actualStatements::add);

        expectedStatements
                .stream()
                .filter(expectedStatement -> actualStatements.stream().noneMatch(s -> s.equals(expectedStatement)))
                .forEach(expectedStatement -> diff.getMissingStatements().add(expectedStatement));

        actualStatements
                .stream()
                .filter(actualStatement -> expectedStatements.stream().noneMatch(s -> s.equals(actualStatement)))
                .forEach(actualStatement -> diff.getExcessStatements().add(actualStatement));

        return diff;
    }

    public static class StatementDiff {
    	
        private final List<Statement> missingStatements = new ArrayList<>();
        private final List<Statement> excessStatements = new ArrayList<>();

        public boolean hasDiff() {
            return !missingStatements.isEmpty() || !excessStatements.isEmpty();
        }

        public boolean noDiff() {
			return !hasDiff();
		}

		public List<Statement> getMissingStatements() {
			return missingStatements;
		}

		public List<Statement> getExcessStatements() {
			return excessStatements;
		}

		@Override
        public String toString() {
            if (this.hasDiff()) {
                return ">> missingStatements :: \n" + missingStatements.stream().map(Objects::toString).collect(Collectors.joining("\n")) + "\n\n" +
                        ">> excessStatements :: \n" + excessStatements.stream().map(Objects::toString).collect(Collectors.joining("\n"));
            } else {
                return ">> No diff found";
            }
        }
    }
}
