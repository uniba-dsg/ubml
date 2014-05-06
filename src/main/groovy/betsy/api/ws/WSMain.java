package betsy.api.ws;

import betsy.api.impl.UniformEngineLifecycleImpl;
import betsy.api.impl.UniformEngineProvisionerImpl;
import betsy.api.impl.UniformEngineSelectorImpl;

import javax.xml.ws.Endpoint;

public class WSMain {

    public static void main(String[] args) {
        Endpoint.publish("http://localhost:1234/UniformEngineLifecycle", new UniformEngineLifecycleImpl());
        Endpoint.publish("http://localhost:1234/UniformEngineProvisioner", new UniformEngineProvisionerImpl());
        Endpoint.publish("http://localhost:1234/UniformEngineSelector", new UniformEngineSelectorImpl());

        System.out.println("UP AND RUNNING");
    }

}
