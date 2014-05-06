package betsy.api.impl;

import betsy.api.model.BpelPackage;
import betsy.api.model.EngineId;
import betsy.api.model.UniformEngineSelector;
import betsy.api.helper.EngineHelper;
import betsy.api.helper.ZipFileHelper;
import betsy.data.engines.Engine;

import javax.jws.WebService;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@WebService
public class UniformEngineSelectorImpl implements UniformEngineSelector {

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

    public EngineId getMatchingEngine(Path bpelFile) {
        List<String> matchedEngines = getMatchingEngineByConformance(bpelFile);

        if (matchedEngines.isEmpty()) {
            throw new RuntimeException("no engine found for this process. At least one of the activities in the given process is not supported.");
        }

        return getEngine(matchedEngines.get(0));
    }

    private List<String> getMatchingEngineByConformance(Path bpelFile) {
        return new bpp.executables.EngineSelector().getSupportingEngines(bpelFile);
    }

}
