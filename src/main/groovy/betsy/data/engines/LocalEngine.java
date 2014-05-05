package betsy.data.engines;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class LocalEngine extends Engine implements LocalEngineAPI {
    /**
     * The path <code>server/$engine</code>
     *
     * @return the path <code>server/$engine</code>
     */
    @Override
    public Path getServerPath() {
        return Paths.get("server").resolve(getName());
    }

    @Override
    public void uninstall() {
        try {
            FileUtils.deleteDirectory(getServerPath().toFile());
        } catch (IOException e) {
            throw new IllegalStateException("uninstall failed", e);
        }
    }
}
