# spring-example-magic
A simple example of using Spring Boot with an external source of data converting it to RDF and storing it locally in a triple store (Apache Jena TDB2). The data can be exposed in various formats by this service: 
* RDF; Turtle, RDF/XML and JSON-LD
* Regular formats like JSON and XML, without semantics
* Maybe more on its way ... 

# Introduction
This project has several purposes: 
* serve as an example of how to retrieve data from an external service and store that data in a triple store after applying semantics
* base for experimentation using the latest and greatest of Java (currently 24, but resorted back to 23 as Gradle dosn't seem to support 24 yet) and other libraries used in this project
* act as a repository of "nice-to-have" features on how to do things Spring Boot and RDF, i.e. implementation, testing, format conversion, asynch, etc.

# Building and Running the service
After checking out the code from this repository, building it should be straightforward. 
In a Windows environment: 

```
gradlew build
```

... Linux and Mac: 

```
./gradle build
```
Building the code will also execute the tests in the project. 

It is recommended to use a tool like Postman (https://www.postman.com/downloads/) to make invocations to the service and try different 'Accept' headers to see the results. 
Running the service in an Windows environment: 

```
gradlew bootRun
```

... and for Linux and Mac: 

```
./gradle bootRun
```
It is also possible to load and execute this code in your IDE of choice. It has been tested on Eclipse SDK (2024-09 Version: 4.33.0).  
A frontend based on React is in the making. 

# Issues
When this service is started up it does not populate the Bloom Filter from the triple store. Until this is properly done (small fix!) it is recommended to delete the files under 'MyDatabases/DB1' after closing down the service. 

# Further improvements ... 
This is an experimental project, however, improvements will be made, including use of the latest features in the Java platform (currently Java 23). 

Items on the current TODO-list: 
- add a client/UI for exploring data visually; currently a React based frontend is being created. It will be exposed in a repository of its own. 
- add a mechanism that reads any stored entries from previous sessions to the Bloom Filter
- add logging. There are some ideas of how to do this using monads, i.e. from a 'Try' chain some other monad that takes care of any logging
- see if we can make use of some interesting new features in Java: 
    - switch with pattern matching
    - primitive types in patterns (instanceof and switch)
    - use a SequencedCollection where applicable 
    - ... more ...
- expose a SPARQL-endpoint
- combine data from other sources?
- introduce Futures for handling incoming requests (to experiment codewise, this service is not required to be performant in any way)
- collect utilities common to other projects into thier own repository (expose a util-library for reuse and avoid code duplication over several repos)
- String templates was introduced as a preview in 21, but as of 23 the feature has been axed due to design flaws. So, we will not se any of those as we are on 23 ... 

