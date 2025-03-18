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
package se.autocorrect.springexample.rdf;

import java.util.Arrays;

import org.springframework.http.MediaType;


/**
 * Represents the media types available for semantic web/linked data.
 * <p>Formats for human consumption; e.g. Turtle, are far more readable than
 * e.g N-triples or N-quads as the latter are optimized for serialization.
 * They are not human readable, even at moderate scale.</p>
 * <p>JSON-LD is popular, but a bit cumbersome and not that space efficient as e.g.
 * Turtle. Although readable by humans, it is primarily used for Front End clients
 * or the like requiring data transported as JSON.</p>
 * 
 * @see LDMediaTypes
 */
public enum LDMediaType {

	 /**
     * The RDF/XML format ("application/rdf+xml").
     */
    RDF_XML(LDMediaTypes.RDF_XML),

    /**
     * The Terse RDF Triple Language a.k.a "Turtle" ("text/turtle").
     */
    TEXT_TURTLE(LDMediaTypes.TEXT_TURTLE),

    /**
     * The Notation 3 (also known as N3) format ("text/n3").
     */
    TEXT_N_TRIPLES(LDMediaTypes.TEXT_N_TRIPLES),

    /**
     * The N-triples format, a subset of the Turtle. ("text/n3")
     */
    N_TRIPLES(LDMediaTypes.APPLICATION_N_TRIPLES),

    /**
     * The JavaScript Object Notation for Linked Data, a.k.a JSON-LD ("application/json+ld").
     */
    JSON_LD(LDMediaTypes.JSON_LD),

    /**
     * TriG, a serialization format for RDF ("application/trig").
     */
    TRIG(LDMediaTypes.APPLICATION_TRIG),

    /**
     * The N-Quads format is a line-based RDF syntax with a similar flavor as N-Triples, however
     * it supports multiple graphs ("text/x-nquads").
     */
    NQUADS(LDMediaTypes.NQUADS),

    /**
     * The N-Quads format is a line-based RDF syntax with a similar flavor as N-Triples, however
     * it supports multiple graphs ("text/x-nquads").
     *
     * @see LDMediaType#NQUADS
     */
    TEXT_NQUADS(LDMediaTypes.TEXT_NQUADS);

    /*
    Not yet supported formats:
    RDF/JSON (application/json)
    TriX (Triples in XML, text/xml, application/trix)
    RDF Binary - ?
     */

    private final String value;

    LDMediaType(String value) {
        this.value = value;
    }

    /**
     * Retrieve the corresponding string representation of this media type.
     *
     * @return the string representation
     */
    public String getMediaTypeString() {
        return value;
    }

    /**
     * Match a received string to a LD Media Type. If there is no
     * match an {@code IllegalArgumentException} will be thrown.
     *
     * @param mediaType the media type
     * @return the LD media type if there is a match, an {@code IllegalArgumentException} will be thrown otherwise
     */
    public static LDMediaType getLDMediaTypeForString(String mediaType) {

        return Arrays.stream(values())
                .filter(mt -> mt.getMediaTypeString().equals(mediaType))
                .findFirst()
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("Not supported media type: " + mediaType);
                });
    }

    /**
     * Retrieve the corresponding regular Media Type for this LD media type.
     *
     * @return the media type
     */
    public MediaType asMediaType() {

        var mediaTypeString = this.getMediaTypeString();
        String[] typeAndSubtype = mediaTypeString.split("/");

        return new MediaType(typeAndSubtype[0], typeAndSubtype[1]);
    }
}
