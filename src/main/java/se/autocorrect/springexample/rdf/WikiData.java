package se.autocorrect.springexample.rdf;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

public class WikiData {

	private WikiData() {}

	/*
	 * A local URI for local usage when processing data. 
	 */
	public static final String localUri = "http://rdf.autocorrect.se/wikidata#";
	
	/*
	 * Prefixes
	 */
	public static final String PREFIX_WIKIDATA_ENITY = "http://www.wikidata.org/entity/";
	
	public static final String PREFIX_WIKIDATA_STATEMENT = "http://www.wikidata.org/entity/statement/";

	public static final String PREFIX_PROPERTY = "http://www.wikidata.org/prop/";
	public static final String PREFIX_PROPERTY_STATEMENT = "http://www.wikidata.org/prop/statement/";
	public static final String PREFIX_PROPERTY_STATEMENT_VALUE = "http://www.wikidata.org/prop/statement/value/";
	public static final String PREFIX_PROPERTY_DIRECT = "http://www.wikidata.org/prop/direct/";
	public static final String PREFIX_PROPERTY_QUALIFIER = "http://www.wikidata.org/prop/qualifier/";
	public static final String PREFIX_PROPERTY_QUALIFIER_VALUE = "http://www.wikidata.org/prop/qualifier/value/";
	public static final String PREFIX_PROPERTY_REFERENCE = "http://www.wikidata.org/prop/reference/";
	public static final String PREFIX_PROPERTY_REFERENCE_VALUE = "http://www.wikidata.org/prop/reference/value/";

	public static final String PREFIX_GEO = "http://www.opengis.net/ont/geosparql#";

	public static final String PREFIX_WIKIDATA_REFERENCE = "http://www.wikidata.org/reference/";

	public static final String PREFIX_WIKIDATA_NO_VALUE = "http://www.wikidata.org/prop/novalue/";

	public static final String PREFIX_WIKIDATA_NO_QUALIFIER_VALUE = PREFIX_WIKIDATA_NO_VALUE;

	public static final String PREFIX_WIKIDATA_VALUE = "http://www.wikidata.org/value/";

	public static final String PREFIX_WBONTO = "http://wikiba.se/ontology#";
	
	/*
	 * IRIs of property datatypes
	 */
	public static final String DT_ITEM = PREFIX_WBONTO + "WikibaseItem";
	public static final String DT_PROPERTY = PREFIX_WBONTO + "WikibaseProperty";
	public static final String DT_LEXEME = PREFIX_WBONTO + "WikibaseLexeme";
	public static final String DT_FORM = PREFIX_WBONTO + "WikibaseForm";
	public static final String DT_SENSE = PREFIX_WBONTO + "WikibaseSense";
	public static final String DT_MEDIA_INFO = PREFIX_WBONTO + "WikibaseMediaInfo";
	public static final String DT_STRING = PREFIX_WBONTO + "String";
	public static final String DT_URL = PREFIX_WBONTO + "Url";
	public static final String DT_COMMONS_MEDIA = PREFIX_WBONTO + "CommonsMedia";
	public static final String DT_TIME = PREFIX_WBONTO + "Time";
	public static final String DT_GLOBE_COORDINATES = PREFIX_WBONTO + "GlobeCoordinate";
	public static final String DT_QUANTITY = PREFIX_WBONTO + "Quantity";
	public static final String DT_MONOLINGUAL_TEXT = PREFIX_WBONTO + "Monolingualtext";
	public static final String DT_EXTERNAL_ID = PREFIX_WBONTO + "ExternalId";
	public static final String DT_MATH = PREFIX_WBONTO + "Math";
	public static final String DT_GEO_SHAPE = PREFIX_WBONTO + "GeoShape";
	public static final String DT_TABULAR_DATA = PREFIX_WBONTO + "TabularData";
	public static final String DT_EDTF = PREFIX_WBONTO + "Edtf";
	
	protected static Resource resource(String uri, String local) {
        return ResourceFactory.createResource(uri + local);
    }

    protected static Property property(String uri, String local) {
        return ResourceFactory.createProperty(uri, local);
    }
    
    public static final Resource ARTICLE = Init.article();
    
    public static final Property P17 = Init.country();
    public static final Property P31 = Init.instanceOf();
    public static final Property P47 = Init.sharesBorderWith();
    public static final Property P421 = Init.inTimeZone();
    public static final Property P407 = Init.languageOfWorkOrName();
    public static final Property P443 = Init.pronounciationAudio();
	
    public static final Property P31D = Init.instanceOfDirect();
    
    static class Init {

    	public static Resource article() {
    		return resource(localUri, "Article");
    	}

    	public static Property instanceOf() {
			return property(DT_ITEM, "P31");
		}

		public static Property instanceOfDirect() {
			return property(PREFIX_PROPERTY_DIRECT, "P31");
		}

		public static Property pronounciationAudio() {
			return property(DT_ITEM, "P443");
		}

		public static Property languageOfWorkOrName() {
			return property(DT_ITEM, "P407");
		}

		public static Property inTimeZone() {
			return property(DT_ITEM, "P421");
		}

		public static Property sharesBorderWith() {
			return property(DT_ITEM, "P47");
		}

		public static Property country() {
			return property(DT_ITEM, "P17");
		}
    }
}
