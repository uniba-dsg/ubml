package betsy.data.engines

import betsy.data.BetsyProcess

import javax.xml.namespace.QName
import java.nio.file.Path;

interface EngineAPI extends EngineLifecycle {

    /**
     * The name of the engine.
     *
     * @return the unique name of the engine
     */
    String getName()

    /**
     * Deploy the given BPEL process to the current engine.
     * Deployment is always synchronous.
     *
     * @param name the name of the BPEL process
     * @param process the path to the archive to be deployed
     */
    void deploy(QName name, Path process)

    /**
     * Build archives required for deployment.
     *
     * @param process the process for which the archives are being built
     */
    Path buildDeploymentArchive(BetsyProcess process)

    /**
     * Gets endpoint url of requested endpoint url. This url is used for testing the process later on.
     *
     * @param process the process
     * @return the url of the endpoint
     */
    String getEndpointUrl(String serviceName)

    /**
     * Store logs used for a specific process. This is required for analysis in case of error.
     *
     * @param process the process for which to store the logs.
     */
    void copyLogsIntoFolder(Path targetFolder)

    void undeploy(QName processId)

    boolean isDeployed(QName name)
}
