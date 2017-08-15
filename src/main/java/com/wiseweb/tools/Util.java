package com.wiseweb.tools;

import com.wiseweb.json.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 2014-6-11
 *
 * @author 贾承斌
 */
public class Util {
//	public static Logger log = Logger.getLogger(Util.class);
	//一天的毫秒值
	public static long time = 1000*60*60*24L;

	/**
	 * 将yyyy-MM-dd HH:mm:ss 时间字符串转换成 cron表达式
	 *
	 * @param dateString
	 * @return
	 */
	public static String stringDateToCronExpressions(String dateString) {
		String cron = null;
		try {
			SimpleDateFormat stringformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = stringformat.parse(dateString);
			SimpleDateFormat format = new SimpleDateFormat("s m H d M *");
			cron = format.format(date);
		} catch (ParseException e) {
			cron = null;
		}
		return cron;

	}


	public static String getDateFormat(String fromFormat,String toFormat,String dataStr) {
		String dateString = null;
		try {
		SimpleDateFormat fFormat = new SimpleDateFormat(fromFormat);
		SimpleDateFormat tFormat = new SimpleDateFormat(toFormat);
		Date date = fFormat.parse(dataStr);
		dateString = tFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateString;

	}

	public static Date getDateFormat(String format,String dataStr) {

		try {
			SimpleDateFormat mformat = new SimpleDateFormat(format);
			Date date = mformat.parse(dataStr);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;

	}
	/**
	 * 获取yyyy-MM-dd HH:mm:ss 时间字符串
	 * @return 
	 */
	public static String getDateFormat() {
		String dateString = null;
		SimpleDateFormat stringformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		dateString = stringformat.format(date);
		return dateString;

	}
	public static String getDateFormat(int seconds) {
		String dateString = null;

		SimpleDateFormat stringformat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(seconds*1000);
		dateString = stringformat.format(date);
		return dateString;

	}

	public static String getFromatDateString() {
		String dateString = null;
		SimpleDateFormat stringformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		dateString = stringformat.format(date);
		return dateString;

	}


	public static String hexMD5(byte[] data) {

		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");

			md5.reset();
			md5.update(data);
			byte digest[] = md5.digest();

			return toHex( digest );
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.out.println("Error - this implementation of Java doesn't support MD5.");
			return null;
			//throw new RuntimeException("Error - this implementation of Java doesn't support MD5.");
		}
	}

	public static String toHex( byte b[] ){
		StringBuilder sb = new StringBuilder();

		for ( int i=0; i<b.length; i++ ){
			String s = Integer.toHexString(0xff & b[i]);

			if (s.length() < 2) {
				sb.append("0");
			}
			sb.append(s);
		}
		return sb.toString();
	}

	/**
	 * 获取请求返回结果
	 * @param res
	 * @return
	 */
	public static String getContent(HttpResponse res) {
		if(res!=null){
			try {
				return EntityUtils.toString(res.getEntity());
			} catch (Exception e) {
				e.printStackTrace();
//				error("获取请求返回结果信息异常",e);
			}
		}
		return null;
	}

	/**
	 * 获取请求返回结果  注：需要添加编码
	 * @param res
	 * @return
	 */
	public static String getContent(HttpResponse res,String defaultCharset) {
		if(res!=null){
			try {
				return EntityUtils.toString(res.getEntity(),defaultCharset);
			} catch (Exception e) {
				e.printStackTrace();
//				error("获取请求返回结果信息异常",e);
			}
		}
		return null;
	}

	/**
	 * 获取请求返回Code
	 * @param res
	 * @return
	 */
	public static int getCode(HttpResponse res){
		if(res!=null){
			try {
				return res.getStatusLine().getStatusCode();
			} catch (Exception e) {
				e.printStackTrace();
//				error("获取请求返回结果Code信息异常",e);
			}
		}
		return -1;
	}

	/**
	 * 获取请求返回的回调地址
	 * * @return
	 */
	public static String getLocation(HttpResponse res){
		if(res!=null){
			try {
				Header h = res.getLastHeader("Location");
				if(h!=null){
					return h.getValue().trim();	
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**解析JSONObject参数封装到Map里面
	 * * @return
	 */
	public static Map<String, String> getMap(JSONObject j) {
		Map<String,String> map = new HashMap<String, String>();
		try {
			Iterator i = j.keys();
			while(i.hasNext()){
				try {
					String key = ((String)i.next()).trim();
					String value = j.get(key).toString();
					map.put(key.trim(),value.trim());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
//			log.error("解析JSON数据封装到Map时，出现异常\n"+j.toString(),e);
		}
		return null;
	}



	/**
	 * 把请求参数封装到map里面
	 */
	public static Map<String, String> getForm(JSONObject f){
		if(f!=null && f.length()>0) {
			Map<String, String> map = new HashMap<String, String>();
			try {
				Iterator i = f.keys();
				while (i.hasNext()) {
					try {
						String key = ((String) i.next()).trim();
						if (!key.equals("Referer")) {
							String value = f.getString(key);
							map.put(key.trim(), value.trim());
						}
					} catch (Exception e) {
					}
				}
				return map;
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("封装请求参数异常报错");
			}
		}
		return null;
	}

	/**
	 * 把请求消息头封装到map里面
	 */
	public static Map<String, String> getHeader(JSONObject f){
		Map<String,String> hmap = new HashMap<String, String>();
		try {
			if(f.has("Referer")){
				hmap.put("Referer",f.getString("Referer"));
			}
			return hmap;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("封装消息头信息异常报错");
		}
		return hmap;
	}

	public static Map<String,String> getCookie(String cookies) {
		Map<String,String> map = new HashMap<String,String>();
		try{
			cookies = cookies.substring(1,cookies.length()-1);
			String[]sp1 = cookies.split(",");
			for(int i=0;i<sp1.length;i++){
				String[] sp2 = sp1[i].split("=");
				map.put(sp2[0].trim(),sp2.length==1?"":sp2[1]);
			}
			return map;
		}catch (Exception e){
			e.printStackTrace();
		}
		return map;
	}

	public static final String fetchStr(String pattern, String str) {
		String returnValue = null;
		try {
			Pattern p = Pattern.compile(pattern);
			Matcher m = p.matcher(str);
			while (m.find()) {
				returnValue = m.group(1);
			}
			return returnValue;
		} catch (PatternSyntaxException e) {
			return returnValue;
		}
	}
	public static Map<String, String> URLRequest(String URL)
	{
		Map<String, String> mapRequest = new HashMap<String, String>();

		String[] arrSplit=null;

		String strUrlParam=TruncateUrlPage(URL);
		if(strUrlParam==null)
		{
			return mapRequest;
		}
		//每个键值为一组 www.2cto.com
		arrSplit=strUrlParam.split("[&]");
		for(String strSplit:arrSplit)
		{
			String[] arrSplitEqual=null;
			arrSplitEqual= strSplit.split("[=]");

			//解析出键值
			if(arrSplitEqual.length>1)
			{
				//正确解析
				mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);

			}
			else
			{
				if(arrSplitEqual[0]!="")
				{
					//只有参数没有值，不加入
					mapRequest.put(arrSplitEqual[0], "");
				}
			}
		}
		return mapRequest;
	}


	private static String TruncateUrlPage(String strURL)
	{
		String strAllParam=null;
		String[] arrSplit=null;

		strURL=strURL.trim().toLowerCase();

		arrSplit=strURL.split("[?]");
		if(strURL.length()>1)
		{
			if(arrSplit.length>1)
			{
				if(arrSplit[1]!=null)
				{
					strAllParam=arrSplit[1];
				}
			}
		}

		return strAllParam;
	}

}
