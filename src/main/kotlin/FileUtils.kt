import guru.nidi.graphviz.attribute.Attributes.attr
import guru.nidi.graphviz.attribute.Color
import guru.nidi.graphviz.attribute.Rank
import guru.nidi.graphviz.attribute.Style
import guru.nidi.graphviz.engine.Format
import guru.nidi.graphviz.engine.Graphviz
import guru.nidi.graphviz.model.Factory.*
import guru.nidi.graphviz.model.Graph
import javafx.application.Application
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.image.Image
import javafx.scene.text.FontWeight
import tornadofx.*
import java.io.File

interface SimulationApp {
    fun runSimulation(): String // returns report
}

class KueueNetworkFx : View() {
    val viewModel : ReportViewModel by inject()

    override val root = vbox {
        run {
            val imageUrl = "file:data/graph.png"
            // 1. Simple synchronous way via property
            println("-- load synchronously #1 -- ")
            val start = System.currentTimeMillis()
            hbox {
                imageview {
                    image = Image(imageUrl)
                    println("loaded for ${System.currentTimeMillis() - start} msecs")
                }
                alignment = Pos.CENTER
            }
            hbox {
                button("Run Simulation") {
                    textFill = javafx.scene.paint.Color.RED
                    action {
                        viewModel.onReportClicked(app as SimulationApp)
                    }
                }
                alignment = Pos.CENTER
            }
            println("finished after ${System.currentTimeMillis() - start} msecs")
            hbox {
                textarea(viewModel.report) {
                    fitToParentWidth()
                }
                alignment = Pos.CENTER
            }
        }
    }
}


class Styles : Stylesheet() {
    init {
        label {
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
            backgroundColor += c("#cecece")
        }
    }
}

object FileUtils {

    fun saveGraph(graph: Graph) {
        Graphviz.fromGraph(graph).height(200).render(Format.PNG).toFile(File("data/graph.png"))
    }
}

class ReportViewModel() : ViewModel() {
    val report = SimpleStringProperty()

    fun onReportClicked(simulationApp: SimulationApp) {
        report.value = simulationApp.runSimulation()
    }
}