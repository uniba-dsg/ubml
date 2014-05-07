package betsy.api.model

class EngineId                     { String engineId;   }
class ProcessId  extends EngineId  { String processId;  }
class InstanceId extends ProcessId { String instanceId; }
class ZipFile { byte[] data; }
class BpelPackage extends ZipFile { }
class DeployableBpelPackage extends BpelPackage {}
class LogPackage extends ZipFile {}
class EndpointReference { String url; }

interface UniformEngineSelector {
    EngineId getEngine(String name);
    EngineId[] getEngines();
    EngineId getMatchingEngine(BpelPackage bpelPackage);
}
interface UniformEngineProvisioner {
    void install(EngineId engineId);
    void uninstall(EngineId engineId);
    boolean isInstalled(EngineId engineId);
}
interface UniformEngineLifecycle {
    void start(EngineId engineId);
    void stop(EngineId engineId);
    boolean isRunning(EngineId engineId);
}
interface UniformProcessDeployment {
    ProcessId deploy(EngineId engineId, DeployableBpelPackage bpelPackage);
    DeployableBpelPackage makeDeployable(EngineId engineId, BpelPackage bpelPackage);
    boolean isDeployed(ProcessId processId);
    void undeploy(ProcessId processId);
}
interface UniformEngineLogfileAccess {
    LogPackage retrieveLogFiles(EngineId engineId);
}
interface UniformProcessManagement {
    EndpointReference[] getEPRs(ProcessId processId);
    ProcessId[] getProcesses(EngineId engineId);
}


