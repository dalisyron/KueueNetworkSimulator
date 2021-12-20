import kotlin.math.abs

fun main() {
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

    val sampleNetwork2 = QueueNetworkDescription(
        startNodes = listOf(
            StartNodeDescription(
                id = 1,
                serviceRate = 2.0,
                arrivalRate = 1.0
            )
        ),
        queueNodes = listOf(),
        edges = listOf()
    )
    val queueNetwork = NetworkCreator.createNetworkFromDescription(sampleNetwork)

    val simulator = QueueNetworkSimulator(queueNetwork)

    simulator.simulate()
}