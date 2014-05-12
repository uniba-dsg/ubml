package ubml.impl;

import ubml.helper.EngineHelper;
import ubml.helper.ZipFileHelper;
import ubml.model.*;
import betsy.data.BetsyProcess;
import betsy.data.engines.LocalEngine;
import org.xml.sax.SAXException;

import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebService
public class UniformProcessDeploymentImpl implements UniformProcessDeployment {

    @Override
    public ProcessId deploy(EngineId engineId, DeployableBpelPackage bpelPackage) {
        try {
            Path file = ZipFileHelper.storeDataAsZipFile(bpelPackage);
            LocalEngine engine = EngineHelper.getLocalEngine(engineId);

            Path extractedPath = ZipFileHelper.extractIntoTemporaryFolder(bpelPackage);
            String bpelName = ZipFileHelper.findBpelProcessNameInPath(extractedPath);
            String bpelNamespace = ZipFileHelper.findBpelTargetNameSpaceInPath(extractedPath);
            QName processId = new QName(bpelNamespace, bpelName);
            engine.deploy(processId, file);

            System.out.println("FOUND " + processId);

            ProcessId result = new ProcessId();
            result.setEngineId(engineId.getEngineId());
            result.setProcessId(processId);

            return result;
        } catch (IOException | SAXException | ParserConfigurationException | XPathExpressionException e) {
            throw new RuntimeException("errors in io", e);
        }
    }

    @Override
    public DeployableBpelPackage makeDeployable(EngineId engineId, BpelPackage bpelPackage) {
        LocalEngine engine = EngineHelper.getLocalEngine(engineId);
        try {
            Path folder  = ZipFileHelper.extractIntoTemporaryFolder(bpelPackage);

            ZipFileHelper.adjustFileNameOfBpelToProcessName(folder);
            Path bpelFile = ZipFileHelper.findBpelFileInPath(folder);

            BetsyProcess process = new BetsyProcess();
            process.setBpel(bpelFile);
            process.setWsdls(ZipFileHelper.findWsdlFilesInPath(folder));
            process.setEngine(engine);
            process.setAdditionalFiles(ZipFileHelper.findOtherFilesInPath(folder));
            engine.setParentFolder(Paths.get("test"));

            Path deployableArchivePath = engine.buildDeploymentArchive(process);
            return ZipFileHelper.zipToDeployableBpel(ZipFileHelper.createZipFileFromArchive(deployableArchivePath));
        } catch (IOException | SAXException | ParserConfigurationException | XPathExpressionException  e) {
            throw new RuntimeException("error due to io", e);
        }
    }

    @Override
    public ProcessId[] getDeployedProcesses(EngineId engineId) {
        return new ProcessId[0];
    }

    @Override
    public boolean isProcessDeployed(ProcessId processId) {
        LocalEngine engine = EngineHelper.getLocalEngine(processId);
        return engine.isDeployed(processId.getProcessId());
    }

    @Override
    public void undeploy(ProcessId processId) {
        LocalEngine engine = EngineHelper.getLocalEngine(processId);
        engine.undeploy(processId.getProcessId());
    }

}
