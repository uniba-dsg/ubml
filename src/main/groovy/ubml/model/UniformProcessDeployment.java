package ubml.model;

public interface UniformProcessDeployment {
    public abstract ProcessId deploy(EngineId engineId, DeployableBpelPackage bpelPackage);

    public abstract void undeploy(ProcessId processId);

    public abstract DeployableBpelPackage makeDeployable(EngineId engineId, BpelPackage bpelPackage);

    public abstract ProcessId[] getDeployedProcesses(EngineId engineId);

    public abstract boolean isProcessDeployed(ProcessId processId);
}
