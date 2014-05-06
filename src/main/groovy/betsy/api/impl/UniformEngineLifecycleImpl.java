package betsy.api.impl;

import betsy.api.model.EngineId;
import betsy.api.model.UniformEngineLifecycle;
import betsy.api.helper.EngineHelper;

import javax.jws.WebService;

@WebService
public class UniformEngineLifecycleImpl implements UniformEngineLifecycle {

    @Override
    public void start(EngineId engineId) {
        EngineHelper.getLocalEngine(engineId).startup();
    }

    @Override
    public void stop(EngineId engineId) {
        EngineHelper.getLocalEngine(engineId).shutdown();
    }

    @Override
    public boolean isRunning(EngineId engineId) {
        return EngineHelper.getLocalEngine(engineId).isRunning();
    }
}
