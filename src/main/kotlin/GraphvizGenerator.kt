import guru.nidi.graphviz.attribute.Color
import guru.nidi.graphviz.attribute.Label
import guru.nidi.graphviz.attribute.Rank
import guru.nidi.graphviz.attribute.Shape
import guru.nidi.graphviz.engine.Graphviz
import guru.nidi.graphviz.model.*
import guru.nidi.graphviz.model.Factory.mutGraph
import guru.nidi.graphviz.model.Factory.mutNode
import guru.nidi.graphviz.model.Link
import java.math.RoundingMode
import java.text.DecimalFormat

object GraphvizGenerator {
    fun generateGraph(network: QueueNetwork): Graph {

        val linkSourceByNode = mutableMapOf<Int, MutableNode>()

        network.nodeById.values.forEach {
            val source: MutableNode = when (it) {
                is StartNode -> {
                    mutNode("${it.id}\nExp(${it.serviceRate})").add(Color.RED)
                }
                is QueueNode -> {
                    mutNode("${it.id}\nExp(${it.serviceRate})")
                }
            }
            linkSourceByNode[it.id] = source
        }

        val graph: MutableGraph = mutGraph("example1").setDirected(true).add(
            linkSourceByNode.values.toMutableList()
        ).graphAttrs().add(Rank.dir(Rank.RankDir.LEFT_TO_RIGHT))

        network.nodeById.values.forEach {
            it.outEdges?.let { edges ->
                edges.forEach { edge ->
                    if (edge.nextNode != null) {
                        val df = DecimalFormat("#.##")
                        df.roundingMode = RoundingMode.CEILING
                        val label = df.format(edge.probability)
                        linkSourceByNode[it.id]!!.addLink(Link.to(linkSourceByNode[edge.nextNode.id]!!).add(Label.of(label)))
                    }
                }
            }
        }
        network.startNodes.forEach { node ->
            graph.add(mutNode("Exp(${node.arrivalRate})").add(Shape.NONE).addLink(linkSourceByNode[node.id]!!))
        }
        return graph.toImmutable()
    }
}