# spring-example-magic
A simple example of using Spring Boot with an external source of data converting it to RDF and storing it in a triple store. The data can be exposed in various formats by this service: 
* RDF; Turtle, RDF/XML and JSON-LD
* Regular formats like JSON and XML, without semantics
* Maybe more on its way ... 

# Introduction
This project has several purposes: 
* serve as an example of how to retrieve data from an external service and store that data in a triple store after applying semantics
* base for experimentation using the latest and greatest of Java (currently 23) and other libraries used in this project
* act as a repository of "nice-to-have" features on how to do things Spring Boot and RDF, i.e. implementation, testing, format conversion, etc.

# Build and Running the service
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

It is recommended to use a tool like Postman (https://www.postman.com/downloads/) to make invocations to the service and try different 'Accept' headers to the results.  
Running the service in an Windows environment: 

```
gradlew bootRun
```

... and for Linux and Mac: 

```
./gradle bootRun
```
It is also possible to load and execute htis code in your IDE of choice. It has been tested on Eclipse SDK (2024-09 Version: 4.33.0).  

# Issues
When this service is started up it does not populate the Bloom Filter from the triple store. Until this is properly done (small fix!) it is recommended to delete the files under 'MyDatabases/DB1' after closing down the service. 

# Further improvements ... 
This is an experimental project, however, improvements will be made, including use of the latest features in the Java platform (currently Java 23). 

Items on the current TODO-list: 
- add a mechanism that reads any stored entries from previous sessions to the Bloom Filter 
- see if we can make use of some interesting new features in Java: 
    - switch with pattern matching
    - primitive types in patterns (instanceof and switch)
    - string templates should be useful when creating some queries ...
    - ... more ...
- expose a SPARQL-endpoint
- combine data from other sources?
- add a client/UI for exploring data? 

