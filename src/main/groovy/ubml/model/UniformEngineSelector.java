package ubml.model;

public interface UniformEngineSelector {
    public abstract EngineId getEngine(String name);

    public abstract EngineId[] getEngines();

    public abstract EngineId getMatchingEngine(BpelPackage bpelPackage);
}
