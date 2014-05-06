package betsy.tasks

import java.nio.file.Path

class ZipTasks {

    public static void zipFolder(Path tempZipFile, Path folder) {
        println "Creating zip archive $tempZipFile using the contents of $folder"
        new AntBuilder().zip(destfile: tempZipFile, basedir: folder)
    }

    public static void unzip(Path tempZipFile, Path tempExtractedFolder) {
        println "Unzipping $tempZipFile to $tempExtractedFolder"
        new AntBuilder().unzip(src: tempZipFile, dest: tempExtractedFolder)
    }

}
