import javafx.application.Application;

public class SampleAppJava {

    public static void main(String[] args) {
        KueueNetworkSimulationApp.Companion.setConfig(
                new KueueNetworkSimulationApp.Config(
                        Config.INSTANCE.getSampleNetwork3()
                )
        );
        Application.launch(KueueNetworkSimulationApp.class, args);
    }
}
