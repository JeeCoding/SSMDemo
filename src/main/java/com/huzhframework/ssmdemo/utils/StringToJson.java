package com.huzhframework.ssmdemo.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName StringToJson
 * @Description TODO
 * @Date 2019/8/30 16:16
 * @Author huzh
 * @Version 1.0
 */
public class StringToJson {

    public static void main(String[] args) {
        String str = "{\"data\":\"MIIC8jCCApagAwIBAgIQNq/B8Goc4EUjbQm/UVne4jAMBggqgRzPVQGDdQUAMIGHMQswCQYDVQQGEwJDTjEOMAwGA1UECAwFSHViZWkxDjAMBgNVBAcMBVd1aGFuMTswOQYDVQQKDDJIdWJlaSBEaWdpdGFsIENlcnRpZmljYXRlIEF1dGhvcml0eSBDZW50ZXIgQ08gTHRkLjEMMAoGA1UECwwDRUNDMQ0wCwYDVQQDDARIQkNBMB4XDTE5MDgyMTA5MjEyN1oXDTIwMDgxNTA5MjEyN1owTzELMAkGA1UEBhMCQ04xJzAlBgNVBAoMHjkxNDIwMTExMzAzNjI5NTQzQemHkeagvOa1i+ivlTEXMBUGA1UEAwwONDIwMTA2MDAwMDAwODEwWTATBgcqhkjOPQIBBggqgRzPVQGCLQNCAAS50Wa9tE5r4cy2OXzWR+t1Ozp9tlpH4UwUkvaTZdc1pq2EUkSSNkntSO80bKEu3G+JTBdKHLlnxYB2TaorK4lFo4IBFzCCARMwHwYDVR0jBBgwFoAURg5nVnAqqf/h2YZH7044SNtxsnIwDAYDVR0TBAUwAwEBADCBtQYDVR0fBIGtMIGqMDOgMaAvpC0wKzELMAkGA1UEBhMCQ04xDDAKBgNVBAsMA0NSTDEOMAwGA1UEAwwFY3JsMTMwc6BxoG+GbWxkYXA6Ly8xNzIuMTYuMzkuMTk3OjM4OS9DTj1jcmwxMyxPVT1DUkwsQz1DTj9jZXJ0aWZpY2F0ZVJldm9jYXRpb25MaXN0P2Jhc2U/b2JqZWN0Y2xhc3M9Y1JMRGlzdHJpYnV0aW9uUG9pbnQwCwYDVR0PBAQDAgbAMB0GA1UdDgQWBBRtq46zakTx1w6VfMRlDZtWJlXoNDAMBggqgRzPVQGDdQUAA0gAMEUCIBxijT+ZmRk31i3X7AIJpezPxJkvRBjlt1MxQ2Hurhc+AiEAnopmlI1oAYKN5s7agRZ9IV0wH0iUMQfQIHm853XExlY=\",\"errorCode\":\"0\",\"errorMsg\":\"成功\"}";
        JSONObject json;
        json = JSONObject.parseObject(str);
        System.out.println("data: " + json.get("data"));
    }
}
