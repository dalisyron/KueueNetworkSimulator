object NetworkCreator {
    fun createNetworkFromDescription(networkDescription: QueueNetworkDescription): QueueNetwork {
        val startNodes = networkDescription.startNodes.map {
            StartNode(id = it.id, outEdges = null, serviceRate = it.serviceRate, arrivalRate = it.arrivalRate)
        }

        val queueNodes = networkDescription.queueNodes.map {
            QueueNode(id = it.id, outEdges = null, serviceRate = it.serviceRate)
        }

        val nodeById: Map<Int, Node> = startNodes.groupBy { it.id }.mapValues { it.value[0] } +
                queueNodes.groupBy { it.id }.mapValues { it.value[0] }

        val adjacencyList = mutableMapOf<Int, MutableList<OutEdge>>()

        for (edge in networkDescription.edges) {
            val outEdge = OutEdge(
                nextNode = nodeById[edge.destination],
                probability = edge.probability
            )
            adjacencyList.getOrPut(edge.source, ::mutableListOf).add(outEdge)
        }

        for (node in startNodes + queueNodes) {
            node.outEdges = adjacencyList[node.id]
        }

        val network = QueueNetwork(startNodes, nodeById)

        return network
    }
}