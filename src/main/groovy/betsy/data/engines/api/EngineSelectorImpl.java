package betsy.data.engines.api;

import betsy.data.engines.ode.OdeEngine;

import java.util.List;

public class EngineSelectorImpl implements EngineSelector {

    @Override
    public EngineManager getEngine(String name) {
        if("ode".equals(name)) {
            return new EngineManagerImpl(new OdeEngine());
        }
        return null;
    }

    @Override
    public EngineManager getMatchingEngine(ZipFileBpelWsdl zipFileBpelWsdl) {
        List<String> matchedEngines = getMatchingEngineByConformance(zipFileBpelWsdl);

        if(matchedEngines.isEmpty()) {
            throw new RuntimeException("no engine found for this process. At least one of the activities in the given process is not supported.");
        }

        return getEngine(matchedEngines.get(0));
    }

    private List<String> getMatchingEngineByConformance(ZipFileBpelWsdl zipFileBpelWsdl) {
        // TODO use joergs code for this by parsing the BPEL file and determining which engine supports which feature
        return null;
    }

}
