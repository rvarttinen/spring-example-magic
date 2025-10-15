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
package se.autocorrect.springexample.util;

import org.apache.jena.riot.Lang;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.FieldSource;
import se.autocorrect.springexample.rdf.LDMediaType;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class RDFUtilsTest {

    @ParameterizedTest
    @FieldSource("ldMediaTypes")
    void testGetCorrespondingRDFLang(LDMediaType mediaType, Lang expectedLang){

        Optional<Lang> expected = Optional.ofNullable(expectedLang);

        Optional<Lang> actual = RDFUtils.getCorrespondingRDFLang(mediaType);

        assertEquals(expected, actual);
    }

    @Test
    void testGetCorrespondingRDFLangWithNull(){
        assertThrows(NullPointerException.class, () -> RDFUtils.getCorrespondingRDFLang((LDMediaType) null));
    }

    static final Supplier<Stream<Arguments>> ldMediaTypes = () ->

            Stream.of(
                    arguments(LDMediaType.RDF_XML, Lang.RDFXML),
                    arguments(LDMediaType.JSON_LD, Lang.JSONLD),
                    arguments(LDMediaType.TEXT_TURTLE, Lang.TURTLE),
                    arguments(LDMediaType.TRIG, Lang.TRIG),
                    arguments(LDMediaType.TEXT_NQUADS, null) // Not yet supported
            );
}