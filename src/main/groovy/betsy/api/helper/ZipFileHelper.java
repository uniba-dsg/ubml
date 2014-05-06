package betsy.api.helper;

import betsy.api.model.BpelPackage;
import betsy.api.model.DeployableBpelPackage;
import betsy.api.model.LogPackage;
import betsy.api.model.ZipFile;
import betsy.tasks.ZipTasks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class ZipFileHelper {

    public static ZipFile buildFromFolder(Path folder) throws IOException {
        Path tempZipFile = Files.createTempDirectory("ubml").resolve("zip-file.zip");
        ZipTasks.zipFolder(tempZipFile, folder);
        return createZipFileFromArchive(tempZipFile);
    }

    public static ZipFile createZipFileFromArchive(Path tempZipFile) throws IOException {
        ZipFile zipFile = new ZipFile();
        zipFile.setData(Files.readAllBytes(tempZipFile));
        return zipFile;
    }

    public static BpelPackage zipToBpel(ZipFile zipFile) {
        BpelPackage result = new BpelPackage();
        result.setData(zipFile.getData());
        return result;
    }

    public static DeployableBpelPackage zipToDeployableBpel(ZipFile zipFile) {
        DeployableBpelPackage result = new DeployableBpelPackage();
        result.setData(zipFile.getData());
        return result;
    }

    public static LogPackage zipToLog(ZipFile zipFile) {
        LogPackage result = new LogPackage();
        result.setData(zipFile.getData());
        return result;
    }

    public static Path extractIntoTemporaryFolder(ZipFile zipFile) throws IOException {
        Path tempZipFile = storeDataAsZipFile(zipFile);

        // unpack in another temporary folder
        Path tempExtractedFolder = Files.createTempDirectory("ubml");

        ZipTasks.unzip(tempZipFile, tempExtractedFolder);

        return tempExtractedFolder;
    }

    public static Path storeDataAsZipFile(ZipFile zipFile) throws IOException {
        Path tempZipFile = Files.createTempDirectory("ubml").resolve("zip-file.zip");

        // write zip file to temporary folder
        Files.write(tempZipFile, zipFile.getData());
        return tempZipFile;
    }

    public static Path findBpelFileInPath(Path path) throws IOException {
        return Files.find(path, 10, ZipFileHelper::isBpelFile).findFirst().
                orElseThrow(() -> new IllegalStateException("could not find any bpel files in path " + path));
    }

    public static List<Path> findWsdlFilesInPath(Path path) throws IOException {
        return Files.find(path, 10, ZipFileHelper::isWsdlFile).collect(Collectors.toList());
    }

    public static List<Path> findOtherFilesInPath(Path path) throws IOException {
        return Files.find(path, 10, ZipFileHelper::isNeitherWsdlNorDirectoryNorBpelFile).collect(Collectors.toList());
    }

    public static boolean isWsdlFile(Path path, BasicFileAttributes a) {
        return path.getFileName().toString().endsWith(".wsdl");
    }

    public static boolean isBpelFile(Path path, BasicFileAttributes a) {
        return path.getFileName().toString().endsWith(".bpel");
    }

    public static boolean isNeitherWsdlNorDirectoryNorBpelFile(Path path, BasicFileAttributes a) {
        return !isBpelFile(path, a) && !isWsdlFile(path, a) && !Files.isDirectory(path);
    }
}
