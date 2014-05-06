package betsy.api.impl;

import betsy.api.helper.EngineHelper;
import betsy.api.helper.ZipFileHelper;
import betsy.api.model.EngineId;
import betsy.api.model.LogPackage;
import betsy.api.model.UniformEngineLogfileAccess;
import betsy.data.engines.LocalEngine;
import betsy.tasks.FileTasks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class UniformEngineLogfileAccessImpl implements UniformEngineLogfileAccess {

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
