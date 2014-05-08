package betsy.api.helper;

import betsy.api.model.BpelPackage;
import betsy.api.model.DeployableBpelPackage;
import betsy.api.model.LogPackage;
import betsy.api.model.ZipFile;
import betsy.tasks.ZipTasks;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Iterator;
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

    public static String findBpelProcessNameInPath(Path path) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
        if(Files.isDirectory(path)) {
            return findBpelProcessNameInPath(findBpelFileInPath(path));
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(path.toFile());
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        xpath.setNamespaceContext( new NamespaceContext() {
            @Override
            public String getNamespaceURI(String prefix) {
                if("bpel".equals(prefix)) {
                    return "http://docs.oasis-open.org/wsbpel/2.0/process/executable";
                }
                return null;
            }

            @Override
            public String getPrefix(String namespaceURI) {
                return null;
            }

            @Override
            public Iterator getPrefixes(String namespaceURI) {
                return null;
            }
        });
        XPathExpression expr = xpath.compile("/bpel:process/@name");
        NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

        return nl.item(0).getTextContent();
    }

    public static String findBpelTargetNameSpaceInPath(Path path) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
        if(Files.isDirectory(path)) {
            return findBpelTargetNameSpaceInPath(findBpelFileInPath(path));
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(path.toFile());
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        xpath.setNamespaceContext( new NamespaceContext() {
            @Override
            public String getNamespaceURI(String prefix) {
                if("bpel".equals(prefix)) {
                    return "http://docs.oasis-open.org/wsbpel/2.0/process/executable";
                }
                return null;
            }

            @Override
            public String getPrefix(String namespaceURI) {
                return null;
            }

            @Override
            public Iterator getPrefixes(String namespaceURI) {
                return null;
            }
        });
        XPathExpression expr = xpath.compile("/bpel:process/@targetNamespace");
        NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

        return nl.item(0).getTextContent();
    }

    public static void adjustFileNameOfBpelToProcessName(Path folder) throws IOException, XPathExpressionException, SAXException, ParserConfigurationException {
        Path bpelFile = findBpelFileInPath(folder);
        String processName = findBpelProcessNameInPath(bpelFile);
        String correctBpelFileName = processName + ".bpel";
        if(!bpelFile.toString().endsWith(correctBpelFileName)){
            Files.move(bpelFile, bpelFile.getParent().resolve(correctBpelFileName));
        }
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
