package software.ulpgc.model;

import java.util.Map;


public record API(String scheme, String host, String access, Map<EndPointName, EndPoint> endpoints) {
    public record EndPoint(String path, Map<Parameter, String> parameters){}
    public enum Parameter{from,to,since,until}
    public enum EndPointName{currencies,rates,historic}
}
