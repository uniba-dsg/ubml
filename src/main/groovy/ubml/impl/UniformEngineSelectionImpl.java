package ubml.impl;

import bpp.executables.EngineSelector;
import ubml.model.BpelPackage;
import ubml.model.EngineId;
import ubml.model.UniformEngineSelection;
import ubml.helper.EngineHelper;
import ubml.helper.ZipFileHelper;
import betsy.data.engines.Engine;

import javax.jws.WebService;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@WebService
public class UniformEngineSelectionImpl implements UniformEngineSelection {

    @Override
    public EngineId getEngine(String name) {
        return EngineHelper.getAllEngines().stream().map(Engine::toString).filter(s -> s.equals(name)).map(s -> {
            EngineId id = new EngineId();
            id.setEngineId(s);
            return id;
        }).findFirst().orElseThrow(() -> new IllegalArgumentException("could not find engine " + name));
    }

    @Override
    public EngineId[] getEngines() {
        return EngineHelper.getAllEngines().stream().map(Engine::toString).map(s -> {
            EngineId id = new EngineId();
            id.setEngineId(s);
            return id;
        }).toArray(EngineId[]::new);
    }

    @Override
    public EngineId getMatchingEngine(BpelPackage bpelPackage) {
        try {
            Path path = ZipFileHelper.extractIntoTemporaryFolder(bpelPackage);
            Path bpelPath = ZipFileHelper.findBpelFileInPath(path);
            return getMatchingEngine(bpelPath);
        } catch (IOException e) {
            throw new IllegalArgumentException("error during extraction or finding the bpel file in the folder", e);
        }
    }

    private EngineId getMatchingEngine(Path bpelFile) {
        List<String> matchedEngines = getMatchingEngineByConformance(bpelFile);

        if (matchedEngines.isEmpty()) {
            throw new RuntimeException("no engine found for this process. At least one of the activities in the given process is not supported.");
        }

        List<String> supportedAssertionsPerEngine = new LinkedList<>();

        for(String matchedEngine : matchedEngines) {
            long numberOfSupportedAssertions = new EngineSelector().getNumberOfSupportedAssertions(matchedEngine);
            String result = prependLeadingZeros(numberOfSupportedAssertions);
            supportedAssertionsPerEngine.add(result + ";" + matchedEngine);
        }

        Collections.sort(supportedAssertionsPerEngine);

        return getEngine(supportedAssertionsPerEngine.get(0).split(";")[1]);
    }

    private String prependLeadingZeros(long numberOfSupportedAssertions) {
        String result = "";
        if(numberOfSupportedAssertions < 10) {
            result += "000";
        } else if(numberOfSupportedAssertions < 100) {
            result += "00";
        }else if(numberOfSupportedAssertions < 1000) {
            result += "0";
        }
        result += numberOfSupportedAssertions;
        return result;
    }

    private List<String> getMatchingEngineByConformance(Path bpelFile) {
        return new bpp.executables.EngineSelector().getSupportingEngines(bpelFile);
    }

}
