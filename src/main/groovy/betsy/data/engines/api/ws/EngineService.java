package betsy.data.engines.api.ws;

import betsy.data.engines.api.EngineSelectorImpl;

import javax.jws.WebService;

@WebService
public class EngineService {

    public void start(String engineId) {
        new EngineSelectorImpl().getEngine(engineId).start();
    }

    public void stop(String engineId) {
        new EngineSelectorImpl().getEngine(engineId).stop();
    }

    public void install(String engineId) {
        new EngineSelectorImpl().getEngine(engineId).install();
    }

    public void uninstall(String engineId) {
        new EngineSelectorImpl().getEngine(engineId).uninstall();
    }

    public boolean isRunning(String engineId) {
        return new EngineSelectorImpl().getEngine(engineId).isRunning();
    }

}
