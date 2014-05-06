package betsy.api.helper;

import betsy.api.model.BpelPackage;
import betsy.api.model.LogPackage;
import betsy.api.model.ZipFile;
import betsy.tasks.ZipTasks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ZipFileHelper {

    public static ZipFile buildFromFolder(Path folder) throws IOException {
        Path tempZipFile = Files.createTempDirectory("ubml").resolve("zip-file.zip");
        ZipTasks.zipFolder(tempZipFile, folder);
        ZipFile zipFile = new ZipFile();
        zipFile.setData(Files.readAllBytes(tempZipFile));
        return zipFile;
    }

    public static BpelPackage zipToBpel(ZipFile zipFile) {
        BpelPackage result = new BpelPackage();
        result.setData(zipFile.getData());
        return result;
    }

    public static LogPackage zipToLog(ZipFile zipFile) {
        LogPackage result = new LogPackage();
        result.setData(zipFile.getData());
        return result;
    }

    public static Path extractIntoTemporaryFolder(ZipFile zipFile) throws IOException {
        Path tempZipFile = Files.createTempDirectory("ubml").resolve("zip-file.zip");

        // write zip file to temporary folder
        Files.write(tempZipFile, zipFile.getData());

        // unpack in another temporary folder
        Path tempExtractedFolder = Files.createTempDirectory("ubml");

        ZipTasks.unzip(tempZipFile, tempExtractedFolder);

        return tempExtractedFolder;
    }

    public static Path findBpelFileInPath(Path path) throws IOException {
        return Files.find(path, 10, (p, a) -> p.getFileName().toString().endsWith(".bpel")).findFirst().
                orElseThrow(() -> new IllegalStateException("could not find any bpel files in path " + path));
    }

}
