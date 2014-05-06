package betsy.api.impl;

import betsy.api.helper.EngineHelper;
import betsy.api.helper.ZipFileHelper;
import betsy.api.model.*;
import betsy.data.BetsyProcess;
import betsy.data.engines.LocalEngine;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UniformProcessDeploymentImpl implements UniformProcessDeployment {

    @Override
    public ProcessId deploy(EngineId engineId, DeployableBpelPackage bpelPackage) {
        try {
            Path file = ZipFileHelper.storeDataAsZipFile(bpelPackage);
            LocalEngine engine = EngineHelper.getLocalEngine(engineId);

            //TODO determine bpel file name?
            String bpelName = "TOBEREPLACED";
            engine.deploy(bpelName, file);

            ProcessId result = new ProcessId();
            result.setEngineId(engineId.getEngineId());
            result.setProcessId(bpelName);

            return result;
        } catch (IOException e) {
            throw new RuntimeException("errors in io", e);
        }

    }

    @Override
    public DeployableBpelPackage makeDeployable(EngineId engineId, BpelPackage bpelPackage) {
        LocalEngine engine = EngineHelper.getLocalEngine(engineId);
        try {
            Path folder  = ZipFileHelper.extractIntoTemporaryFolder(bpelPackage);
            Path bpelFile = ZipFileHelper.findBpelFileInPath(folder);

            BetsyProcess process = new BetsyProcess();
            process.setBpel(bpelFile);
            process.setWsdls(ZipFileHelper.findWsdlFilesInPath(folder));
            process.setEngine(engine);
            process.setAdditionalFiles(ZipFileHelper.findOtherFilesInPath(folder));
            engine.setParentFolder(Paths.get("test"));

            Path deployableArchivePath = engine.buildDeploymentArchive(process);
            return ZipFileHelper.zipToDeployableBpel(ZipFileHelper.createZipFileFromArchive(deployableArchivePath));
        } catch (IOException e) {
            throw new RuntimeException("error due to io", e);
        }
    }

    @Override
    public boolean isDeployed(ProcessId processId) {
        return false;
    }

    @Override
    public void undeploy(ProcessId processId) {

    }

}
