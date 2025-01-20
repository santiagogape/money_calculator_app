package software.external.control.loaders;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.net.URIBuilder;
import software.ulpgc.control.deserializers.CurrenciesDeserializer;
import software.ulpgc.control.loaders.CurrenciesLoader;
import software.ulpgc.model.API;
import software.ulpgc.model.Currency;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class HttpClientCurrenciesLoader implements CurrenciesLoader {

    private final API api;
    private final CurrenciesDeserializer deserializer;

    public HttpClientCurrenciesLoader(API api, CurrenciesDeserializer deserializer) {
        this.api = api;
        this.deserializer =deserializer;
    }

    @Override
    public List<Currency> load(String token) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            URI url = new URIBuilder()
                    .setScheme(api.scheme())
                    .setHost(api.host())
                    .setPath(api.endpoints().get(API.EndPointName.currencies).path())        // Ruta
                    .build();

            HttpGet request = new HttpGet(url);
            request.setHeader(api.access(), token);
            HttpClientResponseHandler<String> responseHandler = (ClassicHttpResponse response) -> {
                int status = response.getCode();
                if (status == 200) {
                    return EntityUtils.toString(response.getEntity());
                } else {
                    throw new IOException("Unexpected response status: " + status);
                }
            };

            String responseBody = client.execute(request, responseHandler);
            System.out.println(responseBody);
            return deserializer.deserialize(responseBody);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
