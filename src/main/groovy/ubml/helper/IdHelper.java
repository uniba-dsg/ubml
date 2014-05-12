package ubml.helper;

import ubml.model.EngineId;
import ubml.model.ProcessId;

import java.util.stream.Stream;

public class IdHelper {

    public static String[] idsToString(EngineId[] ids) {
        return Stream.of(ids).map(IdHelper::toString).toArray(String[]::new);
    }

    public static String toString(EngineId id) {
        return id.getEngineId();
    }

    public static String toString(ProcessId s) {
        return s.getEngineId() + "/" + s.getProcessId();
    }

    public static String[] idsToString(ProcessId[] ids) {
        return Stream.of(ids).map(IdHelper::toString).toArray(String[]::new);
    }

}
