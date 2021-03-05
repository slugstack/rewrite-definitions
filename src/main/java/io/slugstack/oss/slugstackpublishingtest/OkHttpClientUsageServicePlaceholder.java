package io.slugstack.oss.slugstackpublishingtest;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;

/**
 * Just here to trigger some javadoc and some external dependency usage
 */
public class OkHttpClientUsageServicePlaceholder {
    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS))
            .build();

    /**
     * get response of a targetUrl using {@link okhttp3.OkHttpClient}
     * See {@link java.net.URL}
     *
     * @param targetUrl the targeturl to attempt a download
     */
    public void getTarget(String targetUrl) {
        Request request = new Request.Builder()
                .url(targetUrl)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}

