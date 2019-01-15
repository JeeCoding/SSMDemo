package com.huzhframework.ssmdemo.utils.httpclientcraw;


import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;

/**
 * HttpClient & Jsoup libruary test class
 * HttpClient & Jsoup抓取网站信息
 * <p>
 * Created by xuyh at 2017/11/6 15:28.
 */
public class HttpClientJsoupCawl {

    public void cawl(String Url) {
        //通过httpClient获取网页响应,将返回的响应解析为纯文本
        HttpGet httpGet = new HttpGet(Url);
        httpGet.setConfig(RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build());
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;

        String responseStr = "";
        try {
            httpClient = HttpClientBuilder.create().build();
            HttpClientContext context = HttpClientContext.create();
            response = httpClient.execute(httpGet, context);
            int state = response.getStatusLine().getStatusCode();
            if (state != 200)
                responseStr = "";
            HttpEntity entity = response.getEntity();
            if (entity != null)
                responseStr = EntityUtils.toString(entity, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null)
                    response.close();
                if (httpClient != null)
                    httpClient.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (responseStr == null)
            return;

        //将解析到的纯文本用Jsoup工具转换成Document文档并进行操作
        Document document = Jsoup.parse(responseStr);
        List<Element> elements = document.getElementsByAttributeValue("class", "tianyi__wrap-a").first().getElementsByAttributeValue("class", "ty-card ty-card-type10 clearfix");
        for (Element element : elements) {
            for (Element e : element.getElementsByTag("a")) {
                System.out.println(e.attr("href"));
                System.out.println(e.text());
            }
        }
    }
}