package betsy.data.engines.bpelg

import betsy.data.BetsyProcess
import betsy.data.engines.LocalEngine

import betsy.data.engines.tomcat.Tomcat
import betsy.tasks.FileTasks

import java.nio.file.Path

class BpelgEngine extends LocalEngine {

    @Override
    String getName() {
        "bpelg"
    }

    Path getDeploymentDir() {
        tomcat.tomcatDir.resolve("bpr")
    }

    @Override
    void copyLogsIntoFolder(Path process) {
        FileTasks.mkdirs(process)
        ant.copy(todir: process) {
            ant.fileset(dir: tomcat.tomcatLogsDir)
        }
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

    @Override
    String getEndpointUrl(String process) {
        // "${tomcat.tomcatUrl}/bpel-g/services/${process.name}TestInterfaceService"
        "${tomcat.tomcatUrl}/bpel-g/services/${process}"
    }

    Tomcat getTomcat() {
        new Tomcat(engineDir: serverPath)
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
    void install() {
        new BpelgInstaller(serverDir: serverPath).install()
    }

    @Override
    void deploy(String name, Path process) {
        new BpelgDeployer(processName: name,
                packageFilePath: process, //.targetPackageFilePath,
                deploymentDirPath: getDeploymentDir(),
                logFilePath: tomcat.tomcatLogsDir.resolve("bpelg.log")
        ).deploy()
    }

    @Override
    Path buildDeploymentArchive(BetsyProcess process) {
        packageBuilder.createFolderAndCopyProcessFilesToTarget(process)

        // deployment descriptor
        ant.xslt(in: process.bpelFilePath,
                out: process.targetBpelPath.resolve("deploy.xml"),
                style: xsltPath.resolve("bpelg_bpel_to_deploy_xml.xsl"))

        // remove unimplemented methods

        /*
        TODO maybe use a workaround here?
        Util.computeMatchingPattern(process).each { pattern ->
            ant.copy(file: process.targetBpelPath.resolve("TestInterface.wsdl"),
                    tofile: process.targetTmpPath.resolve("TestInterface.wsdl.before_removing_${pattern}"))
            ant.xslt(in: process.targetTmpPath.resolve("TestInterface.wsdl.before_removing_${pattern}"),
                    out: process.targetBpelPath.resolve("TestInterface.wsdl"),
                    style: xsltPath.resolve("bpelg_prepare_wsdl.xsl"), force: "yes") {
                param(name: "deletePattern", expression: pattern)
            }
        }
        */

        // uniquify service name
        ant.replace(file: process.targetBpelPath.resolve("TestInterface.wsdl"),
                token: "TestInterfaceService",
                value: "${process.name}TestInterfaceService")
        ant.replace(file: process.targetBpelPath.resolve("deploy.xml"),
                token: "TestInterfaceService",
                value: "${process.name}TestInterfaceService")

        packageBuilder.replaceEndpointTokenWithValue(process)
        packageBuilder.replacePartnerTokenWithValue(process)
        packageBuilder.bpelFolderToZipFile(process)

        return process.targetPackageFilePath
    }

}
