package betsy.api.helper;

import betsy.api.model.EngineId;
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

public class EngineHelper {
    public static LocalEngine getLocalEngine(EngineId engineId) {
        return getAllEngines().stream().filter(s -> s.getName().equals(engineId.getEngineId())).findFirst().
                orElseThrow(() -> new IllegalStateException("could not find the engine with the id " + IdHelper.toString(engineId)));
    }

    public static List<LocalEngine> getAllEngines() {
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
        return engines;
    }
}
