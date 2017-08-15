package com.wiseweb.weibo.login;

import com.wiseweb.cat.base.GlobalThreadPool;
import com.wiseweb.gathers.GatherClient;
import com.wiseweb.json.JSONObject;
import com.wiseweb.tools.MD5;
import com.wiseweb.tools.Proxy;
import com.wiseweb.tools.RSAUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * Created by ty on 2017/8/7.
 */
public class WeiboLogin extends GatherClient {
    public static void main(String[] args) throws InterruptedException {
        String gsid = new WeiboLogin().getGsid("zdr748855@sina.cn", "qqq12345");
        if (gsid != null) {
            System.out.println(new WeiboLogin().getAccessTokenByGsid(gsid));
        }

    }

    /**
     * get请求
     *
     * @param config  代理ip
     * @param headers 请求头
     * @param url     url
     * @return
     */
    public String get(RequestConfig config, JSONObject headers, String url) {
        HttpResponse response = httpclient.Request(config, null, headers, url);
        String html;
        if (response != null) {
            try {
                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        html = EntityUtils.toString(entity);
                        return html;
                    }
                }
            } catch (IOException e) {
                System.out.println("发送失败");
                return null;
            }
        }
        return null;
    }

    /**
     * post请求
     *
     * @param config  代理ip
     * @param headers 请求头
     * @param url     url
     * @return
     */
    public String post(RequestConfig config, JSONObject form, JSONObject headers, String url) {
        HttpResponse response = httpclient.Request(config, form, headers, url);
        if (response != null) {
            String result;
            try {
                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        result = EntityUtils.toString(entity);
                        return result;
                    }
                } else {
                    System.err.println("status code:" + response.getStatusLine().getStatusCode());
                }
            } catch (IOException e) {
                System.out.println("发送失败");
                return null;
            }
            return null;
        }
        return null;
    }

    /**
     * 传入用户名、密码，自动登录（识别验证码），获取gsid
     *
     * @param userName 用户名
     * @param password 密码
     * @return gsid gsid
     */
    public String getGsid(String userName, String password) throws InterruptedException {
        String gsid;
        String p = passwordRSA(password);
        String s = getS(userName, password);
        JSONObject form = new JSONObject();
        String loginUrl = "http://api.weibo.cn/2/account/login?c=windows8";
        form.put("u", userName);
        form.put("s", s);
        form.put("p", p);
        String content = post(null, form, null, loginUrl);
        JSONObject response = new JSONObject(content);
        if (response.has("gsid")) {
            gsid = response.getString("gsid");
            System.out.println("登入成功" + gsid);
            return gsid;
        }
        String location = "pick/weibo/sourcepictures/";          //保存原型图片的地址
        String editLocation = "pick/weibo/editpicture/";         //保存处理后的图片地址
        int count = 1;            //记录是第几张图片
        if (response.has("errmsg")) {
            String errmsg = response.getString("errmsg");
            while (true) {
                if (errmsg.indexOf("验证码") != -1) {
                    count++;
                    RequestConfig config = RequestConfig.custom().setProxy(Proxy.getProxy()).build();// 获取一个新的代理
                    Map<String, String> map = getCpt();
                    String cpt = map.get("cpt");
                    String pic = map.get("pic");
                    saveImages(config, userName, pic, location);                               //多线程保存图片
                    EditImage.handleWeiboImage(userName, location, editLocation);                 //编辑图片
                    Set<String> results = new OCR().result(userName, editLocation);            //识别验证码
                    System.out.println("开始识别第" + count + "张验证码" + "cpt" + cpt);
                    if (results != null && results.size() > 0) {
                        for (String result : results) {
                            String url = loginUrl + "&cpt=" + cpt + "&cptcode=" + result;
                            content = post(config, form, null, url);
                            response = new JSONObject(content);
                            System.out.println(response);
                            if (response.has("errmsg")){
                                if(   response.getString("errmsg").indexOf("验证码")!=-1 ) {
                                    continue;
                                }
                                if (  response.getString("errmsg").indexOf("激活")!=-1||response.getString("errmsg").indexOf("异常")!=-1) {
                                    info("用户名不可用，请激活");
                                    return null;
                                }
                            }else {
                                gsid = response.getString("gsid");
                                System.out.println("登入成功" + gsid);
                                return gsid;
                            }
                        }
                    } else {
                        continue;
                    }

                } else {
                    info("校验参数不存在");
                    return null;
                }

            }
        }
        return null;
    }

    /**
     * 获得S参数
     *
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    public static String getS(String userName, String password) {
        String uid = userName;
        String pwd = password;
        String key = "wc5Vy7ptfWHOxUpugGWpjiu4uXvVJY1a";
        String md5 = uid + pwd + key;
        String str2 = MD5.Encrypt(md5);
        String s = "" + str2.charAt(1) + str2.charAt(5) + str2.charAt(2) + str2.charAt(10) + str2.charAt(17) + str2.charAt(9) + str2.charAt(25) + str2.charAt(27);
        System.out.println(s);
        return s;
    }

    /**
     * 获得S
     *
     * @param password
     * @return
     */
    public String passwordRSA(String password) {
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC46y69c1rmEk6btBLCPgxJkCxdDcAH9k7kBLffgG1KWqUErjdv+aMkEZmBaprEW846YEwBn60gyBih3KU518fL3F+sv2b6xEeOxgjWO+NPgSWmT3q1up95HmmLHlgVwqTKqRUHd8+Tr43D5h+J8T69etX0YNdT5ACvm+Ar0HdarwIDAQAB";
        String aData = RSAUtil.encrypt(publicKey, password);
        System.out.println(aData);
        return aData.replace("\r\n", "");
    }

    /**
     * 获得验证码地址和登录参数
     *
     * @return
     */
    public Map<String, String> getCpt() {
        Map<String, String> map = new HashMap<>();
        String url = "http://api.weibo.cn/2/captcha/get";
        String html = get(null, null, url);
        JSONObject jsonObject = new JSONObject(html);
        String cpt = jsonObject.getString("cpt");
        String pic = jsonObject.getString("pic");
        map.put("cpt", cpt);
        map.put("pic", pic);
        return map;
    }


    /**
     * 10张图片10个线程去跑 保存
     *
     * @param config   代理IP
     * @param userName 用户名
     * @param pic      图片地址
     * @param location 保存图片地址
     */
    public void saveImages(final RequestConfig config, String userName, final String pic, String location) {
        final CountDownLatch latch = new CountDownLatch(10);
        for (int i = 1; i <= 10; i++) {
            final String path = location + userName + "_" + i + ".gif";
            GlobalThreadPool.instance.submit(new Callable() {
                @Override
                public Object call() throws Exception {
                    downloadImage(config, pic, path);
                    latch.countDown();
                    return null;
                }
            });
        }
        try {
            int sleep = 0;
            do {
                Thread.sleep(300);
                sleep++;
                System.out.println(String.format("[Save image] latch Current Count is %d sleep %d", latch.getCount(), sleep));
            } while (latch.getCount() != 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
        }
    }

    /**
     * 下载保存图片
     *
     * @param config
     * @param address
     * @param savePath
     */
    public void downloadImage(RequestConfig config, String address, String savePath) {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(address);
        httpGet.setConfig(config);
        try {
            httpClient.execute(httpGet);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                byte[] data = EntityUtils.toByteArray(response.getEntity());
                FileOutputStream fos = new FileOutputStream(savePath);
                fos.write(data);
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("保存图片失败");
        }
    }

    /**
     * 传入gsid，获取access_token
     *
     * @param gsid
     * @return
     */
    public String getAccessTokenByGsid(String gsid) {
        Token token = new Token();
        String code = token.getCode(gsid);
        String token_value = token.getToken(code);
        return token_value;
    }

    @Override
    public String runGather() throws Exception {
        return null;
    }

    @Override
    protected JSONObject getTarget(String query) {
        return null;
    }

    @Override
    protected JSONObject gather() throws Exception {
        return null;
    }
}
