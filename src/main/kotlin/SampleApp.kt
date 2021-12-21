import javafx.application.Application

fun main(args: Array<String>) {
    KueueNetworkSimulationApp.config = KueueNetworkSimulationApp.Config(Config.sampleNetwork3)
    Application.launch(KueueNetworkSimulationApp::class.java, *args)
}