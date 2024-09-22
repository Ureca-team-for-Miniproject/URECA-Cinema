package com.ureca.external.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class KmdbOpenAPIRestService {
    private String key;
    private String host;

    public KmdbOpenAPIRestService(String key, String host) {
        this.key = key;
        this.host = host;
    }

    public String getDetailMovie(String movieNm) throws IOException {
        // URL connect
        StringBuilder urlBuilder = new StringBuilder(host);
        urlBuilder.append(
                "?"
                        + URLEncoder.encode("collection", "UTF-8")
                        + "="
                        + URLEncoder.encode("kmdb_new2", "UTF-8"));
        urlBuilder.append(
                "&"
                        + URLEncoder.encode("ServiceKey", "UTF-8")
                        + "="
                        + URLEncoder.encode(key, "UTF-8"));
        urlBuilder.append(
                "&" + URLEncoder.encode("detail", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8"));
        urlBuilder.append(
                "&"
                        + URLEncoder.encode("query", "UTF-8")
                        + "="
                        + URLEncoder.encode(movieNm, "UTF-8"));
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        BufferedReader rd;
        // HTTP GET 메서드 설정
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        // Response 저장
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line).append("\n");
        }
        rd.close();
        conn.disconnect();
        return sb.toString();
    }
}
