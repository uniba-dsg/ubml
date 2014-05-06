package betsy.data.engines.api.ws;

import betsy.data.engines.api.ZipFileBpelWsdl;
import bpp.executables.EngineSelector;

import java.nio.file.Paths;

public class CLIMain {

    public static void main(String[] args) {

        System.out.println(new EngineSelector().getSupportingEngines(Paths.get("testdata/Invoke-Sync.bpel")));
        System.out.println(new EngineSelector().getSupportingEngines(Paths.get("testdata/Validate.bpel")));

        System.out.println(new EngineSelectorService().getMatchingEngine(new ZipFileBpelWsdl(Paths.get("testdata/Invoke-Sync.bpel"))));
        System.out.println(new EngineSelectorService().getMatchingEngine(new ZipFileBpelWsdl(Paths.get("testdata/Validate.bpel"))));

    }
}
