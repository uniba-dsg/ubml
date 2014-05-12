package ubml.model;

public interface UniformEngineSelection {
    public abstract EngineId getEngine(String name);

    public abstract EngineId[] getEngines();

    public abstract EngineId getMatchingEngine(BpelPackage bpelPackage);
}
