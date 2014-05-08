package betsy.data.engines.openesb

import betsy.data.engines.EngineDeployer

import java.nio.file.Path

class OpenEsbDeployer implements EngineDeployer{

    OpenEsbCLI cli

    String processName
    Path packageFilePath
    Path tmpFolder

    public void deploy() {
        cli.forceRedeploy(processName, packageFilePath, tmpFolder)
    }

    @Override
    void undeploy() {

    }

    @Override
    boolean isDeployed() {
        return false
    }
}
