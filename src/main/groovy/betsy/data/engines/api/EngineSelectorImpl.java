package betsy.data.engines.api;

import betsy.data.engines.ode.Ode136Engine;
import betsy.data.engines.ode.OdeEngine;

import java.util.List;

public class EngineSelectorImpl implements EngineSelector {

    @Override
    public EngineManager getEngine(String name) {
        if("ode".equals(name)) {
            return new EngineManagerImpl(new OdeEngine());
        } else if("ode136".equals(name)){
            return new EngineManagerImpl(new Ode136Engine());
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
        return new bpp.executables.EngineSelector().getSupportingEngines(zipFileBpelWsdl.getBpelFile());
    }

}
