package com.yuepei.system.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


@Slf4j
public class UsrDemo {

	public void httpGetExample(String secret,String application,String domain) throws Exception {
//		String secret = "9tgo2urw0u";// 密钥，到控制台->应用管理打开应用可以找到此值
//		String application = "mExxmN0D098";// appKey，到应用管理打开应用可以找到此值
		String version = "20190507004824";// api版本，到文档中心->使能平台API文档打开要调用的api可以找到此值
		String MasterKey = "25ce00cc28c1498c833276110ee483f0";// MasterKey，在产品中心打开对应的产品查看此值
		HttpResponse response = null;
		CloseableHttpClient httpClient = null;
		httpClient = HttpClientBuilder.create().build();
		long offset = getTimeOffset();// 获取时间偏移量,方法见前面
		// 下面示例以根据产品ID查询产品信息的API为例【具体信息请以使能平台的API文档为准】。
		// 构造请求的URL,具体参考文档中心->API文档中的请求地址和访问路径
		URIBuilder uriBuilder = new URIBuilder();
		uriBuilder.setScheme("https");// 请求用的协议，http或者https
		uriBuilder.setHost(domain+"/aep_product_management");//请求地址
		uriBuilder.setPath("/products");//访问路径，可以在API文档中对应的API中找到此访问路径
		// 在请求的URL中添加参数,具体参考文档中心->API文档中“请求参数”说明
		// (如果有MasterKey，将MasterKey加到head中，不加在此处)
//		uriBuilder.addParameter("productId", "9392");
		HttpGet httpGet = new HttpGet(uriBuilder.build());//构造get请求
		long timestamp = System.currentTimeMillis() + offset;// 获取时间戳
		Date date = new Date(timestamp);
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		String dataString = dateFormat.format(date);// 生成格式化的日期字符串
		// head中添加公共参数，具体参考文档中心->API文档中“公共参数”里的公共请求参数
		// httpGet.addHeader("MasterKey", MasterKey);// MasterKey加在此处head中
		httpGet.addHeader("application", application);
		httpGet.addHeader("timestamp", "" + timestamp);
		httpGet.addHeader("version", version);
		httpGet.addHeader("Content-Type", "application/json; charset=UTF-8");
		httpGet.addHeader("Date", dataString);
		// 下列注释的head暂时未用到
		// httpGet.addHeader("sdk", "GIT: a4fb7fca");
		// httpGet.addHeader("Accept", "gzip,deflate");
		// httpGet.addHeader("User-Agent", "Telecom API Gateway Java SDK");
		// 构造签名需要的参数,如果参数中有MasterKey,则添加来参与签名计算,其他参数根据实际API从URL中获取
		Map<String, String> param = new HashMap<String, String>();
		// param.put("MasterKey", MasterKey);
		// 从URL中获取参数加到param中
		List<NameValuePair> list = uriBuilder.getQueryParams();
		for (int i = 0; i < list.size(); i++)
			param.put(list.get(i).getName(), list.get(i).getValue());
		// 添加签名
		httpGet.addHeader("signature", sign(param, timestamp, application, secret, null));
		System.out.println(httpGet.getURI());
		try {
			// 发送请求
			response = httpClient.execute(httpGet);
			// 从response获取响应结果
			System.out.println(new String(EntityUtils.toByteArray(response.getEntity())));
			httpClient.close();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 添加产品
	 */
	public void addProduct(String secret,String application,String domain) {
		try {
//			String secret = "9tgo2urw0u";//密钥，到控制台->应用管理打开应用可以找到此值
//			String application = "mExxmN0D098";//appKey，到控制台->应用管理打开应用可以找到此值
			String version = "20220924042921";//api版本，到文档中心->使能平台API文档打开要调用的api可以找到版本值
			// 下面以增加设备的API为例【具体信息请以使能平台的API文档为准】。
			//请求BODY,到文档中心->使能平台API文档打开要调用的api中，在“请求BODY”中查看
			String bodyString = "{\"productName\":\"测试\",\"productDesc\":\"测试\",\"productType\":\"边缘计算\",\"secondaryType\":\"" +
					"边缘网关\",\"thirdType\":\"边缘设备\",\"createBy\":\"admin\",\"nodeType\":1,\"accessType\":1,\"networkType\":3,\"productProtocol" +
					"\":3,\"authType\":4,\"dataEncryption\":5,\"encryptionType\":0,\"tupIsThrough\":0,\"payloadFormat\":1,\"powerModel\":2,\"endpointFormat\":1," +
					"\"tupDeviceModel\":\"测试\",\"accessAreaName\":\"深圳市-龙岗区\"}";
			CloseableHttpClient httpClient = null;
			HttpResponse response = null;
			httpClient = HttpClientBuilder.create().build();
			long offset = getTimeOffset();// 获取时间偏移量，方法见前面
			// 构造请求的URL，具体参考文档中心->使能平台API文档中的请求地址和访问路径
			URIBuilder uriBuilder = new URIBuilder();
			uriBuilder.setScheme("https");
			uriBuilder.setHost("2000297260.api.ctwing.cn/aep_device_command"); //请求地址
			uriBuilder.setPath("/product"); //访问路径，可以在API文档中对应API中找到此访问路径
			// 在请求的URL中添加参数，具体参考文档中心->API文档中请求参数说明
			// (如果有MasterKey，将MasterKey加到head中，不加在此处)
			//uriBuilder.addParameter("productId", "9392");//如果没有其他参数，此行不要
			HttpPost httpPost = new HttpPost(uriBuilder.build());//构造post请求
			URI uri = httpPost.getURI();
			log.info("请求URL：{}", uri);
			long timestamp = System.currentTimeMillis() + offset;// 获取时间戳
			Date date = new Date(timestamp);
			SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
			dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
			String dataString = dateFormat.format(date);// 生成格式化的日期字符串
			// head中添加公共参数
//			httpPost.addHeader("MasterKey", MasterKey);// MasterKey加在此处head中
			httpPost.addHeader("application", application);
			httpPost.addHeader("timestamp", "" + timestamp);
			httpPost.addHeader("version", version);
			httpPost.addHeader("Content-Type", "application/json; charset=UTF-8");
			httpPost.addHeader("Date", dataString);
			// 下列注释的head暂时未用到
			// httpPost.addHeader("sdk", "GIT: a4fb7fca");
			// httpPost.addHeader("Accept", "gzip,deflate");
			// httpPost.addHeader("User-Agent", "Telecom API Gateway Java SDK");
			// 构造签名需要的参数,如果参数中有MasterKey，则添加来参与签名计算,
			// 其他参数根据实际API从URL中获取,如有其他参数,写法参考get示例
			Map<String, String> param = new HashMap<String, String>();
//			param.put("MasterKey", MasterKey);
			// 添加签名
			httpPost.addHeader("signature", sign(param, timestamp, application, secret, bodyString.getBytes()));
			//请求添加body部分
			httpPost.setEntity(new StringEntity(bodyString, "utf-8"));
			// 发送请求
			response = httpClient.execute(httpPost);
			// 从response获取响应结果
			System.out.println(new String(EntityUtils.toByteArray(response.getEntity())));
			httpClient.close();
		}catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * http POST请求示例统一下发指令
	 */
	public void httpPostExample(String hexBinary,String productId,String MasterKey,String deviceId,String secret,String application,String domain) throws Exception {
		log.info(hexBinary+"---"+productId+"---"+MasterKey+"-----"+deviceId);
		log.info(application+"---"+secret+"----"+domain);
//		String secret = "9tgo2urw0u";//密钥，到控制台->应用管理打开应用可以找到此值
//		String application = "mExxmN0D098";//appKey，到控制台->应用管理打开应用可以找到此值
		String version = "20190712225145";//api版本，到文档中心->使能平台API文档打开要调用的api可以找到版本值
//		String MasterKey = "73dcc3a337954de78d7a912394dbd52f";//MasterKey，在产品中心打开对应的产品查看此值
		// 下面以增加设备的API为例【具体信息请以使能平台的API文档为准】。
		//请求BODY,到文档中心->使能平台API文档打开要调用的api中，在“请求BODY”中查看
		String bodyString = "{\"content\": {\"jtMessageId\":\"8001\",\"jtMessageType\":\"00\"," +
				"\"dataType\":2,\"payload\":\""+hexBinary+"\"},\"deviceId\":\""+deviceId+"\"," +
				"\"operator\":\"string\",\"productId\":"+productId+",\"ttl\":0,\"deviceGroupId\": 100,\"level\": 1}";
		CloseableHttpClient httpClient = null;
		HttpResponse response = null;
		httpClient = HttpClientBuilder.create().build();
		long offset = getTimeOffset();// 获取时间偏移量，方法见前面
		// 构造请求的URL，具体参考文档中心->使能平台API文档中的请求地址和访问路径
		URIBuilder uriBuilder = new URIBuilder();
		uriBuilder.setScheme("https");
		uriBuilder.setHost(domain+"/aep_device_command"); //请求地址
		uriBuilder.setPath("/command"); //访问路径，可以在API文档中对应API中找到此访问路径
		// 在请求的URL中添加参数，具体参考文档中心->API文档中请求参数说明
		// (如果有MasterKey，将MasterKey加到head中，不加在此处)
		//uriBuilder.addParameter("productId", "9392");//如果没有其他参数，此行不要
		HttpPost httpPost = new HttpPost(uriBuilder.build());//构造post请求
		URI uri = httpPost.getURI();
		log.info("请求URL：{}",uri);
		long timestamp = System.currentTimeMillis() + offset;// 获取时间戳
		Date date = new Date(timestamp);
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		String dataString = dateFormat.format(date);// 生成格式化的日期字符串
		// head中添加公共参数
		httpPost.addHeader("MasterKey", MasterKey);// MasterKey加在此处head中
		httpPost.addHeader("application", application);
		httpPost.addHeader("timestamp", "" + timestamp);
		httpPost.addHeader("version", version);
		httpPost.addHeader("Content-Type", "application/json; charset=UTF-8");
		httpPost.addHeader("Date", dataString);
		// 下列注释的head暂时未用到
		// httpPost.addHeader("sdk", "GIT: a4fb7fca");
		// httpPost.addHeader("Accept", "gzip,deflate");
		// httpPost.addHeader("User-Agent", "Telecom API Gateway Java SDK");
		// 构造签名需要的参数,如果参数中有MasterKey，则添加来参与签名计算,
		// 其他参数根据实际API从URL中获取,如有其他参数,写法参考get示例
		Map<String, String> param = new HashMap<String, String>();
		param.put("MasterKey", MasterKey);
		// 添加签名
		httpPost.addHeader("signature", sign(param, timestamp, application, secret, bodyString.getBytes()));
		//请求添加body部分
		httpPost.setEntity(new StringEntity(bodyString,"utf-8"));
		try {
			// 发送请求
			response = httpClient.execute(httpPost);
			// 从response获取响应结果
			System.out.println(new String(EntityUtils.toByteArray(response.getEntity())));
			httpClient.close();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param param    api 配置参数表
	 * @param timestamp UNIX格式时间戳
	 * @param application appKey,到应用管理打开应用可以找到此值
	 * @param secret 密钥,到应用管理打开应用可以找到此值
	 * @param body 请求body数据,如果是GET请求，此值写null
	 * @return 签名数据
	 */
	public static String sign(Map<String, String> param, long timestamp, String application, String secret, byte[] body) throws Exception {
		// 连接系统参数
		StringBuffer sb = new StringBuffer();
		sb.append("application").append(":").append(application).append("\n");
		sb.append("timestamp").append(":").append(timestamp).append("\n");
		// 连接请求参数
		if (param != null) {
			TreeSet<String> keys = new TreeSet<String>(param.keySet());
			Iterator<String> i = keys.iterator();
			while (i.hasNext()) {
				String s = i.next();
				String val = param.get(s);
				sb.append(s).append(":").append(val == null ? "" : val).append("\n");
			}
		}
		//body数据写入需要签名的字符流中
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(sb.toString().getBytes("utf-8"));
		if (body != null && body.length > 0) {
			baos.write(body);
			baos.write("\n".getBytes("utf-8"));
		}
		// 得到需要签名的字符串
		String string = baos.toString("utf-8");
		System.out.println("Sign string: " + string);
		// hmac-sha1编码
		byte[] bytes = null;
		SecretKey secretKey = new SecretKeySpec(secret.getBytes("utf-8"), "HmacSha1");
		Mac mac = Mac.getInstance(secretKey.getAlgorithm());
		mac.init(secretKey);
		bytes = mac.doFinal(string.getBytes("utf-8"));
		// base64编码
		String encryptedString = new String(Base64.encodeBase64(bytes));
		// 得到需要提交的signature签名数据
		return encryptedString;
	}


	public static long getTimeOffset() {
		long offset = 0;
		HttpResponse response = null;
		//构造httpGet请求
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet httpTimeGet = new HttpGet("https://ag-api.ctwing.cn/echo");
		try {
			long start = System.currentTimeMillis();
			response = httpClient.execute(httpTimeGet);
			long end = System.currentTimeMillis();
			//时间戳在返回的响应的head的x-ag-timestamp中
			Header[] headers = response.getHeaders("x-ag-timestamp");
			if (headers.length > 0) {
				long serviceTime = Long.parseLong(headers[0].getValue());
				offset = serviceTime - (start + end) / 2L;
			}
			httpClient.close();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return offset;
	}


}
