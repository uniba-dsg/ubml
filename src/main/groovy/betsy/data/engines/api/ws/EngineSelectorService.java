package betsy.data.engines.api.ws;

import betsy.data.engines.api.EngineSelectorImpl;
import betsy.data.engines.api.ZipFileBpelWsdl;

import javax.jws.WebService;

@WebService
public class EngineSelectorService {

    public String getMatchingEngine(ZipFileBpelWsdl zipFileBpelWsdl) {
        return new EngineSelectorImpl().getMatchingEngine(zipFileBpelWsdl).getName();
    }

}
