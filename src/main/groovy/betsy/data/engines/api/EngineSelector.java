package betsy.data.engines.api;

public interface EngineSelector {

    EngineHandle getEngineHandle(String name);
    EngineHandle getMatchingEngine(ZipFileBpelWsdl zipFileBpelWsdl);

}
