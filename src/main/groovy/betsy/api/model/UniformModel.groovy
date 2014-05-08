package betsy.api.model

import javax.xml.namespace.QName

class EngineId                     { String engineId;   }
class ProcessId  extends EngineId  { QName processId;   }
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
    void undeploy(ProcessId processId);
    DeployableBpelPackage makeDeployable(EngineId engineId, BpelPackage bpelPackage);
    ProcessId[] getDeployedProcesses(EngineId engineId);
    ProcessId[] getDeployedProcesses();
    boolean isProcessDeployed(ProcessId processId);
}
interface UniformEngineLogfileAccess {
    LogPackage retrieveLogFiles(EngineId engineId);
}
interface UniformProcessManagement {
    EndpointReference[] getEPRs(ProcessId processId);
}


