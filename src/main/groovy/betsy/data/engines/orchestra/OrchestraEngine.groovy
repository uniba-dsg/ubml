package betsy.data.engines.orchestra

import betsy.config.Configuration
import betsy.data.BetsyProcess
import betsy.data.engines.LocalEngine
import betsy.data.engines.tomcat.Tomcat
import betsy.tasks.FileTasks

import javax.xml.namespace.QName
import java.nio.file.Path

class OrchestraEngine extends LocalEngine {

    @Override
    String getName() {
        "orchestra"
    }

    Tomcat getTomcat() {
        new Tomcat(engineDir: serverPath)
    }

    @Override
    void install() {
        new OrchestraInstaller().install()
    }

    @Override
    void startup() {
        tomcat.startup()
    }

    @Override
    void shutdown() {
        tomcat.shutdown()
    }

    @Override
    boolean isRunning() {
        try {
            tomcat.failIfIsRunning()
            return false;
        } catch (Exception ignore) {
            return true;
        }
    }

    @Override
    String getEndpointUrl(String process) {
        // "${tomcat.tomcatUrl}/orchestra/${process.name}TestInterface"
        "${tomcat.tomcatUrl}/orchestra/${process}"
    }

    @Override
    void copyLogsIntoFolder(Path process) {
        FileTasks.mkdirs(process)
        ant.copy(todir: process) {
            ant.fileset(dir: tomcat.tomcatLogsDir)
            ant.fileset(dir: orchestraHome) {
                include(name: "error.txt")
            }
        }
    }

    @Override
    void undeploy(QName processId) {

    }

    @Override
    boolean isDeployed(QName name) {
        return false
    }

    @Override
    void deploy(QName name, Path process) {
        new OrchestraDeployer(
                orchestraHome: getOrchestraHome(),
                packageFilePath: process, //.targetPackageFilePath,
                antBinFolder: Configuration.antHome.resolve("bin").toAbsolutePath()
        ).deploy()
    }

    public Path getOrchestraHome() {
        serverPath.resolve("orchestra-cxf-tomcat-4.9.0")
    }

    public Path buildDeploymentArchive(BetsyProcess process) {
        packageBuilder.createFolderAndCopyProcessFilesToTarget(process)

        // engine specific steps
        packageBuilder.replaceEndpointTokenWithValue(process)
        packageBuilder.replacePartnerTokenWithValue(process)
        packageBuilder.bpelFolderToZipFile(process)

        return process.targetPackageFilePath
    }

}
