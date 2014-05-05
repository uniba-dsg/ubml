package betsy.data.engines.openesb

import ant.tasks.AntUtil
import betsy.config.Configuration;
import betsy.tasks.ConsoleTasks
import betsy.tasks.FileTasks

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class OpenEsbInstaller {

    final AntBuilder ant = AntUtil.builder()

    Path serverDir = Paths.get("server/openesb")
    String fileName = "glassfishesb-v2.2-full-installer-windows.exe"
    String downloadUrl = "https://lspi.wiai.uni-bamberg.de/svn/betsy/${fileName}"
    Path stateXmlTemplate = Paths.get(OpenEsbInstaller.class.getResource("/openesb/state.xml.template").toURI())

    public void install() {
        // setup engine folder
        FileTasks.mkdirs(serverDir)

        ant.get(dest: Configuration.get("downloads.dir"), skipexisting: true) {
            ant.url url: downloadUrl
        }

        FileTasks.deleteDirectory(serverDir)
        FileTasks.mkdirs(serverDir)

        Path stateXmlPath = serverDir.resolve("state.xml").toAbsolutePath()
        ant.copy file: stateXmlTemplate, tofile: stateXmlPath, {
            filterchain {
                replacetokens {
                    token key: "INSTALL_PATH", value: serverDir.toAbsolutePath()
                    token key: "JDK_LOCATION", value: System.getenv()['JAVA_HOME']
                    token key: "HTTP_PORT", value: 8383
                    token key: "HTTPS_PORT", value: 8384
                }
            }
        }

        Path reinstallGlassFishBatPath = serverDir.resolve("reinstallGlassFish.bat")
        Files.copy(Paths.get(OpenEsbInstaller.class.getResource("/openesb/reinstallGlassFish.bat").toURI()),
                reinstallGlassFishBatPath)

        ConsoleTasks.executeOnWindowsAndIgnoreError(ConsoleTasks.CliCommand.build(
                reinstallGlassFishBatPath).values(
                Configuration.downloadsDir.resolve(fileName).toString(),
                stateXmlPath.toString())
        )

    }
}
