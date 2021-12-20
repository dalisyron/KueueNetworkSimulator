import java.util.*
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

enum class EventType {
    ARRIVAL, DEPARTURE
}

class Event(
    val time: Double,
    val nodeId: Int,
    val customerId: Int,
    val type: EventType
)

class ArrivalDepartureLog(
    val event: Event
)

class QueueNetworkSimulator(
    private val network: QueueNetwork
) {
    private var clock: Double = 0.0
    private val arrivalDepartureLogsForNode: Map<Int, MutableList<ArrivalDepartureLog>> =
        network.nodeById.mapValues { mutableListOf() }
    private val serviceTimeForCustomer = mutableMapOf<Int, Double>()

    private var customerCount: Int = 0

    private val eventQueue: PriorityQueue<Event> = PriorityQueue<Event> { o1, o2 ->
        return@PriorityQueue o1.time.compareTo(o2.time)
    }

    private fun handleDeparture(nodeId: Int, customerId: Int) {
        val node = network.nodeById[nodeId]!!
        node.queue.removeFirst()

        if (node.outEdges != null) {
            addNextNodeArrival(node, customerId)
        }

        if (node.queue.isNotEmpty()) {
            val serviceTime = StatUtils.exp(node.serviceRate)
            serviceTimeForCustomer[node.queue.first()] = serviceTime
            addEvent(
                Event(
                    time = clock + serviceTime,
                    nodeId = nodeId,
                    type = EventType.DEPARTURE,
                    customerId = node.queue.first()
                )
            )
        }
    }

    private fun addStartingNodeArrival(node: StartNode, time: Double) {
        addEvent(
            Event(
                time = time,
                nodeId = node.id,
                type = EventType.ARRIVAL,
                customerId = generateNextCustomerId()
            )
        )
    }

    private fun addNextNodeArrival(node: Node, customerId: Int) {
        val nextNodeDist = node.outEdges!!.map { edge ->
            edge.nextNode to edge.probability
        }.toList()

        val nextNode = StatUtils.choice(nextNodeDist)

        if (nextNode != null) {
            addEvent(
                Event(
                    nodeId = nextNode.id,
                    time = clock,
                    type = EventType.ARRIVAL,
                    customerId = customerId
                )
            )
        }
    }

    private fun generateNextCustomerId(): Int {
        customerCount++
        return customerCount
    }

    private fun handleArrival(nodeId: Int, customerId: Int) {
        val node = network.nodeById[nodeId]!!

        if (node.isBusy()) {
            node.queue.addLast(customerId)
        } else {
            val serviceTime = StatUtils.exp(node.serviceRate)
            serviceTimeForCustomer[customerId] = serviceTime
            addEvent(
                Event(
                    time = clock + serviceTime,
                    nodeId = nodeId,
                    type = EventType.DEPARTURE,
                    customerId = customerId
                )
            )
            node.queue.addLast(customerId)
        }
    }

    private fun addEvent(event: Event) {
        eventQueue.add(event)
        arrivalDepartureLogsForNode[event.nodeId]!!.add(ArrivalDepartureLog(event))
    }

    fun simulate() {
        // initialize start nodes
        val timeLimit = 3000
        val customerArrivalTimes = mutableListOf<Pair<Int, Double>>()

        for (node in network.startNodes) {
            var timer = 0.0

            while (true) {
                val it = StatUtils.exp(node.arrivalRate)
                timer += it

                if (timer < timeLimit) {
                    customerArrivalTimes.add(Pair(node.id, timer))
                } else {
                    break
                }
            }

        }

        val sortedCustomerArrivalTimes = customerArrivalTimes.sortedBy { it.second }

        for (ct in sortedCustomerArrivalTimes) {
            val event = Event(
                time = ct.second,
                nodeId = ct.first,
                type = EventType.ARRIVAL,
                customerId = generateNextCustomerId()
            )
            addEvent(event)
        }

        var op = 0

        while (eventQueue.isNotEmpty()) {
            if (op % 10 == 0) {
                println(op)
            }
            op++
            val strq = eventQueue.map { it -> "${it.type} | ${it.customerId} : ${it.time}" }.joinToString("\n")
            // println(strq)
            // println("---")

            val event = eventQueue.poll()
            clock = event.time

            when (event.type) {
                EventType.ARRIVAL -> {
                    handleArrival(event.nodeId, event.customerId)
                }
                EventType.DEPARTURE -> {
                    handleDeparture(event.nodeId, event.customerId)
                }
            }
        }

        for (nodeId in network.nodeById.keys) {
            generateReportsForNode(nodeId)
        }
    }

    private fun generateReportsForNode(nodeId: Int) {
        val sortedLogs = arrivalDepartureLogsForNode[nodeId]!!.sortedBy {
            it.event.time
        }
        var clock = 0.0
        var count = 0
        var totalSumL = 0.0
        var totalSumLQ = 0.0
        var totalSumW = 0.0
        var totalSumWQ = 0.0
        var totalSumPhi = 0.0

        val startTime = mutableMapOf<Int, Double>()

        for (log in sortedLogs) {
            totalSumL += (log.event.time - clock) * count
            totalSumLQ += (log.event.time - clock) * max(0, count - 1)
            totalSumPhi += (log.event.time - clock) * min(1, count)

            clock = log.event.time

            if (log.event.type == EventType.ARRIVAL) {
                count++
                startTime[log.event.customerId] = clock
            } else {
                count--
                val waitingTime = clock - startTime[log.event.customerId]!!
                totalSumW += waitingTime
                totalSumWQ += waitingTime - serviceTimeForCustomer[log.event.customerId]!!
            }
        }
        val L = totalSumL / clock
        val LQ = totalSumLQ / clock
        val W = totalSumW / clock
        val WQ = totalSumWQ / clock
        val phi = totalSumPhi / clock

        println("L[$nodeId] : $L | LQ[$nodeId] : $LQ | W[$nodeId] : $W | WQ[$nodeId] : $WQ | phi[$nodeId] : $phi  ")
    }
}