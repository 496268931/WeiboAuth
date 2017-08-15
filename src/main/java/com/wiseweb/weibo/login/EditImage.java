package com.wiseweb.weibo.login;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by ty on 2017/7/24.
 * 编辑图片
 */
public class EditImage {
    /**
     * 去颜色相同的干扰线
     *
     * @param img
     * @param px
     * @return
     */
    private static BufferedImage removeLine(BufferedImage img, int px) {
        if (img != null) {
            int width = img.getWidth();
            int height = img.getHeight();

            for (int x = 0; x < width; x++) {
                List<Integer> list = new ArrayList<Integer>();
                for (int y = 0; y < height; y++) {
                    int count = 0;
                    while (y < height - 1 && isWhite(img.getRGB(x, y))) {
                        count++;
                        y++;
                    }
                    if (count <= px && count > 0) {
                        for (int i = 0; i <= count; i++) {
                            list.add(y - i);
                        }
                    }
                }
                if (list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        img.setRGB(x, list.get(i), Color.white.getRGB());
                    }
                }
            }
        }
        return img;

    }

    public static boolean isBlack(int colorInt) {
        Color color = new Color(colorInt);
        if (color.getRed() + color.getGreen() + color.getBlue() <= 10) {
            return true;
        }
        return false;
    }

    public static boolean isWhite(int colorInt) {
        Color color = new Color(colorInt);
        if (color.getRed() + color.getGreen() + color.getBlue() > 480) {   //757.5
            return true;
        }
        return false;
    }


    /**
     * 处理微博的图片，取出干扰线、转为jpg
     *
     * @param location     源图片路径
     * @param editLocation 处理后保存路径
     */
    public static void handleWeiboImage(String userName, String location, String editLocation) {
        for (int i = 1; i <= 10; i++) {
            String name = userName + "_" + i + ".gif";
            String newName = userName + "_" + i + ".jpg";
            BufferedImage img = null;
            try {
                img = ImageIO.read(new File(location + name));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("读取图片失败");
            }
            BufferedImage imger = EditImage.removeLine(img, 20);
            if (imger == null) {
                System.out.println(name + "不可显示");
                continue;
            }
            File file = new File(editLocation + newName);
            try {
                ImageIO.write(imger, "jpg", file);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("编辑保存图片失败");
            }
        }
    }

    /**
     * 保存下载的图片
     *
     * @param url             url图片地址
     * @param locatioon       保存图片地址
     */
    public static void saveWeiboImage(RequestConfig config, String userName, String url, String locatioon) {
        for (int j = 1; j <= 10; j++) {
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            httpGet.setConfig(config);
            try {
                httpClient.execute(httpGet);
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    byte[] data = EntityUtils.toByteArray(response.getEntity());
                    String name = userName + "_" + j + ".gif";
                    FileOutputStream fos = new FileOutputStream(locatioon + name);
                    fos.write(data);
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("保存图片失败");
            }
        }
    }
}