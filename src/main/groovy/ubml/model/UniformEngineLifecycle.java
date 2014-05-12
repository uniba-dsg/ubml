package ubml.model;

public interface UniformEngineLifecycle {
    public abstract void start(EngineId engineId);

    public abstract void stop(EngineId engineId);

    public abstract boolean isRunning(EngineId engineId);
}
