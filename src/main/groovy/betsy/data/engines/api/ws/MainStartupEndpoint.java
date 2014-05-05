package betsy.data.engines.api.ws;

import javax.xml.ws.Endpoint;

public class MainStartupEndpoint {

    public static void main(String[] args) {
        Endpoint.publish("http://localhost:1234/EngineSelectorService", new EngineSelectorService());
        Endpoint.publish("http://localhost:1234/EngineService", new EngineService());
        System.out.println("WS STARTED");
    }
}
