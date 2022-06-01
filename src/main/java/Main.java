import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Main {
    private static final String REQUEST_URI = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";

    public static void main(String[] args) {
        doHttpRequest(REQUEST_URI);
    }

    private static void doHttpRequest(String uri) {
        try (CloseableHttpClient httpClient = createHttpClient()) {
            CloseableHttpResponse response = httpClient.execute(new HttpGet(uri));
            List<Cats> list = jsonToList(response.getEntity().getContent());
            list.stream().filter(value -> value.getUpvotes() != null && Integer.parseInt(value.getUpvotes()) > 0)
                    .forEach(System.out::println);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static CloseableHttpClient createHttpClient() {
        return HttpClientBuilder.create().setDefaultRequestConfig(RequestConfig.custom()
                .setConnectTimeout(5000)
                .setSocketTimeout(30000)
                .setRedirectsEnabled(false)
                .build()).build();
    }

    private static List<Cats> jsonToList(InputStream jsonData) {
        try {
            return new ObjectMapper().readValue(jsonData, new TypeReference<>() {});
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
}
