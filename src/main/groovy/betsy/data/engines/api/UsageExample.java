package betsy.data.engines.api;

public class UsageExample {

    public static void main(String[] args) {
        try {
            EndpointInformation ei = provideBpelProcess(new ZipFileBpelWsdl());
        } catch (DeploymentException e) {
            e.printStackTrace();
        }
    }

    private static EndpointInformation provideBpelProcess(ZipFileBpelWsdl zipFileBpelWsdl) throws DeploymentException {
        EngineSelector impl = null;
        EngineManager eh = impl.getMatchingEngine(zipFileBpelWsdl);
        eh.install();
        eh.start();
        return eh.deploy(zipFileBpelWsdl);
    }

    private static void sample1() {
        EngineSelector impl = null;
        EngineManager eh = impl.getEngine("ode136");
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
