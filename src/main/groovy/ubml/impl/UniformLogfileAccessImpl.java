package ubml.impl;

import ubml.helper.EngineHelper;
import ubml.helper.ZipFileHelper;
import ubml.model.EngineId;
import ubml.model.LogPackage;
import ubml.model.UniformLogfileAccess;
import betsy.data.engines.LocalEngine;
import betsy.tasks.FileTasks;

import javax.jws.WebService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@WebService
public class UniformLogfileAccessImpl implements UniformLogfileAccess {

    @Override
    public LogPackage retrieveLogFiles(EngineId engineId) {
        LocalEngine engine = EngineHelper.getLocalEngine(engineId);

        Path logs = getLogFolder();
        FileTasks.mkdirs(logs);
        engine.copyLogsIntoFolder(logs);

        try {
            return ZipFileHelper.zipToLog(ZipFileHelper.buildFromFolder(logs));
        } catch (IOException e) {
            throw new RuntimeException("could not create zip file", e);
        }
    }

    private Path getLogFolder() {
        try {
            return Files.createTempDirectory("ubml").resolve("logs");
        } catch (IOException e) {
            throw new RuntimeException("could not create log folder", e);
        }
    }
}
