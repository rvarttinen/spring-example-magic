# spring-example-magic

![Java](https://img.shields.io/badge/Java-blue) ![RDF](https://img.shields.io/badge/RDF-orange) ![JSON](https://img.shields.io/badge/JSON-black) ![XML](https://img.shields.io/badge/XML-white) ![SPARQL](https://img.shields.io/badge/SPARQL-green) ![GraphQL](https://img.shields.io/badge/GraphQL-yellow) 

![Docker](https://img.shields.io/badge/Docker-blue) ![Kubernetes](https://img.shields.io/badge/Kubernetes-blue)

![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/rvarttinen/spring-example-magic/gradle.yml)
<a href="https://github.com/rvarttinen/spring-example-magic/issues">
      <img alt="Issues" src="https://img.shields.io/github/issues/rvarttinen/spring-example-magic?color=0088ff" />
</a>


A simple example of using Spring Boot with an external source of data converting it to RDF and storing it locally in a triple store (Apache Jena). The data can be exposed in various formats by this service: 
* RDF; Turtle, RDF/XML and JSON-LD
* Regular formats like JSON and XML, without semantics
* Accepting GraphQL queries and corresponding responses
* A SPARQL endpoint can be activated and accessed using a SPARQL-editor or the like
* Maybe more on its way ... 

# Introduction
This project has several purposes: 
* serve as an example of how to retrieve data from an external service and store that data in a triple store after applying semantics
* base for experimentation using the latest and greatest of Java (currently 24) and other libraries used in this project
* act as a repository of "nice-to-have" stuff on how to do things Spring Boot, RDF, etc., i.e. implementation, testing, format conversion, asynch, practicies, etc.

# What is does
The example/demo service is really simple; it will retrieve an entry from the [Bored API](https://apis.scrimba.com/bored/) and store it locally as RDF magic. I.e providing a key to an entry will fetch it from the external, Bored API, and apply semantics to  it and then store it in an in-memory triple store. Keys are in the range [1000000, 9999999]. If same entry is requested subsequently the locally stored entry will be used (a Bloom filter is queried first to see if there might be a local entry of it stored, otherwise an external fetch is performed). 

### Regular REST, with and without RDF 
If no key is provided the service will list all locally stored entries. It will not make any attempt to retrieve any external data - for now. 

Use [PostMan](https://www.postman.com/downloads/) or your client of choice to perform request invocations to the service. By altering the `Accept`-header you may get different formats of the response of interest. 
If you prefer using `curl`: 

```
curl --location 'http://localhost:8080/v1/magic?key=3943506' \
--header 'Accept: text/turtle'
```

By providing different values for the `Accept`-header when making a `GET` request will render corresponding format for the data retrieved. Currently supported formats: 
* `application/json`, plain ol' JSON
* `text/xml`, plain even older XML

And, the supported RDF formats: 
* `application/json+ld`, the JSON-LD format, JavaScript Object Notation for Linked Data
* `text/turtle`, the Terse RDF Triple Language (Turtle) format, more compact and readable than JSON-LD
* `application/rdf+xml` , RDF/XML to express (i.e. serialize) an RDF graph as an XML document. Not so compact and not that readable ...

Compare that with the other results when invoking `GET` requests with various `Accept`-hreaders. 

As the triple store used in this experiment/demo service is an in-memory store, all its data will be lost when the service is closed. There are some ideas creating a persistent store that retains the data between sessions. Also, there are some other ideas of creating a mechanism for populating the store with a specified number of random entries when invoked. 

Some example keys that can be used for interesting results: 
* 4290333 - "Go on a long drive with no music" 
* 6204657 - "Surprise your significant other with something considerate"
* 3943506 - "Learn Express.js"

### GraphQL endpoint
A GraphQL end point has been added recently. The GraphiQL capability is activated in the `application.properties` file, thus executing queries can be done by pointing your web browser of choice to: 

```
http://localhost:8080/graphiql
```
By "populating" the triple store, e.g. by providing a key "6204657" using Postman or curl, a query like below:

```
query magicDetails {
  magicByKey(key: "6204657") {
    id
    magicString
    type
  }
```
.. will render an intersting result like: 

```
q{
  "data": {
    "magicByKey": {
      "id": "6204657",
      "magicString": "Surprise your significant other with something considerate",
      "type": "social"
    }
  }
}
```

Please keep in mind that the GraphQL capabilities was recently added and is a work in progress (as the entire app for that matter), as more, and more complex, things will come soon. 

### SPARQL endpoint
By activating the SPARQL capability the locally stored RDF data can bea extracted and manipulated using a SPRQL endpoint. This capability comes "out-of-box" by employing Apache Fuseki.
The `application.properties` file holds a setting, `rdf.fuseki.enabled`, controlling this feature. By default it is set to `false`. 
By setting it to `true` and restarting the service the Fuseki provided endpoint will be available on port 3001. You can use Postman (post the request with the SPARQL-query) or any SPARQL-editor of your choice. 

### Logging to the console
There is a logging aspect taking care of logging in the `services` and `infrastructure` packages. These log statements are set to the be active for the log level `INFO`. Using an aspect for logging is maybe not that obvious, it can be sometimes hard to see what gets logged and when. The idea behind it, though, is that we do not want to litter the code with logging statements and keep it somewhat clean, and this is a kind of demo anyway. The project is not that large ... yet. 

Also, when it comes to logging: all incoming request are furnished with Mapped Diagnostic Context (MDC) so the call chain of method invocations through the application can easily be followed if there are multiple simultaneous incoming requests. Check the `logback-spring.xml` file, the value appears as `traceId`. The class `RequestLoggingFilter` is invoked for each incoming request and produces a trace id for this purpose. 

The controller already has logging by default in Spring Boot itself. However, in order to see it you need to set the log level to `DEBUG`. However, doing so will render quite a lot of printouts on the console. If you want to try logging on other levels, like `INFO`; you could alter the `RequestLoggingFilter` class; add whatever logging statments necessary. 

# History
Everything has a history, even this little project. It started out as a simple demo with a slightly silly and whimsical touch (to get people's attention?). It lay dormant for some years until quite recently when it is now housed in this repository. 
However, in doing so it started slowly move away from some silliness and hopefully it will mature over time as it gets new features and the deployment model solidifies (Kubernetes). 

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
A frontend based on React is in the making. 

Running the service in an Windows environment: 

```
gradlew bootRun
```

... and for Linux and Mac: 

```
./gradle bootRun
```

It is also possible to load and execute this code in your IDE of choice. It has been tested on Eclipse SDK (2025-03 4.35.0). 

# Docker 
Containerizing the application is done using the following steps. Once a Docker image is available it can be deployed as single service in e.g Docker Desktop, or in a Kubernetes cluster. 

### Building the Docker image
Building a Docker image is pretty straight forward: 

```
gradlew bootBuildImage --imageName=autocorrect/spring-boot-magic
```

### Deploy to Docker Desktop
Once the container is built, it is time for deployment: 

```
docker run -p 8080:8080 -t autocorrect/spring-boot-magic:latest
```

### Testing the deployed container
The Tomcat web container bundled with Spring Boot exposes the service on port 8080 by default. 

Use PostMan or your client of choice to check that the service is responding. If you prefer using `curl`: 

```
curl --location 'http://localhost:8080/v1/magic?key=3943506' \
--header 'Accept: text/turtle'
```
Sometimes, like using the 'cmder' command line tool on Windows might render an error indicating the port number setting not bing a number, despite being an integer. 

```
curl: (3) URL rejected: Port number was not a decimal number between 0 and 65535
```
Try replacing the single (') quotes with double quotes (") and it should work. 

### Deploy to a Kubernetes cluster

Once the the Docker image is verified it can also be deployed to Kubernetes cluster. In this example we will make a simple deploy to a locally running Minikube installation. 

More on [minikube](https://minikube.sigs.k8s.io/docs/)

For our convenience a deployment YAML-file for Kubernetes is already created in this repository. Further, the latest version of the Docker image is available on Docker Hub. The [deployment.yaml](./deployment.yaml) points to this image on Docker Hub. However, if you want to create your own you can either: 
* push your image to a local Docker repository (can be a bit cumbersome as you also need a proxy running for our container), or ..
* ... push it to your own repository on Docker Hub 

If you want to check the details on how to create and deploy a Kubernetes deployment for Spring Boot you can read more: [Guide: Spring Boot Kubernetes](https://spring.io/guides/gs/spring-boot-kubernetes)

Start your Minikube cluster: 

```
minikube start
```
Running the Minikube Dashboard will render a UI in your default browser. It is not necessary as all commands can be executed on the command line, but maybe visuaully more pleasing. 

```
minikube dashboard
```

Once you have a Docker image available, locally or on Docker Hub, it can be deployed:

```
kubectl apply -f deployment.yaml
```
Check that your pod has started and is up and running (or check in the dashboard). 

```
kubectl get pods
```
Executing the following command will tell us whether our service is available: 

```
kubectl get all
```
The console printout will look like something like this: 

```
NAME                        READY   STATUS    RESTARTS   AGE
pod/demo-6476c974bd-rxkjx   1/1     Running   0          68m

NAME                 TYPE        CLUSTER-IP     EXTERNAL-IP   PORT(S)    AGE
service/demo         ClusterIP   10.109.8.245   <none>        8080/TCP   2d23h
service/kubernetes   ClusterIP   10.96.0.1      <none>        443/TCP    2d23h

NAME                   READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/demo   1/1     1            1           114m

NAME                              DESIRED   CURRENT   READY   AGE
replicaset.apps/demo-6476c974bd   1         1         1       101m
replicaset.apps/demo-bf9496f7f    0         0         0       114m
```
The service is running, but there is no port forwarding so the following is needed (recommendation: execute the following command in a separate console window or tab as it locks the console): 

```
kubectl port-forward svc/demo 8080:8080
```
... accessing the service using e.g. PostMan or the 'curl' command as above will render something like this on the console with port forwarding:

```
Forwarding from 127.0.0.1:8080 -> 8080
Forwarding from [::1]:8080 -> 8080
Handling connection for 8080
Handling connection for 8080
Handling connection for 8080
```


# Further improvements ... 
This is an experimental project, however, improvements will be made, including use of the latest features in the Java platform (currently Java 24). 

Items on the current TODO-list: 
- create a mechanism for populating the triple store with a specified number of random entries
- introduce version 2 (v2) of the service with greater capabilities and asynchronous behavior 
- add a client/UI for exploring data visually; currently a React based front-end is being worked on. It is harbored in a [repository of its own.](https://github.com/rvarttinen/react-example-magic) 
- separate out the triple store to a separate service
- maybe add OAuth2 authentication to make the whole thing more production like? One could, e.g., login using one's account on GitHub. 
- if parts of this service is split up, it could be deployed as several pods in a Kubernetes cluster (executed locally in e.g. MiniKube)
- add a mechanism that reads any stored entries from previous sessions into the Bloom filter when using TDB (file persistence) 
- see if we can make use of some interesting new features in Java: 
    - switch with pattern matching (to a large extent already done) 
    - primitive types in patterns (instanceof and switch, on its way ...)
    - maybe use a SequencedCollection where applicable 
    - ... more ...
- combine data from more sources? "Convert" Chuck Norris jokes to magic as well? 
- introduce Futures for handling incoming requests (to experiment codewise, this service is not really required to be that performant in any way)
- collect utilities common to other projects into their own repository (expose a util-library for reuse and avoid code duplication over several repos)
- String templates was introduced as a preview in 21, but as of 23 the feature seems to have been axed due to alleged design flaws. So, we will unfortunately not se any of those for now ... 

