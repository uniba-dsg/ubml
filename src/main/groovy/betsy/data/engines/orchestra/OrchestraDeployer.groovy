package betsy.data.engines.orchestra

import ubml.helper.ZipFileHelper
import betsy.data.engines.EngineDeployer;
import betsy.tasks.ConsoleTasks

import java.nio.file.Path

class OrchestraDeployer implements EngineDeployer{

    Path orchestraHome
    Path packageFilePath
    Path antBinFolder

    void deploy() {
        ConsoleTasks.executeOnWindowsAndIgnoreError(ConsoleTasks.CliCommand.build(orchestraHome, antBinFolder.resolve("ant.bat")).values("deploy", "-Dbar=" + packageFilePath.toAbsolutePath()))
        ConsoleTasks.executeOnUnixAndIgnoreError(ConsoleTasks.CliCommand.build(orchestraHome, antBinFolder.resolve("ant")).values("deploy", "-Dbar=" + packageFilePath.toAbsolutePath()))
    }

    @Override
    void undeploy() {
        // super hack
        Path folder = ZipFileHelper.extractIntoTemporaryFolder(ZipFileHelper.createZipFileFromArchive(packageFilePath))
        String processName = ZipFileHelper.findBpelProcessNameInPath(folder)
        String targetNamespace = ZipFileHelper.findBpelTargetNameSpaceInPath(folder)
        String processQname = "{" + targetNamespace + "}" + processName;

        ConsoleTasks.executeOnWindowsAndIgnoreError(ConsoleTasks.CliCommand.build(orchestraHome, antBinFolder.resolve("ant.bat")).values("undeploy", "-Dprocess=" + processQname))
        ConsoleTasks.executeOnUnixAndIgnoreError(ConsoleTasks.CliCommand.build(orchestraHome, antBinFolder.resolve("ant")).values("undeploy", "-Dprocess=" + processQname))
    }

    @Override
    boolean isDeployed() {
        throw new UnsupportedOperationException();
    }
}
