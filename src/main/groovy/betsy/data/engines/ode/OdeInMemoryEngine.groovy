package betsy.data.engines.ode

import javax.xml.namespace.QName
import java.nio.file.Path
import java.nio.file.Paths

class OdeInMemoryEngine extends OdeEngine {

    @Override
    String getName() {
        "ode-in-memory"
    }

    @Override
    Path getXsltPath() {
        Paths.get(OdeInMemoryEngine.class.getResource("/ode-in-memory").toURI())
    }

}