import kotlin.math.abs

data class QueueNetworkDescription(
    val startNodes: List<StartNodeDescription>,
    val queueNodes: List<NodeDescription>,
    val edges: List<EdgeDescription>
)

sealed class NodeDescription(
    val id: Int,
    val serviceRate: Double
)

class QueueNodeDescription(
    id: Int,
    serviceRate: Double
) : NodeDescription(id, serviceRate)

class StartNodeDescription(
    id: Int,
    serviceRate: Double,
    val arrivalRate: Double // exponential lambda
) : NodeDescription(id, serviceRate)

data class EdgeDescription(
    val source: Int,
    val destination: Int,
    val probability: Double
)