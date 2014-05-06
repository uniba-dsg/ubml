package betsy.data.engines.ode

import betsy.data.BetsyProcess
import betsy.data.engines.LocalEngine
import betsy.data.engines.tomcat.Tomcat
import betsy.tasks.FileTasks

import java.nio.file.Path

class OdeEngine extends LocalEngine {

    @Override
    String getName() {
        "ode"
    }

    @Override
    String getEndpointUrl(String process) {
        // "${tomcat.tomcatUrl}/ode/processes/${process.name}TestInterface"
        "${tomcat.tomcatUrl}/ode/processes/${process}"
    }

    Path getDeploymentDir() {
        tomcat.tomcatDir.resolve("webapps/ode/WEB-INF/processes")
    }

    Tomcat getTomcat() {
        new Tomcat(engineDir: getServerPath())
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
    void copyLogsIntoFolder(Path process) {
        FileTasks.mkdirs(process)
        ant.copy(todir: process) {
            ant.fileset(dir: tomcat.tomcatLogsDir)
        }
    }

    @Override
    void install() {
        new OdeInstaller(serverDir: getServerPath()).install()
    }

    @Override
    void deploy(String name, Path process) {
        new OdeDeployer(processName: name,
                logFilePath: tomcat.tomcatLogsDir.resolve("ode.log"),
                deploymentDirPath: getDeploymentDir(),
                packageFilePath: process //.targetPackageFilePath
        ).deploy()
    }

    @Override
    Path buildDeploymentArchive(BetsyProcess process) {
        packageBuilder.createFolderAndCopyProcessFilesToTarget(process)

        // engine specific steps
        ant.xslt(in: process.bpelFilePath, out: process.targetBpelPath.resolve("deploy.xml"), style: xsltPath.resolve("bpel_to_ode_deploy_xml.xsl"))
        ant.replace(file: process.targetBpelPath.resolve("TestInterface.wsdl"), token: "TestInterfaceService", value: "${process.name}TestInterfaceService")
        ant.replace(file: process.targetBpelPath.resolve("deploy.xml"), token: "TestInterfaceService", value: "${process.name}TestInterfaceService")

        packageBuilder.replaceEndpointTokenWithValue(process)
        packageBuilder.replacePartnerTokenWithValue(process)

        packageBuilder.bpelFolderToZipFile(process)

        return process.targetPackageFilePath
    }

    @Override
    boolean isRunning() {
        try {
            tomcat.checkIfIsRunning()
            return false;
        } catch (Exception ignore) {
            return true;
        }
    }

}
