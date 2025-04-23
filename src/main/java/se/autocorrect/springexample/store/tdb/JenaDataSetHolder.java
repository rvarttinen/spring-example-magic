package se.autocorrect.springexample.store.tdb;

import org.apache.jena.query.Dataset;

public interface JenaDataSetHolder {

	Dataset getDataSet();
}
