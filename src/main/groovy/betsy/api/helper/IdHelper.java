package betsy.api.helper;

import betsy.api.model.EngineId;
import betsy.api.model.InstanceId;
import betsy.api.model.ProcessId;

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

    public static String toString(InstanceId s) {
        return s.getEngineId() + "/" + s.getProcessId() + "/" + s.getInstanceId();
    }

    public static String[] idsToString(ProcessId[] ids) {
        return Stream.of(ids).map(IdHelper::toString).toArray(String[]::new);
    }

    public static String[] idsToString(InstanceId[] ids) {
        return Stream.of(ids).map(IdHelper::toString).toArray(String[]::new);
    }

}
