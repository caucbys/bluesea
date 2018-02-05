/**
 * @(#)HttpUtil.java, Dec 17, 2014.
 * <p>
 * Copyright 2014 Tiger, Inc. All rights reserved.
 * Tiger PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.alphalion.crawl.application.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by liwt on 2/3/17.
 */
@Slf4j
public class HttpUtil {

    private static PoolingHttpClientConnectionManager connMgr;
    private static RequestConfig requestConfig;
    private static final int MAX_TIMEOUT = 10000;

    static {
        // 设置连接池
        connMgr = new PoolingHttpClientConnectionManager();
        // 设置连接池大小
        connMgr.setMaxTotal(100);
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());

        RequestConfig.Builder configBuilder = RequestConfig.custom();
        // 设置连接超时
        configBuilder.setConnectTimeout(MAX_TIMEOUT);
        // 设置读取超时
        configBuilder.setSocketTimeout(MAX_TIMEOUT);
        // 设置从连接池获取连接实例的超时
        configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
        requestConfig = configBuilder.build();
    }

    public static String get(String url) {
        try {
            Content content = Request.Get(url).connectTimeout(1000)
                    .socketTimeout(1000).execute().returnContent();
            return IOUtils.toString(content.asStream(), "UTF-8");
        } catch (Exception e) {
            log.error("http get {}", url, e);
        }
        return null;
    }

    public static String get(String url, int connectTimeout, int socketTimeout) {
        try {
            Content content = Request.Get(url).connectTimeout(connectTimeout)
                    .socketTimeout(socketTimeout).execute().returnContent();
            return IOUtils.toString(content.asStream(), "UTF-8");
        } catch (Exception e) {
            log.error("http get {}", url, e);
        }
        return null;
    }

    public static String get(String url, String accessToken, int connectTimeout, int socketTimeout) {
        try {
            Content content = Request.Get(url).setHeader("Authorization", "Bearer " + accessToken)
                    .connectTimeout(connectTimeout)
                    .socketTimeout(socketTimeout).execute().returnContent();
            return IOUtils.toString(content.asStream(), "UTF-8");
        } catch (Exception e) {
            log.error("http get {}", url, e);
        }
        return null;
    }

    public static String get(String url, String charset, int connectTimeout, int socketTimeout, Map<String, String> params) {
        try {
            StringBuilder urlSB = new StringBuilder(url);

            if (params != null) {
                int i = 0;
                for (Entry<String, String> param : params.entrySet()) {
                    if (i == 0) {
                        urlSB.append(url.indexOf("?") >= 0 ? '&' : '?').append(param.getKey()).append('=').append(param.getValue());
                    } else {
                        urlSB.append('&').append(param.getKey()).append('=').append(param.getValue());
                    }
                    ++i;
                }
            }

            Content content = Request.Get(urlSB.toString())
                    .connectTimeout(connectTimeout)
                    .socketTimeout(socketTimeout).execute().returnContent();

            return IOUtils.toString(content.asStream(), charset);
        } catch (Exception e) {
            log.error("http get {}", url, e);
        }

        return null;
    }

    public static String get(Executor executor, String url) {
        try {
            Content content = executor.execute(Request.Get(url).connectTimeout(1000)
                    .socketTimeout(1000)).returnContent();
            return IOUtils.toString(content.asStream(), "UTF-8");
        } catch (Exception e) {
            log.error("get url", e);
        }
        return null;
    }

    public static String get(String url, Map<String, String> requestProperties) {
        try {
            Request request = Request.Get(url);
            if (requestProperties != null) {
                for (Entry<String, String> entry : requestProperties.entrySet()) {
                    request.addHeader(entry.getKey(), entry.getValue());
                }
            }
            Content content = request.connectTimeout(3000).socketTimeout(3000).execute().returnContent();
            return IOUtils.toString(content.asStream(), "UTF-8");
        } catch (Exception e) {
            log.error("get url {}", url, e);
        }
        return null;
    }

    public static String get(String url, String accessToken, Map<String, Object> params) {
        try {
            StringBuilder urlSB = new StringBuilder(url);

            if (params != null) {
                int i = 0;
                for (Entry<String, Object> param : params.entrySet()) {
                    if (i == 0) {
                        urlSB.append(url.indexOf("?") >= 0 ? '&' : '?').append(param.getKey()).append('=').append(param.getValue());
                    } else {
                        urlSB.append('&').append(param.getKey()).append('=').append(param.getValue());
                    }
                    ++i;
                }
            }

            Content content = Request.Get(urlSB.toString()).setHeader("Authorization", "Bearer " + accessToken)
                    .connectTimeout(10000)
                    .socketTimeout(10000).execute().returnContent();

            return IOUtils.toString(content.asStream(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getByProxy(String url, Map<String, String> requestProperties) {
        try {
            Request request = Request.Get(url);
            if (requestProperties != null) {
                for (Entry<String, String> entry : requestProperties.entrySet()) {
                    request.addHeader(entry.getKey(), entry.getValue());
                }
            }
            HttpHost host = new HttpHost("us.stunnel.net", 110, "https");
            Content content = request.viaProxy(host).connectTimeout(5000).
                    socketTimeout(5000).execute().returnContent();
            return IOUtils.toString(content.asStream(), "UTF-8");
        } catch (Exception e) {
            log.error("get", e);
        }
        return null;
    }

    public static String post(String url, int connectTimeout, int socketTimeout) {
        try {
            Content content = Request.Post(url).connectTimeout(connectTimeout)
                    .socketTimeout(socketTimeout).execute().returnContent();
            IOUtils.toString(content.asStream(), "UTF-8");
        } catch (Exception e) {
            log.error("http post {}", url, e);
        }

        return null;
    }

    public static String postJson(String url, String body) throws IOException {
        Content content = Request.Post(url)
                .bodyString(body, ContentType.create("application/json"))
                .connectTimeout(10000).socketTimeout(10000)
                .execute().returnContent();
        return IOUtils.toString(content.asStream(), content.getType().getCharset());
    }

    public static String post(String url, Map<String, String> params) throws IOException {
        if (params == null) {
            return get(url);
        }

        List<NameValuePair> pairs;
        Form form = Form.form();

        for (Entry<String, String> entry : params.entrySet()) {
            form.add(entry.getKey(), entry.getValue());
        }
        pairs = form.build();

        Content content = Request.Post(url)
                .bodyForm(pairs, Charset.forName("UTF-8")).connectTimeout(10000)
                .socketTimeout(10000).execute().returnContent();

        return IOUtils.toString(content.asStream(), "UTF-8");
    }

    public static String post(String url, String accessToken, JSONObject json) throws IOException {
        if (json == null) {
            return get(url);
        }

        Content content = Request.Post(url).setHeader("Authorization", "Bearer " + accessToken)
                .bodyString(json.toString(), ContentType.APPLICATION_JSON).connectTimeout(10000)
                .socketTimeout(10000).execute().returnContent();

        return IOUtils.toString(content.asStream(), "UTF-8");
    }

    public static String post(Executor executor, String url,
                              Map<String, String> params) throws IOException {
        if (params == null) {
            return get(url);
        }

        List<NameValuePair> pairs;
        Form form = Form.form();

        for (Entry<String, String> entry : params.entrySet()) {
            form.add(entry.getKey(), entry.getValue());
        }
        pairs = form.build();

        Content content = executor.execute(Request.Post(url).bodyForm(
                pairs, Charset.forName("UTF-8")).connectTimeout(5000)
                .socketTimeout(5000)).returnContent();

        return IOUtils.toString(content.asStream(), "UTF-8");
    }

    public static String post(String url, List<Pair<String, String>> params, Map<String, String> requestProperties) throws IOException {
        if (params == null) {
            return get(url, null);
        }
        List<NameValuePair> pairs;
        Form form = Form.form();

        for (Pair<String, String> values : params) {
            form.add(values.getKey(), values.getValue());
        }
        pairs = form.build();
        Request request = Request.Post(url);
        if (requestProperties != null) {
            for (Entry<String, String> entry : requestProperties.entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Content content = request
                .bodyForm(pairs, Charset.forName("UTF-8")).connectTimeout(20000)
                .socketTimeout(20000).execute().returnContent();

        return IOUtils.toString(content.asStream(), "UTF-8");
    }


    /**
     * 发送 POST 请求（HTTP），K-V形式
     *
     * @param apiUrl API接口URL
     * @param params 参数map
     * @return
     */
    public static String doPost(String apiUrl, Map<String, Object> params) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String httpStr = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;

        try {
            httpPost.setConfig(requestConfig);
            List<NameValuePair> pairList = new ArrayList<>(params.size());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry
                        .getValue().toString());
                pairList.add(pair);
            }
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));
            response = httpClient.execute(httpPost);
            System.out.println(response.toString());
            HttpEntity entity = response.getEntity();
            httpStr = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }

    /**
     * 发送 SSL POST 请求（HTTPS），K-V形式
     *
     * @param apiUrl API接口URL
     * @param params 参数map
     * @return
     */
    public static String doPostSSL(String apiUrl, String header, Map<String, Object> params) {
        HttpResponse response = null;
        String httpStr = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        httpClient = (CloseableHttpClient) wrapClient(httpClient);
        try {
            HttpPost httpPost = new HttpPost(apiUrl);
            httpPost.addHeader("Authorization", header);
            httpPost.setConfig(requestConfig);
            List<NameValuePair> pairList = new ArrayList<NameValuePair>(params.size());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry
                        .getValue().toString());
                pairList.add(pair);
            }
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Consts.UTF_8));
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            }
            httpStr = EntityUtils.toString(entity, "utf-8");
        } catch (Exception e) {
            log.error("doPostSSL error:{}", e);
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }


    /**
     * 避免HttpClient的”SSLPeerUnverifiedException: peer not authenticated”异常
     * 不用导入SSL证书
     * @param base
     * @return
     */
    public static HttpClient wrapClient(HttpClient base) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] arg0,
                                               String arg1) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] arg0,
                                               String arg1) throws CertificateException {
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(ctx, NoopHostnameVerifier.INSTANCE);
            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(ssf).build();
            return httpclient;
        } catch (Exception ex) {
            ex.printStackTrace();
            return HttpClients.createDefault();
        }
    }
}
