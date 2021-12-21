object Config {
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

    val sampleNetwork3 = QueueNetworkDescription(
        startNodes = listOf(
            StartNodeDescription(
                id = 1,
                serviceRate = 2.0,
                arrivalRate = 1.0
            ),
            StartNodeDescription(
                id = 2,
                serviceRate = 3.0,
                arrivalRate = 2.0
            )
        ),
        queueNodes = listOf(
            QueueNodeDescription(
                id = 3,
                serviceRate = 4.0
            ),
            QueueNodeDescription(
                id = 4,
                serviceRate = 3.0
            ),
            QueueNodeDescription(
                id = 5,
                serviceRate = 2.0
            ),
            QueueNodeDescription(
                id = 6,
                serviceRate = 8.0
            )
        ),
        edges = listOf(
            EdgeDescription(1, 3, 0.4),
            EdgeDescription(1, 4, 0.6),
            EdgeDescription(2, 5, 1.0),
            EdgeDescription(5, 6, 1.0),
            EdgeDescription(3, 6, 1.0),
            EdgeDescription(4, 6, 1.0)
        )
    )

    val sampleNetwork4 = QueueNetworkDescription(
        startNodes = listOf(
            StartNodeDescription(
                id = 1,
                serviceRate = 1.0,
                arrivalRate = 1.0
            ),
            StartNodeDescription(
                id = 2,
                serviceRate = 3.0,
                arrivalRate = 2.0
            )
        ),
        queueNodes = listOf(
            QueueNodeDescription(
                id = 3,
                serviceRate = 4.0
            ),
            QueueNodeDescription(
                id = 4,
                serviceRate = 3.0
            ),
            QueueNodeDescription(
                id = 5,
                serviceRate = 2.0
            ),
            QueueNodeDescription(
                id = 6,
                serviceRate = 8.0
            )
        ),
        edges = listOf(
            EdgeDescription(1, 3, 0.4),
            EdgeDescription(1, 4, 0.6),
            EdgeDescription(2, 5, 1.0),
            EdgeDescription(5, 6, 1.0),
            EdgeDescription(3, 6, 1.0),
            EdgeDescription(4, 6, 1.0)
        )
    )
}
