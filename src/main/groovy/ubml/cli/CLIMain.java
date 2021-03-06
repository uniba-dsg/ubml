package ubml.cli;

import ubml.impl.*;
import ubml.model.*;
import ubml.helper.IdHelper;
import ubml.helper.ZipFileHelper;
import betsy.tasks.WaitTasks;
import bpp.executables.EngineSelector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class CLIMain {

    public static final Path INVOKE_SYNC_FOLDER = Paths.get("testdata/Invoke-Sync");
    public static final Path VALIDATE_FOLDER = Paths.get("testdata/Validate");
    public static final Path SEQUENCE_FOLDER = Paths.get("testdata/Sequence");

    public static void main(String[] args) throws Exception {
        //testBppEngineSelector();
        //testUniformEngineSelector();
        //testProvisioningAndLifecycle();
        //testLogPackages();

        System.out.println(Arrays.toString(IdHelper.idsToString(new UniformEngineSelectionImpl().getEngines())));

        //testDeployment(SEQUENCE_FOLDER);
    }

    private static void testDeployment(Path folder) throws IOException {
        EngineId engineId = new EngineId();
        engineId.setEngineId("ode");

        UniformEngineLifecycle engineLifecycle = new UniformEngineLifecycleImpl();
        if(engineLifecycle.isRunning(engineId)) {
            engineLifecycle.stop(engineId);
        }

        UniformEngineProvisioning provisioner = new UniformEngineProvisioningImpl();
        provisioner.install(engineId);
        engineLifecycle.start(engineId);

        UniformProcessDeployment processDeployment = new UniformProcessDeploymentImpl();
        DeployableBpelPackage deployableBpelPackage = processDeployment.makeDeployable(engineId, ZipFileHelper.zipToBpel(ZipFileHelper.buildFromFolder(folder)));
        System.out.println("Created deployable package");
        ProcessId processId = processDeployment.deploy(engineId, deployableBpelPackage);
        System.out.println("Deployed process: " + IdHelper.toString(processId));

        WaitTasks.sleep(2000);

        System.out.println("Is deployed?: " + processDeployment.isProcessDeployed(processId));
        processDeployment.undeploy(processId);
        System.out.println("Is deployed?: " + processDeployment.isProcessDeployed(processId));
    }

    private static void testLogPackages() throws IOException {
        EngineId ode = new EngineId();
        ode.setEngineId("ode");

        UniformEngineProvisioning provisioner = new UniformEngineProvisioningImpl();
        provisioner.install(ode);
        UniformEngineLifecycle engineLifecycle = new UniformEngineLifecycleImpl();
        engineLifecycle.start(ode);
        engineLifecycle.stop(ode);

        UniformLogfileAccess logfileAccess = new UniformLogfileAccessImpl();
        LogPackage logPackage = logfileAccess.retrieveLogFiles(ode);

        Path path = ZipFileHelper.extractIntoTemporaryFolder(logPackage);
        Files.find(path, 10, (s, a) -> true).forEach(System.out::println);
    }

    private static void testProvisioningAndLifecycle() {
        EngineId bpelg = new EngineId();
        bpelg.setEngineId("bpelg");

        UniformEngineProvisioning provisioner = new UniformEngineProvisioningImpl();
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
        UniformEngineSelection selector = new UniformEngineSelectionImpl();
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
