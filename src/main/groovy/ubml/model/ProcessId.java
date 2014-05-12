package ubml.model;

import javax.xml.namespace.QName;

public class ProcessId extends EngineId {

    public QName getProcessId() {
        return processId;
    }

    public void setProcessId(QName processId) {
        this.processId = processId;
    }

    private QName processId;
}
