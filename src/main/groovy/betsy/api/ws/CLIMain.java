package betsy.api.ws;

import betsy.api.impl.*;
import betsy.api.model.*;
import betsy.api.helper.IdHelper;
import betsy.api.helper.ZipFileHelper;
import betsy.tasks.WaitTasks;
import bpp.executables.EngineSelector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

public class CLIMain {

    public static final Path INVOKE_SYNC_FOLDER = Paths.get("testdata/Invoke-Sync");
    public static final Path VALIDATE_FOLDER = Paths.get("testdata/Validate");
    public static final Path SEQUENCE_FOLDER = Paths.get("testdata/Sequence");

    public static void main(String[] args) throws IOException {
        //testBppEngineSelector();
        //testUniformEngineSelector();
        //testProvisioningAndLifecycle();
        //testLogPackages();

        testDeployment();
    }

    private static void testDeployment() throws IOException {
        EngineId engineId = new EngineId();
        engineId.setEngineId("bpelg");

        UniformEngineProvisioner provisioner = new UniformEngineProvisionerImpl();
        if(!provisioner.isInstalled(engineId)) {
            provisioner.install(engineId);
        }
        UniformEngineLifecycle engineLifecycle = new UniformEngineLifecycleImpl();
        if(!engineLifecycle.isRunning(engineId)) {
            engineLifecycle.start(engineId);
        }

        UniformProcessDeployment processDeployment = new UniformProcessDeploymentImpl();
        DeployableBpelPackage deployableBpelPackage = processDeployment.makeDeployable(engineId, ZipFileHelper.zipToBpel(ZipFileHelper.buildFromFolder(SEQUENCE_FOLDER)));
        System.out.println("Created deployable package");
        System.out.println("Deployed process: " + IdHelper.toString(processDeployment.deploy(engineId, deployableBpelPackage)));

        if(engineLifecycle.isRunning(engineId)) {
            engineLifecycle.stop(engineId);
        }
    }

    private static void testLogPackages() throws IOException {
        EngineId ode = new EngineId();
        ode.setEngineId("ode");

        UniformEngineProvisioner provisioner = new UniformEngineProvisionerImpl();
        provisioner.install(ode);
        UniformEngineLifecycle engineLifecycle = new UniformEngineLifecycleImpl();
        engineLifecycle.start(ode);
        engineLifecycle.stop(ode);

        UniformEngineLogfileAccess logfileAccess = new UniformEngineLogfileAccessImpl();
        LogPackage logPackage = logfileAccess.retrieveLogFiles(ode);

        Path path = ZipFileHelper.extractIntoTemporaryFolder(logPackage);
        Files.find(path, 10, (s, a) -> true).forEach(System.out::println);
    }

    private static void testProvisioningAndLifecycle() {
        EngineId bpelg = new EngineId();
        bpelg.setEngineId("bpelg");

        UniformEngineProvisioner provisioner = new UniformEngineProvisionerImpl();
        System.out.println("bpelg isInstalled: " + provisioner.isInstalled(bpelg));
        provisioner.install(bpelg);
        System.out.println("bpelg isInstalled: " + provisioner.isInstalled(bpelg));

        UniformEngineLifecycle engineLifecycle = new UniformEngineLifecycleImpl();
        System.out.println("bpel isRunning: " + engineLifecycle.isRunning(bpelg));
        engineLifecycle.start(bpelg);
        System.out.println("bpel isRunning: " + engineLifecycle.isRunning(bpelg));

        WaitTasks.sleep(3000);

        engineLifecycle.stop(bpelg);
        System.out.println("bpel isRunning: " + engineLifecycle.isRunning(bpelg));

        provisioner.uninstall(bpelg);
        System.out.println("bpelg isInstalled: " + provisioner.isInstalled(bpelg));
    }

    private static void testUniformEngineSelector() throws IOException {
        UniformEngineSelector selector = new UniformEngineSelectorImpl();
        System.out.println(Arrays.toString(IdHelper.idsToString(selector.getEngines())));
        System.out.println(IdHelper.toString(selector.getMatchingEngine(ZipFileHelper.zipToBpel(ZipFileHelper.buildFromFolder(SEQUENCE_FOLDER)))));
        System.out.println(IdHelper.toString(selector.getEngine("ode")));
    }

    private static void testBppEngineSelector() throws IOException {
        System.out.println(new EngineSelector().getSupportingEngines(ZipFileHelper.findBpelFileInPath(INVOKE_SYNC_FOLDER)));
        System.out.println(new EngineSelector().getSupportingEngines(ZipFileHelper.findBpelFileInPath(VALIDATE_FOLDER)));
        System.out.println(new EngineSelector().getSupportingEngines(ZipFileHelper.findBpelFileInPath(SEQUENCE_FOLDER)));
    }

}
