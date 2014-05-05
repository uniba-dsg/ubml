package betsy.data.engines.api;

public interface EngineSelector {

    EngineManager getEngine(String name);
    EngineManager getMatchingEngine(ZipFileBpelWsdl zipFileBpelWsdl);

}
