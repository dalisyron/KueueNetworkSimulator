import kotlin.math.abs

data class QueueNetwork(
    val startNodes: List<StartNode>,
    val nodeById: Map<Int, Node>
)

sealed class Node(
    val id: Int,
    var outEdges: List<OutEdge>?,
    val serviceRate: Double,
    var queue: ArrayDeque<Int> = ArrayDeque()
) {
    fun isBusy(): Boolean = queue.isNotEmpty()
}

class QueueNode(
    id: Int,
    outEdges: List<OutEdge>?,
    serviceRate: Double
) : Node(id, outEdges, serviceRate) {
    init {
        // check outEdges are valid
        outEdges?.let { edges ->
            val probSum = edges.sumOf { it.probability }
            check(abs(probSum - 1) < eps)
        }
    }

    companion object {
        private const val eps = 1e-6
    }
}

class StartNode(
    id: Int,
    outEdges: List<OutEdge>?,
    serviceRate: Double,
    val arrivalRate: Double // exponential lambda
) : Node(id, outEdges, serviceRate)

data class OutEdge(
    val nextNode: Node?, // null if sink
    val probability: Double
)
