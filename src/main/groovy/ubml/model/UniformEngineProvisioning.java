package ubml.model;

public interface UniformEngineProvisioning {
    public abstract void install(EngineId engineId);

    public abstract void uninstall(EngineId engineId);

    public abstract boolean isInstalled(EngineId engineId);
}
