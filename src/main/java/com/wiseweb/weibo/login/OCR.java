package com.wiseweb.weibo.login;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ty on 2017/8/3.
 */


public class OCR {
    private final String LANG_OPTION = "-l";
    private final String LANG = "weibo2";
    private final String EOL = System.getProperty("line.separator");

    /**
     * @param imageFile 传入的图像文件
     * @param
     * @return 识别后的字符串
     */
    public String recognizeImg(File imageFile) throws Exception {
        File outputFile = new File(imageFile.getParentFile(), "output.txt");
        StringBuffer strB = new StringBuffer();
        List<String> cmd = new ArrayList<>();
        cmd.add("tesseract");
        cmd.add("");
        cmd.add("output");
        cmd.add(LANG_OPTION);
        cmd.add(LANG);
        ProcessBuilder pb = new ProcessBuilder();
        pb.directory(imageFile.getParentFile());
        cmd.set(1, imageFile.getName());
        pb.command(cmd);
        Process process = pb.start();
        int w = process.waitFor();
        if (w == 0)// 0代表正常退出
        {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(new FileInputStream(outputFile.getAbsolutePath()), "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
                strB.append(str).append(EOL);
            }
            in.close();
            outputFile.delete();
        } else {
            InputStream errorStream = process.getErrorStream();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(errorStream, "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
                System.err.println(str);
            }
        }
        return strB.toString().replaceAll("\\s*", "");
    }

    public Set<String> result(String userName, String Location) {
        List<String> list = new ArrayList<String>();
        for (int i = 1; i <= 10; i++) {
            String name = userName + "_" + i + ".jpg";
            File img = new File(Location + name);
            try {
                String result = recognizeImg(img);
                if (result.length() == 4) {
                    list.add(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        List<String> result_1 = new ArrayList<String>();
        List<String> result_2 = new ArrayList<String>();
        List<String> result_3 = new ArrayList<String>();
        List<String> result_4 = new ArrayList<String>();

        for (int i = 0; i < list.size(); i++) {
            result_1.add(list.get(i).charAt(0) + "");
            result_2.add(list.get(i).charAt(1) + "");
            result_3.add(list.get(i).charAt(2) + "");
            result_4.add(list.get(i).charAt(3) + "");
        }
        Set<String> result = new HashSet<String>();

        for (int i = 0; i < result_1.size(); i++) {
            for (int j = 1; j < result_1.size(); j++) {
                String res = "";
                if (result_1.size() == 1) {
                    res += result_1.get(i);
                } else {
                    if (result_1.get(i).equals(result_1.get(j))) {
                        res += result_1.get(i);
                    } else {
                        continue;
                    }
                }
                if (result_2.size() == 1) {
                    res += result_2.get(i);
                } else {
                    if (result_2.get(i).equals(result_2.get(j))) {
                        res += result_2.get(i);
                    } else {
                        continue;
                    }
                }

                if (result_3.size() == 1) {
                    res += result_3.get(i);
                } else {
                    if (result_3.get(i).equals(result_3.get(j))) {
                        res += result_3.get(i);
                    } else {
                        continue;
                    }
                }

                if (result_4.size() == 1) {
                    res += result_4.get(i);
                } else {
                    if (result_4.get(i).equals(result_4.get(j))) {
                        res += result_4.get(i);
                    } else {
                        continue;
                    }
                }
                result.add(res);

            }
        }
        System.out.println(result);
        return result;
       /* Set<String> set = new HashSet<String>();
        for (int j = 0; j < list.size(); j++) {
            for (int n = 1; n < list.size(); n++) {
                String res = "";
                for (int i = 0; i < 4; i++) {
                    String a = list.get(j).charAt(i) + "";
                    String b = list.get(n).charAt(i) + "";
                    if (a.equals(b)) {
                        res += a;
                    }
                }
                if (res.length() == 4) {
                    set.add(res);
                }
            }
        }
        System.out.println(r);*/
    }

    public static void main(String[] args) throws Exception {
        File file = new File("/Users/Sane/Downloads/103.jpg");
        String result = new OCR().recognizeImg(file);
        System.out.println(result);
//        new OCR().result("E:\\tupian\\去干扰线\\");
    }
}