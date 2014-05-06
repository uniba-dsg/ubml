package betsy.data.engines.api;

import java.nio.file.Path;

public class ZipFileBpelWsdl {

    private final Path bpelFile;

    public ZipFileBpelWsdl(Path bpelFile) {
        this.bpelFile = bpelFile;
    }

    public Path getBpelFile() {
        return bpelFile;
    }
}
