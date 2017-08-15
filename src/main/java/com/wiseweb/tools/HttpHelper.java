package com.wiseweb.tools;

import com.wiseweb.cat.base.BaseClass;
import com.wiseweb.json.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by apple on 15/12/26.
 */
public class HttpHelper extends BaseClass {
    /**
     * 调用 get方法 请求
     *
     * @param routeurl 请求地址
     * @param host     请求地址
     * @return
     * @throws IOException
     */
    public HttpResponse doGET(String routeurl, String host) {
        HttpResponse response = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(routeurl);
            httpget.addHeader("Accept", "text/html,application / xhtml + xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            if (host != null) {
                httpget.addHeader("Host", host);
            }
            httpget.addHeader("Upgrade-Insecure-Requests", "1");
            httpget.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.97 Safari/537.36");
            response = httpclient.execute(httpget);
        } catch (IOException ex) {
            error("访问出现异常:", ex);
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                error("关闭链接异常", e);
            }
        }
        return response;

    }
//
//    public String doPOST(String route, Map<String, String> paras) {
//        String re = null;
//        try {
//            String exeURL = this.url + route;//执行
//            HttpClient httpclient = new DefaultHttpClient();
//            //请求超时
//            httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
//            //读取超时
//            //httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
//            HttpPost httpPost = this.postForm(exeURL, paras);
//            HttpClientContext cou;
//
//            HttpResponse response;
//            debug("POST访问" + exeURL);
//            response = httpclient.execute(httpPost);
//
//            int code = response.getStatusLine().getStatusCode();
//            if (code == 200) {
//                re = EntityUtils.toString(response.getEntity(), "utf-8");
//            } else {
//                info("访问失败:" + code);
//                return null;
//            }
//        } catch (IOException ex) {
//            error("访问出现异常:", ex);
//
//        }
//        return re;
//    }
//
//    /**
//     * 設置post參數
//     *
//     * @param url
//     * @param params
//     * @return
//     */
//    private HttpPost postForm(String url, Map<String, String> params) {
//
//        HttpPost httpost = new HttpPost(url);
//        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//        if (!params.isEmpty()) {
//
//            Set<String> keySet = params.keySet();
//            for (String key : keySet) {
//                nvps.add(new BasicNameValuePair(key, params.get(key)));
//            }
//        }
//        try {
//            info("set utf-8 form entity to httppost");
//            httpost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            error("不支持编码", e);
//            return null;
//        }
//
//        return httpost;
//    }

    /**
     * 异步请求
     *
     * @param requests
     * @return
     */
    public List<HttpResponse> AsyncRequest(Object requests) {
        try {
            CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom().build();
            try {
                httpclient.start();
                List<HttpResponse> responses = new ArrayList<HttpResponse>();
                HttpUriRequest[] arr = requests instanceof HttpUriRequest[] ? (HttpUriRequest[]) requests : null;
                List<HttpUriRequest> list = requests instanceof HttpUriRequest[] ? null : (List<HttpUriRequest>) requests;
                int count = requests instanceof HttpUriRequest[] ? arr.length : list.size();
                for (int i = 0; i < count; i++) {
                    debug(list.get(i).toString());
                    try {
                        responses.add(httpclient.execute(requests instanceof HttpUriRequest[] ? arr[i] : list.get(i), null).get());
                    } catch (Exception e) {
                        responses.add(null);
                        error("AsyncRequest(Object requests),For循环异常", e);
                    }
                }
                return responses;
            } finally {
                httpclient.close();
            }
        } catch (Exception e) {
            error("AsyncRequest(Object requests)异常", e);
        }
        return null;
    }

    /**
     * Http无代理请求
     *
     * @param form
     * @param headers
     * @param url
     * @return
     */
    public HttpResponse Request(JSONObject form, JSONObject headers, String url) {
        return Request(null, form, headers, url);
    }


    /**
     * Http代理请求
     *
     * @param config
     * @param form
     * @param headers
     * @param exeurl
     * @return
     */
    public HttpResponse Request(RequestConfig config, JSONObject form, JSONObject headers, String exeurl) {
        try {
            info(exeurl);
            CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom()
                    .setDefaultRequestConfig(getConfig())
                    .build();
            try {
                httpclient.start();
                URL url = new URL(exeurl);
                debug(exeurl);
                URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), null);
                Future<HttpResponse> future = httpclient.execute(getHttpUriRequest(config, form, headers, uri + ""), null);
                HttpResponse response = future.get();
                //执行成功获取返回Cookie信息
                if (response != null && headers != null) {
                    Header[] header = response.getHeaders("Set-Cookie");
                    if (header != null && header.length > 0) {
                        JSONObject Cookie = headers.has("Cookie") ? (headers.get("Cookie") instanceof String ? getCookie(headers.getString("Cookie")) : headers.getJSONObject("Cookie")) : new JSONObject();
                        for (int i = 0; i < header.length; i++) {
                            String[] sp = header[i].getValue().split(";")[0].split("=");
                            if (sp.length > 2) {
                                Cookie.put(sp[0], header[i].getValue().split(";")[0].split(sp[0] + "=")[1]);
                            } else {
                                Cookie.put(sp[0], sp.length > 1 ? sp[1] : "");
                            }
                        }
                        headers.put("Cookie", Cookie);
                    }
                }
                return response;
            } finally {
                httpclient.close();
            }
        } catch (Exception e) {
            error("Request(RequestConfig config,JSONObject form,JSONObject headers,String exeurl)异常", e);
        }
        return null;
    }

    /**
     * Http代理请求
     *
     * @param config
     * @param form
     * @param headers
     * @param exeurl
     * @return
     */
    public HttpResponse ReportRequest(RequestConfig config, JSONObject form, JSONObject headers, String exeurl) throws IOException, URISyntaxException, ExecutionException, InterruptedException {
        info(exeurl);
        CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom()
                .setDefaultRequestConfig(getConfig())
                .build();
        try {
            httpclient.start();
            URL url = new URL(exeurl);
            debug(exeurl);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), null);
            Future<HttpResponse> future = httpclient.execute(getHttpUriRequest(config, form, headers, uri + ""), null);
            HttpResponse response = future.get();
            //执行成功获取返回Cookie信息
            if (response != null && headers != null) {
                Header[] header = response.getHeaders("Set-Cookie");
                if (header != null && header.length > 0) {
                    JSONObject Cookie = headers.has("Cookie") ? (headers.get("Cookie") instanceof String ? getCookie(headers.getString("Cookie")) : headers.getJSONObject("Cookie")) : new JSONObject();
                    for (int i = 0; i < header.length; i++) {
                        String[] sp = header[i].getValue().split(";")[0].split("=");
                        if (sp.length > 2) {
                            Cookie.put(sp[0], header[i].getValue().split(";")[0].split(sp[0] + "=")[1]);
                        } else {
                            Cookie.put(sp[0], sp.length > 1 ? sp[1] : "");
                        }
                    }
                    headers.put("Cookie", Cookie);
                }
            }
            return response;
        } finally {
            httpclient.close();
        }
