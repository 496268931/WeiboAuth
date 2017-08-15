package com.wiseweb.gathers;

import com.wiseweb.cat.base.BaseClass;
import com.wiseweb.cat.base.ConfigerFactory;
import com.wiseweb.json.JSONObject;
import com.wiseweb.tools.HttpHelper;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by apple on 16/1/21.
 */
public abstract class GatherClient extends BaseClass {
    public HttpHelper httpclient = new HttpHelper();

    public abstract String runGather() throws Exception;

    /**
     * 获取采集任务信息
     *
     * @param query
     * @return
     */
    protected abstract JSONObject getTarget(String query);

    /**
     * 采集任务信息
     *
     * @return
     */
    protected abstract JSONObject gather() throws Exception;


    protected void ReportDate(JSONObject form, String route) throws Exception {
        long startTime = System.currentTimeMillis();
        RequestConfig config = RequestConfig.custom()
                .setSocketTimeout(150000)
                .setConnectTimeout(50000)
                .setConnectionRequestTimeout(50000)
                .build();
        JSONObject headers = new JSONObject();
        headers.put("Connection", "close");
        HttpResponse response = httpclient.ReportRequest(config, form, headers, ConfigerFactory.getConfiger().get("API_URL") + route);
        if (response != null && response.getStatusLine().getStatusCode() == 200) {
            info("上报信息：" + form + "\n上报结果：" + response.getStatusLine().getStatusCode() + "—>" + httpclient.getEntity(response));
        } else {
            info("上报请求失败：" + form);
            throw new Exception("上报请求失败,用时：" + (System.currentTimeMillis() - startTime) + "\n"
                    + ConfigerFactory.getConfiger().get("API_URL") + route
                    + "\n" + form
                    + "\n上报结果：" + response + "\t");
        }
    }

    protected double getNum(double amount, String unit) {
        if (unit == null || unit.length() == 0) {
            return amount;
        }
        if ("万".equals(unit)) {
            return amount * 10000;
        }
        if ("亿".equals(unit)) {
            return amount * 100000000;
        }
        return amount;
    }

    protected double getNumFromString(String content) {
        Pattern p = Pattern.compile("(\\d+\\.?\\d*)([亿万]?)");
        Matcher m = p.matcher(content);
        if (m.find()) {
            double amount = Double.parseDouble(m.group(1));
            String unit = m.group(2);
            return getNum(amount, unit);
        }
        return 0;
    }
}
