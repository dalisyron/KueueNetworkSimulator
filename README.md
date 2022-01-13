# KueueNetworkSimulator

A queuing network simulator written in Kotlin. Part of the Computer Simulation course at IUST (Fall 2021).

Networks of queues are systems in which a number of queues are connected by what's known as customer routing. When a customer is serviced at one node it can join another node and queue for service, or leave the network.

# Usage
In order to build the application and its components you need GraalVM, which is a high-performance JDK.

KueueNetworkSimulator (KNS) allows for defining your own queue networks using the ```QueueNetworkDescription``` class. A queue network description is described in a very similar fashion to describing graphs using adjacency lists. Kotlin's idiomatic syntax allows for defining network descriptions very easily, in some cases using less code than well-known graph DSLs.

**Example:**
```
val sampleNetwork = QueueNetworkDescription(
    startNodes = listOf(
        StartNodeDescription(
            id = 1,
            serviceRate = 2.0,
            arrivalRate = 1.0
        )
    ),
    queueNodes = listOf(
        QueueNodeDescription(
            id = 2,
            serviceRate = 4.0
        ),
        QueueNodeDescription(
            id = 3,
            serviceRate = 3.0
        )
    ),
    edges = listOf(
        EdgeDescription(1, 2, 0.4),
        EdgeDescription(1, 3, 0.6)
    )
)
```
The above code corresponds to the following queue network:

<p align="center">
  <img src="https://user-images.githubusercontent.com/34644374/149368236-6a8917ba-99c8-4abc-8f86-72b8bc278b3b.png" />
</p>

# Internals
Under the hood, there are three main steps to each KNS app.

## Step1 - Network Creation
KNS uses the ```NetworkCreator``` class to convert a ```QueueNetworkDescription``` object to a ```QueueNetwork``` object, which is the runtime representation of the queue network, and is used in Step 2 and Step3. 
 
##  Step 2 - Graph GUI Generation
KNS offers a GUI which automatically displays the corresponding queue network using its graph representation. The '''GraphvizGenerator''' class is a mapper that converts '''QueueNetwork''' objects to their standard GraphViz representations. Next, the [graphviz-java](https://github.com/nidi3/graphviz-java) library is used to draw the input graph.

<p align="center">
  <img src="https://user-images.githubusercontent.com/34644374/149370459-a3945368-fe2c-4c12-997f-25a13ece6bce.png" />
</p>

##  Step 3 - Simulation
Finally the ```QueueNetworkSimulator``` class is used to perform the simulation on the ```QueueNetwork``` object. KNS uses the standard Discrete-time Event Simulation paradigm. The simulator uses an event queue which is filled with both arrival and departure events. As the events are executed the simulation clock progresses and logs are recorded for future reporting.
<p align="center">
  <img src="https://user-images.githubusercontent.com/34644374/149372600-64f9aea7-922b-4d96-95eb-1e969b7b6fa8.png" width="300" height="600"/>
</p>
