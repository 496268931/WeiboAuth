package com.wiseweb.weibo.login;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ty on 2017/8/11.
 */
public class Request {

    /**
     * get请求
     * @param url      url地址
     * @param headers  请求头
     * @return         String
     */
    public String get(String url, JSONObject headers) {
        //String url="https://api.weibo.cn/2/account/login?c=android&cpt=2_680eee6c22232110&u=jan8865758%40sina.cn&s=256e9afb&cptcode=jjwd&p=KnfF03XFN5DSHTtLzSjki63rw4tIRHP1ZF7OeuaBeOWhZ37dVVTOA%2BBAg46X60o%2BtQ7bL%2FaQIO8PHVhW1PJMrw6MCqahlf8%2FKVTPpvLDTjiHKwmORXs1NIitIJ99WM2JEdjfbUhOQM9X8wfrlzdCEBYYmw%2BcQvZ9ezT6mBqDw%2Fc%3D";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        for (String key : headers.keySet()) {
            httpGet.setHeader(key, headers.getString(key));
        }
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .setRedirectsEnabled(false)
                .build();
        httpGet.setConfig(requestConfig);
        HttpResponse response;
        String html;
        try {
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    html = EntityUtils.toString(entity, "utf-8");
                    return html;
                }
            } else if (response.getStatusLine().getStatusCode() == 302) {
                Header[] location = response.getHeaders("Location");
                String code = "";
                for (Header header : location) {
                    code += header.toString();
                }
                return code;
            }
            httpClient.close();
        } catch (IOException e) {
            System.out.println("发送失败");
            return null;
        }
        return null;
    }

    /**
     *
     * @param url       url地址
     * @param headers   请求头
     * @param form      formb表单参数
     * @return          String
     */
    public String post(String url, JSONObject headers, JSONObject form) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .setRedirectsEnabled(false)
                .build();

        httpPost.setConfig(requestConfig);
        if (headers != null) {
            for (String key : headers.keySet()) {
                httpPost.setHeader(key, headers.getString(key));
            }
        }

        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        if (form != null) {
            for (String key : form.keySet()) {
                pairs.add(new BasicNameValuePair(key, form.getString(key)));
            }
        }
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(pairs);
            httpPost.setEntity(urlEncodedFormEntity);
            HttpResponse response;
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 302) {
                Header[] location = response.getHeaders("Location");
                String code = "";
                for (Header header : location) {
                    code += header.toString();
                }
                return code;
            } else if (response.getStatusLine().getStatusCode() == 200) {
                String html = EntityUtils.toString(response.getEntity());
                return html;
            }

            httpClient.close();
        } catch (IOException e) {
            // e.printStackTrace();
            System.out.println("发送失败");
            return null;

        }
        return null;
    }
}
