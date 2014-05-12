package ubml.model;

public interface UniformEngineProvisioner {
    public abstract void install(EngineId engineId);

    public abstract void uninstall(EngineId engineId);

    public abstract boolean isInstalled(EngineId engineId);
}
