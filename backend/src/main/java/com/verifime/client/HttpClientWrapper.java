package com.verifime.client;

import java.io.IOException;
import java.net.http.HttpResponse;

public interface HttpClientWrapper {
    HttpResponse<String> send(String url) throws IOException, InterruptedException;
}
