# KueueNetworkSimulator

A queuing network simulator written in Kotlin.

KNS provides a flexible and robust framework for simulating queue networks and deriving key metrics such as average waiting time, queue lengths, and node utilization. The system's modular design makes it easy to experiment with different network configurations and observe the effects on overall performance.

Networks of queues are systems in which a number of queues are connected by what's known as customer routing. When a customer is serviced at one node, it can either join another node and queue for service or leave the network.

Queueing networks are widely used in computer science, especially in networking technology. Routers and switches, for instance, rely on queues to manage the packets awaiting transmission. By applying queuing theory principles, system designers can optimize these devices to achieve better performance and resource efficiency, which is crucial for high-speed packet forwarding in data centers and other large-scale networks.

# Usage
In order to build the application and its components, you need GraalVM, which is a high-performance JDK.

KueueNetworkSimulator (KNS) allows for defining your own queue networks using the ```QueueNetworkDescription``` class. A queue network description is described in a similar fashion to describing graphs using adjacency lists. Kotlin's idiomatic syntax allows for defining network descriptions very easily, sometimes with less code than well-known graph DSLs.

**Example:**
```kotlin
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

## Step 1 - Network Creation
KNS uses the ```NetworkCreator``` class to convert a ```QueueNetworkDescription``` object to a ```QueueNetwork``` object, which is the runtime representation of the queue network and is used in Step 2 and Step 3.

##  Step 2 - Graph GUI Generation
KNS offers a GUI that automatically displays the corresponding queue network using its graph representation. The ```GraphvizGenerator``` class is a mapper that converts ```QueueNetwork``` objects to their standard GraphViz representations. Then, the [graphviz-java](https://github.com/nidi3/graphviz-java) library is used to draw the input graph.

<p align="center">
  <img src="https://user-images.githubusercontent.com/34644374/149370459-a3945368-fe2c-4c12-997f-25a13ece6bce.png" />
</p>

##  Step 3 - Simulation
The ```QueueNetworkSimulator``` class performs the simulation on the ```QueueNetwork``` object. KNS uses the Discrete-time Event Simulation paradigm, where an event queue is filled with both arrival and departure events. As events are executed, the simulation clock progresses and logs are recorded for future reporting.

# Metrics and Interpretation

During the simulation, several performance metrics are calculated and displayed. These are important for understanding the efficiency of the network. The following metrics are tracked:

### Per-node Metrics:

1. **L (Average number of customers in the system)**:
   - This represents the average number of customers in both service and the queue at a particular node. Calculated as:

     $$L = \frac{\text{totalSumL}}{\text{clock}}$$
     
     where totalSumL is the cumulative number of customers at the node and clock is the current simulation time.

2. **LQ (Average number of customers in the queue)**:
   - This represents the average number of customers waiting in the queue (excluding those in service). Calculated as:

     $$LQ = \frac{\text{totalSumLQ}}{\text{clock}}$$

3. **W (Average time spent by a customer in the system)**:
   - This metric measures the average time a customer spends in the system (both waiting in the queue and being serviced). Calculated as:
   
     $$W = \frac{\text{totalSumW}}{\text{clock}}$$

4. **WQ (Average time spent by a customer in the queue)**:
   - This metric calculates the average time a customer spends waiting in the queue (excluding service time). It is given by:

     $$WQ = \frac{\text{totalSumWQ}}{\text{clock}}$$

5. **ϕ (Node utilization)**:
   - This metric represents the fraction of time the node is busy serving customers. Utilization can help identify whether a node is over-utilized or under-utilized. It is calculated as:

     $$\phi = \frac{\text{totalSumPhi}}{\text{clock}}$$

### System-wide Metrics:

1. **N (Average number of customers in the system)**:
   - This system-wide metric tracks the average number of customers in the network (all nodes combined) over the entire simulation. It is calculated as:

     $$N = \frac{\text{sum}}{\text{clock}}$$

2. **R (System throughput or response time)**:
   - This metric calculates the system's overall throughput or response time. It represents how quickly customers are processed by the entire network, defined by:

     $$R = \frac{N}{\sum \text{arrival rates}}$$

     where N is the average number of customers in the system and the denominator is the total arrival rate across all start nodes.

# Simulation Flow

The simulation follows a discrete-event model, where events are processed in chronological order. The following flowchart outlines the basic process:

<p align="center">
  <img src="https://user-images.githubusercontent.com/34644374/149372600-64f9aea7-922b-4d96-95eb-1e969b7b6fa8.png" width="300" height="600"/>
</p>

