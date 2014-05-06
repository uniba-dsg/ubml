package betsy.data.engines.api;

import betsy.data.engines.LocalEngine;
import betsy.data.engines.activebpel.ActiveBpelEngine;
import betsy.data.engines.bpelg.BpelgEngine;
import betsy.data.engines.bpelg.BpelgInMemoryEngine;
import betsy.data.engines.ode.Ode136Engine;
import betsy.data.engines.ode.Ode136InMemoryEngine;
import betsy.data.engines.ode.OdeEngine;
import betsy.data.engines.ode.OdeInMemoryEngine;
import betsy.data.engines.openesb.OpenEsb231Engine;
import betsy.data.engines.openesb.OpenEsb23Engine;
import betsy.data.engines.openesb.OpenEsbEngine;
import betsy.data.engines.orchestra.OrchestraEngine;
import betsy.data.engines.petalsesb.PetalsEsb41Engine;
import betsy.data.engines.petalsesb.PetalsEsbEngine;
import betsy.data.engines.wso2.Wso2Engine_v2_1_2;
import betsy.data.engines.wso2.Wso2Engine_v3_0_0;
import betsy.data.engines.wso2.Wso2Engine_v3_1_0;

import java.util.LinkedList;
import java.util.List;

public class EngineSelectorImpl implements EngineSelector {

    @Override
    public EngineManager getEngine(String name) {
        List<LocalEngine> engines = new LinkedList<>();
        engines.add(new Ode136Engine());
        engines.add(new Ode136InMemoryEngine());
        engines.add(new OdeEngine());
        engines.add(new OdeInMemoryEngine());

        engines.add(new BpelgInMemoryEngine());
        engines.add(new BpelgEngine());

        engines.add(new ActiveBpelEngine());

        engines.add(new OrchestraEngine());

        engines.add(new OpenEsb231Engine());
        engines.add(new OpenEsb23Engine());
        engines.add(new OpenEsbEngine());

        engines.add(new PetalsEsbEngine());
        engines.add(new PetalsEsb41Engine());

        engines.add(new Wso2Engine_v3_1_0());
        engines.add(new Wso2Engine_v3_0_0());
        engines.add(new Wso2Engine_v2_1_2());

        for(LocalEngine localEngine : engines) {
            if(localEngine.getName().equals(name)) {
                return new EngineManagerImpl(localEngine);
            }
        }

        throw new IllegalArgumentException("could not find engine with name " + name);
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
