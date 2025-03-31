# spring-example-magic
A simple example of using Spring Boot with an external source of data converting it to RDF and storing it locally in a triple store (Apache Jena). The data can be exposed in various formats by this service: 
* RDF; Turtle, RDF/XML and JSON-LD
* Regular formats like JSON and XML, without semantics
* Maybe more on its way ... 

# Introduction
This project has several purposes: 
* serve as an example of how to retrieve data from an external service and store that data in a triple store after applying semantics
* base for experimentation using the latest and greatest of Java (currently 24, but resorted back to 23 as Gradle dosn't seem to support 24 yet) and other libraries used in this project
* act as a repository of "nice-to-have" features on how to do things Spring Boot and RDF, i.e. implementation, testing, format conversion, asynch, etc.

# What is does
The example/demo service is really simple; it will retrieve an entry from the [Bored API](https://apis.scrimba.com/bored/) and stores it locally as RDF magic. I.e providing a key to an entry will fetch it from the external, Bored API, and apply semantics to  it and then store it in an in-memory triple store. Keys are in the range [1000000, 9999999]. If same entry is requested subsequently the locally stored entry will be used (a Bloom filter is queried first to see if there might be a local one, otherwise an external fetch is performed). 

If no key is provided the service will list all locally stored entries. It will not make any attempt to retrieve any external data - for now. 

By providing different values for the 'Accept'- header when making a 'GET' request will render corresponding format for the data retrieved. Currently supported formats: 
* 'application/json', plain ol' JSON
* 'text/xml', plain even older XML

And, the supported RDF formats: 
* 'application/json+ld', the JSON-LD format, JavaScript Object Notation for Linked Data
* 'text/turtle', the Terse RDF Triple Language (Turtle) format, more compact and readable than JSON-LD
* 'application/rdf+xml' , RDF/XML to express (i.e. serialize) an RDF graph as an XML document. Not so compact and not that readable ...

As the triple store used in this experiment/demo service is an in-memory store, all its data will be lost when the service is closed. There are some ideas creating a persistent store that retains the data between sessions. Also. ,there are some other ideas of creating a mechanism for populating the store with a specified number of entries when invoked. 

Some example keys that can be used: 
* 4290333 - "Go on a long drive with no music" 
* 6204657 - "Surprise your significant other with something considerate"
* 3943506 - "Learn Express.js"

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

It is also possible to load and execute this code in your IDE of choice. It has been tested on Eclipse SDK (2024-09 Version: 4.33.0). 

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

Use PostMan or your client of choice to check that the service is responding. using 'curl': 

```
curl --location 'http://localhost:8080/v1/magic?key=3943506' \
--header 'Accept: text/turtle'
```
Sometimes, like using the 'cmder' command line tool on Windows might render an error setting the port number should be number. 

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
... accessing the service using e.g. PostMan or the 'curl' command as above will render something like this on the console:

```
Forwarding from 127.0.0.1:8080 -> 8080
Forwarding from [::1]:8080 -> 8080
Handling connection for 8080
Handling connection for 8080
Handling connection for 8080
```


# Further improvements ... 
This is an experimental project, however, improvements will be made, including use of the latest features in the Java platform (currently Java 23). 

Items on the current TODO-list: 
- there are some bugs in the porocess of applying semantics and storing entries that needs to be fixed (seems to be a mixup of keys)
- create a mechanism for populating the triple store with a specified number of random entries
- introduce version 2 (v2) of the service with greater capabilities and asynchronous behavior 
- add a client/UI for exploring data visually; currently a React based frontend is being created. It will be harbored in a repository of its own. 
- separate out the triple store to a separate service
- if parts of this service is split up, it could be deployed as several pods in a Kubernetes cluster (executed locally in e.g. MiniKube)
- add a mechanism that reads any stored entries from previous sessions into the Bloom filter when using TDB (file persistence) 
- add logging. There are some ideas of how to do this using monads, i.e. from a 'Try' chain some other monad that takes care of any logging
- see if we can make use of some interesting new features in Java: 
    - switch with pattern matching (to a large extent already done) 
    - primitive types in patterns (instanceof and switch, on its way ...)
    - use a SequencedCollection where applicable 
    - ... more ...
- expose a SPARQL-endpoint (if the triple store is executed in a separate service, it probably already has this or add a Apache Fuseki service pod if using TDB)
- combine data from other sources? 
- introduce Futures for handling incoming requests (to experiment codewise, this service is not really required to be that performant in any way)
- collect utilities common to other projects into their own repository (expose a util-library for reuse and avoid code duplication over several repos)
- String templates was introduced as a preview in 21, but as of 23 the feature seems to have been axed due to alleged design flaws. So, we will unfortunately not se any of those as we are on 23, for now ... 

