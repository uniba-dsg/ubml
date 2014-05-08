package betsy.data.engines;


public interface EngineDeployer {

    void deploy();
    void undeploy();
    boolean isDeployed();

}
