package betsy.data.engines.openesb

import betsy.data.BetsyProcess
import betsy.data.engines.LocalEngine
import betsy.tasks.FileTasks

import javax.xml.namespace.QName
import java.nio.file.Files
import java.nio.file.Path

class OpenEsbEngine extends LocalEngine {

    static final String CHECK_URL = "http://localhost:18181"

    @Override
    String getName() {
        "openesb"
    }

    @Override
    String getEndpointUrl(String process) {
        // "${CHECK_URL}/${process.name}TestInterface"
        "${CHECK_URL}/${process}"
    }

    @Override
    void copyLogsIntoFolder(Path process) {
        FileTasks.mkdirs(process)
        ant.copy(todir: process) {
            ant.fileset(dir: glassfishHome.resolve("domains/domain1/logs/"))
        }
    }

    @Override
    void undeploy(QName processId) {

    }

    @Override
    boolean isDeployed(QName name) {
        return false
    }

    OpenEsbCLI getCli() {
        new OpenEsbCLI(glassfishHome: getGlassfishHome())
    }

    protected Path getGlassfishHome() {
        serverPath.resolve("glassfish")
    }

    @Override
    void startup() {
        cli.startDomain()
        ant.waitfor(maxwait: "15", maxwaitunit: "second", checkevery: "500") {
            http url: CHECK_URL
        }
    }

    @Override
    void shutdown() {
        cli.stopDomain()
    }

    @Override
    void install() {
        new OpenEsbInstaller().install()
    }

    @Override
    void deploy(QName name, Path process) {
        new OpenEsbDeployer(cli: cli,
                processName: name.getLocalPart(),
                packageFilePath: process, //.targetPackageCompositeFilePath,
                tmpFolder: Files.createTempDirectory("openesb-deploy")).deploy()
    }

    @Override
    public Path buildDeploymentArchive(BetsyProcess process) {
        packageBuilder.createFolderAndCopyProcessFilesToTarget(process)

        // engine specific steps
        buildDeploymentDescriptor(process)
        ant.replace(file: process.targetBpelPath.resolve("TestInterface.wsdl"),
                token: "TestInterfaceService", value: "${process.name}TestInterfaceService")

        packageBuilder.replaceEndpointTokenWithValue(process)
        packageBuilder.replacePartnerTokenWithValue(process)
        packageBuilder.bpelFolderToZipFile(process)

        return new OpenEsbCompositePackager(process: process).build()
    }

    void buildDeploymentDescriptor(BetsyProcess process) {
        Path metaDir = process.targetBpelPath.resolve("META-INF")
        Path catalogFile = metaDir.resolve("catalog.xml")
        FileTasks.mkdirs(metaDir)
        ant.echo file: catalogFile, message: """<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<catalog xmlns="urn:oasis:names:tc:entity:xmlns:xml:catalog" prefer="system">
</catalog>
        """
        ant.echo file: metaDir.resolve("MANIFEST.MF"), message: "Manifest-Version: 1.0"
        ant.xslt(in: process.targetBpelFilePath, out: metaDir.resolve("jbi.xml"),
                style: xsltPath.resolve("create_jbi_from_bpel.xsl"))
    }


    @Override
    boolean isRunning() {
        return isUrlAvailable(CHECK_URL);
    }

}
