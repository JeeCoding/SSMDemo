package com.huzhFramework.ssmDemo.utils.HttpClientCraw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupHtml {

    // 智联招聘网站URL
    private String url_head = "https://sou.zhaopin.com/jobs/searchresult.ashx?jl=";
    private String url_gw = "&kw=";
    private String url_page = "&p=1&isadv=";
    //要爬取的城市
    private static String[] cityname = {"北京","上海","广州","深圳","杭州"};
    //要爬取的岗位名称（或关键字）
    private static String[] jobname = {"android","ios","前端工程师","Java工程师","大数据开发工程师",
            "数据库开发工程师"};

    public void getZhiLianWork(String cityName,String gwmc) {
        String nullString = "\t";
        try {
            for (int i = 1; i <= 25; i++) {
                System.out.println("*********开始遍历第" + (i) + "页的求职信息*********");
                Document doc = Jsoup.connect(url_head+ cityName + url_gw + gwmc + url_page + i).get();
                Element content = doc.getElementById("newlist_list_content_table");
                // 职位名称
                Elements zwmcEls = content.getElementsByClass("zwmc");
                // 公司名称
                Elements gsmcEls = content.getElementsByClass("gsmc");
                // 职位月薪
                Elements zwyxEls = content.getElementsByClass("zwyx");
                // 工作地点
                Elements gzddEls = content.getElementsByClass("gzdd");

                Elements xinxi = doc.getElementsByClass("newlist_deatil_two");
                for (int j = 1; j <= zwmcEls.size(); j++) {
                    String info = null;
                    if (j < zwmcEls.size()) {

                        String n1 = zwmcEls.get(j).tagName("a").text();
                        String n2 = gsmcEls.get(j).tagName("a").text();
                        String n3 = zwyxEls.get(j).tagName("a").text();
                        String n4 = gzddEls.get(j).tagName("a").text();

                        //为空的字段直接用\t代替
                        if (n1 == null ) {
                            n1 = nullString;
                        } else if (n2 == null){
                            n2 = nullString;
                        } else if (n3 == null){
                            n3 = nullString;
                        } else if (n4 == null) {
                            n4 = nullString;
                        }
                        info = n1 + "\t" + n2 + "\t" + n3 + "\t" + n4;
                    }
                    if (xinxi.size() > j - 1) {
                        Elements x = xinxi.get(j - 1).getAllElements();
                        for (int k = 1; k < x.size(); k++) {
                            String n = x.get(k).text();
                            if (n == null || x == null || x.equals("")){
                                n = nullString;
                            }
                            info += "\t" + n;
                        }
                    }

                    if (info != null){
                        info+="\n";
                        Date date = new Date();
                        DateFormat format = new SimpleDateFormat("yyyyMMdd");
                        String today = format.format(date);
                        write("/home/richard/Data/" + gwmc + today + ".txt", info);
                    }


                }
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //Java IO流写入文件
    public void write(String path, String content) {
        // 1、建立联系 File对象 目的地
        File dest = new File(path);
        // 2、选择流 文件输出流 OutputStream FileOutputStream
        OutputStream os = null;
        try {
            // 以追加形式 写出文件 必须为true 否则为覆盖
            os = new FileOutputStream(dest, true);
            // 3、操作
            String str = content;
            // 字符串转字节数组
            byte[] data = str.getBytes();
            os.write(data, 0, data.length);

            os.flush(); // 强制刷新出去
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("文件未找到");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("文件写出失败");
        } finally {
            // 4、释放资源 :关闭
            try {
                if (null != os) {
                    os.close();
                }
            } catch (Exception e2) {
                System.out.println("关闭输出流失败");
            }
        }
    }

    public static void main(String[] args) {

        JsoupHtml jHtml = new JsoupHtml();
        //遍历岗位名称
        for (String job:jobname) {
            //遍历城市
            for (String s: cityname) {
                System.out.println("Start : job:" + job + "\tcity:" + s);
                //爬取数据并写到本地
                jHtml.getZhiLianWork(s,job);
            }
        }


    }

}
