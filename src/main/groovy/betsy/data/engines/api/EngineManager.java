package betsy.data.engines.api;


public interface EngineManager {

    String getName();

    void install();
    void uninstall();

    void start();
    void stop();
    boolean isRunning();

    EndpointInformation deploy(ZipFileBpelWsdl zipFileBpelWsdl) throws DeploymentException;
    ZipFileLogs retrieveLogFiles();

}
