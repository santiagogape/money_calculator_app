package software.external.control.loaders;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.net.URIBuilder;
import software.ulpgc.control.deserializers.ExchangeRatesDeserializer;
import software.ulpgc.control.loaders.HistoricExchangeRateLoader;
import software.ulpgc.model.API;
import software.ulpgc.model.Currency;
import software.ulpgc.model.Histogram;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static software.ulpgc.model.CurrencyReference.codesOf;

public class HttpClientHistoricExchangeRatesLoader implements HistoricExchangeRateLoader {

    private final ExchangeRatesDeserializer deserializer;
    private final API api;

    public HttpClientHistoricExchangeRatesLoader(API api, ExchangeRatesDeserializer deserializer) {
        this.api = api;
        this.deserializer = deserializer;
    }

    @Override
    public Histogram load(String token,
                          Currency from,
                          List<Currency> to,
                          ZonedDateTime since,
                          ZonedDateTime until) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            URI url = urlFor(from, to, since, until);
            System.out.println(url.toString());
            HttpGet request = new HttpGet(url);
            request.setHeader(api.access(), token);
            HttpClientResponseHandler<String> responseHandler = (ClassicHttpResponse response) -> {
                int status = response.getCode();
                if (status >= 200 && status < 300) {
                    return EntityUtils.toString(response.getEntity());
                } else {
                    throw new IOException("Unexpected response status: " + status);
                }
            };

            String responseBody = client.execute(request, responseHandler);
            return deserializer.deserialize(responseBody);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    private URI urlFor(Currency from, List<Currency> to, ZonedDateTime since, ZonedDateTime until) throws URISyntaxException {
        return new URIBuilder()
                .setScheme(api.scheme())
                .setHost(api.host())
                .setPath(api.endpoints().get(API.EndPointName.historic).path())
                .addParameter(api.endpoints().
                        get(API.EndPointName.historic)
                        .parameters()
                        .get(API.Parameter.since), formated(since))
                .addParameter(api.endpoints().
                        get(API.EndPointName.historic)
                        .parameters()
                        .get(API.Parameter.until), formated(until))
                .addParameter(api.endpoints().
                        get(API.EndPointName.historic)
                        .parameters()
                        .get(API.Parameter.from), from.code())
                .addParameter(api.endpoints().
                        get(API.EndPointName.historic)
                        .parameters()
                        .get(API.Parameter.to), codesOf(to))
                .build();
    }

    private String formated(ZonedDateTime zonedDateTime) {
        return zonedDateTime.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
