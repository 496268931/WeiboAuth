package com.wiseweb.weibo.login;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ty on 2017/8/10.
 */
public class Token {

    /**
     * 将gsid放入Cookie中，模拟已登录的请求，访问授权页面，获取code
     *
     * @param gsid
     * @return
     */
    public String getCode(String gsid) {
        String url = "https://api.weibo.com/oauth2/authorize?client_id=1642594939&redirect_uri=https://api.weibo.com/oauth2/default.html&response_type=code";
        String code_url = "https://api.weibo.com/oauth2/authorize";
        JSONObject headers = new JSONObject();
        headers.put("Accept", "text/html, application/xhtml+xml, image/jxr, */*");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36 Edge/15.15063");
        headers.put("Host", "api.weibo.com");
        headers.put("Accept-Encoding", "gzip, deflate, br");
        headers.put("Referer", "https://api.weibo.com/oauth2/authorize");
        headers.put("Cookie", "SUB=" + gsid);
        String html = new Request().get(url, headers);
        if (html.indexOf("code") != -1) {
            Pattern pattern = Pattern.compile("code=(.*)");
            Matcher matcher = pattern.matcher(html);
            if (matcher.find()) {
                System.out.println(matcher.group(1));
                return matcher.group(1);
            }
        }
        JSONObject form = new JSONObject();
        Document documen = Jsoup.parse(html);
        Elements inputs = documen.select("div[class=oauth_login_box01 clearfix] input");
        for (Element element : inputs) {
            String name = element.attr("name");
            String value = element.attr("value");
            if (name != null && name.length() > 0 && value != null && value.length() > 0) {
                form.put(name, value);
            }
        }
        //String code_url = "https://api.weibo.com/oauth2/authorize";
        String code = new Request().post(code_url, headers, form);
        Pattern pattern = Pattern.compile("code=(.*)");
        Matcher matcher = pattern.matcher(code);
        if (matcher.find()) {
            System.out.println(matcher.group(1));
            return matcher.group(1);
        }
        return null;
    }

    /**
     * 根据code，完成授权（参照了weibo4j里的代码）
     * client_id和client_secret是应用的授权信息
     * @param code
     * @return
     */
    public String getToken(String code) {
        String url = "https://api.weibo.com/oauth2/access_token";
        JSONObject form = new JSONObject();
        form.put("client_id", "1642594939");
        form.put("client_secret", "cde3ffa9fb7e72f11d441dba492a9171");
        form.put("grant_type", "authorization_code");
        form.put("code", code);
        form.put("redirect_uri", "https://api.weibo.com/oauth2/default.html");
        String html = new Request().post(url, null, form);
        return html;
    }

}
