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

/**
 * The Linked, Semantic, Data media types. TRhese are used when accessing data
 * over the net (Accept and Content-Type headers) and forms the base for the
 * corresponding enum denoting the same.
 */
public final class LDMediaTypes {

	private LDMediaTypes() {
	}

	/**
	 * The Terse RDF Triple Language a.k.a. "Turtle" ("text/turtle").
	 */
	public static final String TEXT_TURTLE = "text/turtle";

	/**
	 * The JavaScript Object Notation for Linked Data, a.k.a. JSON-LD
	 * ("application/json+ld").
	 */
	public static final String JSON_LD = "application/json+ld";

	/**
	 * The RDF/XML format ("application/rdf+xml").
	 */
	public static final String RDF_XML = "application/rdf+xml";

	/**
	 * The N-triples format, a subset of the Turtle. ("application/n-triples")
	 */
	public static final String APPLICATION_N_TRIPLES = "application/n-triples";

	/**
	 * TriG, a serialization format for RDF ("application/trig").
	 */
	public static final String APPLICATION_TRIG = "application/trig";

	/**
	 * The N-Quads format is a line-based RDF syntax with a similar flavor as
	 * N-Triples, however it supports multiple graphs ("text/x-nquads").
	 */
	public static final String TEXT_NQUADS = "text/x-nquads";

	/**
	 * The N-Quads format is a line-based RDF syntax with a similar flavor as
	 * N-Triples, however it supports multiple graphs ("text/x-nquads").
	 */
	public static final String NQUADS = TEXT_NQUADS;

	/**
	 * The N-triples format, a subset of the Turtle. ("text/n3")
	 */
	public static final String TEXT_N_TRIPLES = "text/n3";
}
