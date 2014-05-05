package betsy.data.engines.api;

import betsy.data.engines.LocalEngine;

public class EngineManagerImpl implements EngineManager {
    private LocalEngine engine;

    public EngineManagerImpl(LocalEngine engine) {
        this.engine = engine;
    }

    @Override
    public String getName() {
        return engine.getName();
    }

    @Override
    public void install() {
        engine.install();
    }

    @Override
    public void uninstall() {
        engine.uninstall();
    }

    @Override
    public void start() {
        engine.startup();
    }

    @Override
    public void stop() {
        engine.shutdown();
    }

    @Override
    public boolean isRunning() {
        return engine.isRunning();
    }

    @Override
    public EndpointInformation deploy(ZipFileBpelWsdl zipFileBpelWsdl) throws DeploymentException {
        return null;
    }

    @Override
    public ZipFileLogs retrieveLogFiles() {
        // TODO reuse engine.storeLogs logic
        return null;
    }
}
