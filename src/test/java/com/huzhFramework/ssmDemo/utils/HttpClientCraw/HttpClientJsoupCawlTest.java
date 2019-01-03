package com.huzhFramework.ssmDemo.utils.HttpClientCraw;

import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

import static org.junit.Assert.*;

public class HttpClientJsoupCawlTest {

    @Test
    public void cawl() {
        HttpClientJsoupCawl httpClientJsoupCawl = new HttpClientJsoupCawl();
//        HttpGet httpGet = new HttpGet("http://sports.sina.com.cn/");
//        HttpGet httpGet = new HttpGet("https://sou.zhaopin.com");
//        HttpGet httpGet = new HttpGet("https://search.51job.com");
//        HttpGet httpGet = new HttpGet("http://sou.zhaopin.com/jobs/searchresult.ashx");
        httpClientJsoupCawl.cawl("http://sou.zhaopin.com/jobs/searchresult.ashx");
    }
}