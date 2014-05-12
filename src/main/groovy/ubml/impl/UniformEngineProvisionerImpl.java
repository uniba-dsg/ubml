package ubml.impl;

import ubml.model.EngineId;
import ubml.model.UniformEngineProvisioner;
import ubml.helper.EngineHelper;

import javax.jws.WebService;

@WebService
public class UniformEngineProvisionerImpl implements UniformEngineProvisioner {

    @Override
    public void install(EngineId engineId) {
        EngineHelper.getLocalEngine(engineId).install();
    }

    @Override
    public void uninstall(EngineId engineId) {
        EngineHelper.getLocalEngine(engineId).uninstall();
    }

    @Override
    public boolean isInstalled(EngineId engineId) {
        return EngineHelper.getLocalEngine(engineId).isInstalled();
    }

}
