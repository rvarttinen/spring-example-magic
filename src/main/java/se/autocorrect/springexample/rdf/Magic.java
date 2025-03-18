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

import org.apache.jena.graph.Node;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

/**
 * The Magic vocabulary. It reflects the contents of the vocabulary as defined
 * in the {@code magic.ttl} file.
 */
public class Magic {
	
	private Magic() {}
	
	/**
     * The proposed name space of the vocabulary as a string.
     */
    public static final String uri = "http://rdf.autocorrect.se/magic#";

	/**
     * The proposed prefix to use.
     */
    public static final String prefix = "magic";

    /**
     * Returns the URI for this schema.
     *
     * @return the URI for this schema
     */
    public static String getURI() {
        return uri;
    }

    protected static Resource resource(String local) {
        return ResourceFactory.createResource(uri + local);
    }

    protected static Property property(String local) {
        return ResourceFactory.createProperty(uri, local);
    }


    public static final Resource Magic = Init.Magic();

    public static final Property magicId = Init.magicId();
    public static final Property magicDescription = Init.magicDescription();
    public static final Property magicType = Init.magicType();

    public static final Property originatingType = Init.originatingType();
    

    /**
     * Magic constants are used during Apache Jena initialization. If that initialization
     * is triggered by touching the {@code Magic} class, then the constants are null.
     * So for these cases, call this helper class: Init.function()
     */
    public static class Init {

        public static Resource Magic() {
            return resource("Magic");
        }

        public static Property magicType() {
			return property("magicType");
		}

		public static Property magicId() {
            return property("magicId");
        }

        public static Property magicDescription() {
            return property("magicDescription");
        }

        public static Property originatingType() {
            return property("originatingType");
        }
    }

    /**
     * The same items of vocabulary, but at the Node level, parked inside
     * a nested class so that there is a simple way to refer to them.
     */
    public static final class Nodes {

        public static final Node Magic = Init.Magic().asNode();
        public static final Node magicId = Init.magicId().asNode();
        public static final Node magicDescription = Init.magicDescription().asNode();
        public static final Node magicType = Init.magicType().asNode();
        public static final Node originatingType = Init.originatingType().asNode();
    }
}
