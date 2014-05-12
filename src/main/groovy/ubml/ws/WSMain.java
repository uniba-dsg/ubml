package ubml.ws;

import ubml.impl.*;

import javax.xml.ws.Endpoint;

public class WSMain {

    public static void main(String[] args) {
        Endpoint.publish("http://localhost:1234/UniformEngineLifecycle", new UniformEngineLifecycleImpl());
        Endpoint.publish("http://localhost:1234/UniformEngineProvisioner", new UniformEngineProvisionerImpl());
        Endpoint.publish("http://localhost:1234/UniformEngineSelector", new UniformEngineSelectorImpl());
        Endpoint.publish("http://localhost:1234/UniformLogfileAccess", new UniformLogfileAccessImpl());
        Endpoint.publish("http://localhost:1234/UniformProcessDeployment", new UniformProcessDeploymentImpl());

        System.out.println("UP AND RUNNING");
    }

}
