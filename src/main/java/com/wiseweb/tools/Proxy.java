package com.wiseweb.tools;

import com.wiseweb.cat.base.ConfigerFactory;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;

import java.util.Random;

/**
 * Created by Sane on 2017/5/15.
 */
public class Proxy {
    protected HttpHelper httpclient = new HttpHelper();
    public HttpHost getUsefulProxy(){
        String url = "http://123.56.154.24:5000/get/";
        HttpResponse response = httpclient.Request( null, null, url);
        if (response.getStatusLine().getStatusCode() == 200 && response != null) {
            String content = httpclient.getEntity(response);
            if(content.indexOf(":")!=-1){
                String[] values = content.split(":");
                return new HttpHost(values[0],Integer.parseInt(values[1]));
            }

        }
        return null;
    }

    public static HttpHost getProxy() {
        String proxiesStr = ConfigerFactory.getConfiger().get("PROXIES");
        String[] proxies = proxiesStr.split(",");
        Random random=new Random();
        int a=random.nextInt(proxies.length);
        String proxyStr = proxies[a];
        String ip = proxyStr.split(":")[0];
        String port = proxyStr.split(":")[1];
        HttpHost proxy = new HttpHost(ip, Integer.parseInt(port),"http");
        return proxy;
    }
}
