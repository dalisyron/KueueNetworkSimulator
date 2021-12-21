import guru.nidi.graphviz.model.Graph
import tornadofx.App

class KueueNetworkSimulationApp : App(KueueNetworkFx::class, Styles::class), SimulationApp {

    private var queueNetwork: QueueNetwork? = null
    private var graph: Graph? = null

    init {
        queueNetwork = NetworkCreator.createNetworkFromDescription(KueueNetworkSimulationApp.config!!.networkDescription)
        graph = GraphvizGenerator.generateGraph(queueNetwork!!)
        FileUtils.saveGraph(graph!!)
    }

    override fun runSimulation(): String {
        queueNetwork?.let {
            val queueNetworkSimulator = QueueNetworkSimulator(it)
            val report = queueNetworkSimulator.simulate()
            return report
        }
        return ""
    }

    companion object {
        var config: Config? = null
    }

    class Config(
        var networkDescription: QueueNetworkDescription
    )
}