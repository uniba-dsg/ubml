package betsy.api.impl;

import betsy.api.EngineId;
import betsy.api.UniformEngineProvisioner;
import betsy.api.helper.EngineHelper;

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