//        return null;
    }

    public HttpUriRequest getHttpUriRequest(RequestConfig config, JSONObject form, JSONObject headers, String url) {
        try {
            config = config != null ? config : getConfig();
            // url = URLDecoder.decode(url, "UTF-8");
            //  url = URLDecoder.decode(URLDecoder.decode(url, "UTF-8"), "UTF-8");
            HttpGet get = form == null ? new HttpGet(url) : null;
            HttpPost post = form == null ? null : new HttpPost(url);
            (get == null ? post : get).setConfig(config);
            //form不为Null,则POST请求
            if (form != null) {
                List<NameValuePair> params = getParams(form);
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "utf-8");
                post.setEntity(entity);
            }
            if (headers != null) {
                Iterator i = headers.keys();
                while (i.hasNext()) {
                    try {
                        String key = ((String) i.next()).trim();
                        if (key.equals("Cookie")) {
                            if (headers.get(key) instanceof String) {
                                (get == null ? post : get).addHeader("Cookie", headers.getString("Cookie").trim());
                            } else {
                                Iterator ic = headers.getJSONObject("Cookie").keys();
                                StringBuffer Cookies = new StringBuffer();
                                while (ic.hasNext()) {
                                    key = ((String) ic.next()).trim();
                                    Cookies.append(key + "=" + headers.getJSONObject("Cookie").get(key) + "; ");
                                }
                                if (Cookies.length() > 0) {
                                    String Cookie = Cookies.toString().substring(0, Cookies.toString().length() - 2);
                                    (get == null ? post : get).addHeader("Cookie", Cookie);
                                }
                                debug(Cookies.toString());
                            }
                        } else {
                            (get == null ? post : get).addHeader(key, headers.getString(key));
                        }
                    } catch (Exception e) {
                        error("getHttpUriRequest方法解析JSONObject异常", e);
                    }
                }
            }
            return (get == null ? post : get);
        } catch (Exception e) {
            error("getHttpUriRequest(RequestConfig config,JSONObject form,JSONObject headers,String url)异常", e);
        }
        return null;
    }


    /**
     * 下载图片
     *
     * @param imageUrl
     * @param headers
     * @return
     */
    public byte[] getImageByte(final String imageUrl, JSONObject headers) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            //设置Cookie信息
            if (headers != null) {
                Iterator i = headers.keys();
                while (i.hasNext()) {
                    try {
                        String key = ((String) i.next()).trim();
                        if (key.equals("Cookie")) {
                            if (headers.get(key) instanceof String) {
                                con.setRequestProperty("Cookie", headers.getString("Cookie").trim());
                            } else {
                                Iterator ic = headers.getJSONObject("Cookie").keys();
                                StringBuffer Cookies = new StringBuffer();
                                while (ic.hasNext()) {
                                    key = ((String) ic.next()).trim();
                                    Cookies.append(key + "=" + headers.getJSONObject("Cookie").get(key) + "; ");
                                }
                                if (Cookies.length() > 0) {
                                    String Cookie = Cookies.toString().substring(0, Cookies.toString().length() - 2);
                                    con.setRequestProperty("Cookie", Cookie);
                                }
                            }
                        } else {
                            con.setRequestProperty(key, headers.getString(key));
                        }
                    } catch (Exception e) {
                        error("getImageByte(final String imageUrl, JSONObject headers)解析JSONObject异常", e);
                    }
                }
            }
            //设置请求超时10s
            con.setConnectTimeout(10 * 1000);

            //执行成功获取返回Cookie信息
            if (con != null && headers != null) {
                Iterator<String> it = con.getHeaderFields().get("Set-Cookie").iterator();
                while (it.hasNext()) {
                    JSONObject Cookie = headers.has("Cookie") ? (headers.get("Cookie") instanceof String ? getCookie(headers.getString("Cookie")) : headers.getJSONObject("Cookie")) : new JSONObject();
                    String s = it.next().split(";")[0].trim();
                    String[] sp = s.split("; ")[0].split("=");
                    Cookie.put(sp[0], sp.length > 1 ? sp[1] : "");
                    headers.put("Cookie", Cookie);
                }
            }

            InputStream is = con.getInputStream();
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[100];
            int rc = 0;
            while ((rc = is.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            byte[] b = swapStream.toByteArray();
            swapStream.close();
            is.close();
            return b;
        } catch (Exception e) {
            error("getImageByte(final String imageUrl, JSONObject headers)异常", e);
        }
        return null;
    }

    /**
     * 封装String类型的Cookie
     *
     * @param Cookies
     * @return
     */
    public JSONObject getCookie(String Cookies) {
        JSONObject Cookie = new JSONObject();
        try {
            String[] arr = Cookies.split(";");
            for (int i = 0; i < arr.length; i++) {
                Cookie.put(arr[i].split("=")[0].trim(), arr[i].split("=").length > 1 ? arr[i].split("=")[1].trim() : "");
            }
        } catch (Exception e) {
            error("getCookie(String Cookies)异常", e);
        }
        return Cookie;
    }

    public RequestConfig getConfig() {
        try {
            return RequestConfig.custom()
                    .setSocketTimeout(100000)
                    .setConnectTimeout(50000)
                    .setConnectionRequestTimeout(50000)
                    .build();
        } catch (Exception e) {
            error("getConfig()异常", e);
        }
        return null;
    }

    /**
     * 设置代理
     *
     * @param ips
     * @return
     */
    public RequestConfig getConfig(String ips) {
        try {
            HttpHost proxy = new HttpHost(ips.split(":")[0].trim(), Integer.parseInt(ips.split(":")[1].trim()));
            return RequestConfig.custom()
                    .setSocketTimeout(10000)
                    .setConnectTimeout(10000)
                    .setConnectionRequestTimeout(10000)
                    .setProxy(proxy)
                    .build();
        } catch (Exception e) {
            error("getConfig(String ips)异常", e);
        }
        return null;
    }

    public String getEntity(HttpResponse response) {
        try {
            return getEntity(response, "UTF-8");
        } catch (Exception e) {
            error("getEntity(HttpResponse response)异常", e);
        }
        return null;
    }

    public String getEntity(HttpResponse response, String CharSet) {
        try {
            return EntityUtils.toString(response.getEntity(), CharSet);
        } catch (Exception e) {
            error("getEntity(HttpResponse response,String CharSet)异常", e);
        }
        return null;
    }


    /**
     * 添加请求参数
     *
     * @param form
     * @return
     */
    public List<NameValuePair> getParams(JSONObject form) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        try {
            Iterator i = form.keys();
            while (i.hasNext()) {
                try {
                    String key = ((String) i.next()).trim();
//                    params.add(new BasicNameValuePair(key.trim(), form.getString(key).trim()));

                    params.add(new BasicNameValuePair(key.trim(), form.get(key).toString().trim()));
                } catch (Exception e) {
                    error("getParams(JSONObject form)解析JSONObject异常", e);
                }
            }
        } catch (Exception e) {
            error("getParams(JSONObject form)异常", e);
        }
        return params;
    }


}
