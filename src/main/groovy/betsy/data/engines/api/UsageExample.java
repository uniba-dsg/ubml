package betsy.data.engines.api;

public class UsageExample {

    public static void main(String[] args) {

        EngineSelector impl = null;
        EngineHandle eh = impl.getEngine("ode136");
        eh.install();
        eh.start();
        try {
            EndpointInformation ei = eh.deploy(new ZipFileBpelWsdl());
            // TODO call using ei
        } catch (DeploymentException e) {
            System.out.println("error during deployment");
            e.printStackTrace();
        }
        eh.stop();
        eh.uninstall();

    }

}
