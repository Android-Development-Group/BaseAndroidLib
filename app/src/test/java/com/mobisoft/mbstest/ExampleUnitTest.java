package com.mobisoft.mbstest;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private String TAG=ExampleUnitTest.class.getName();

//    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

    }
    @Test
    public void pastest() throws Exception {
//        String url = "kitapps://getudid?para=%22%22&callback=checkAccountLogin&sn=1544493601335";
               String url = "kitapps://onlive?para=%7B%22videoUrl%22%3A%22http%3A%2F%2Fpullhls.mobisoft.com.cn%2Flive%2F9d2e80daafcc447b8af7644822c90dbd%2Fplaylist.m3u8%3FwsTime%3D1544434694%26wsSecret%3Df6c67acbacc5c2cf4b000c5b173cea93%22%2C%22liveFlag%22%3A%22true%22%2C%22placeholderurl%22%3A%22%22%2C%22videoPercentage%22%3A%22%22%2C%22course_no%22%3A%22%22%2C%22courseItem_no%22%3A%22%22%7D&callback=ResultForCallback(0,#result#)&sn=1544434214658";

        Map<String, String> map =  parseUrl(url);
        for (Map.Entry<String, String> entry : map.entrySet()) {

            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());

        }

    }

    public Map<String, String> parseUrl(String url) {

        String param = url.substring( url.indexOf("?") + 1);
        param = param.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
        Map<String, String> result = new HashMap<String, String>();

        try {
            param = URLDecoder.decode(param, "UTF-8");
            if( param.indexOf("para={")>=0){
                int first = param.indexOf("={");
                int indexPara = param.indexOf("para=");
                int last = param.indexOf("}&")+1;
                String para= param.substring(indexPara,last);
                System.out.println("para--->"+para);
                System.out.println("param1--->"+param);
                String[] keyval = new String[]{para.substring(0, first), para.substring(first + 1, para.length())};
                if (keyval.length == 2) {
                    result.put(keyval[0], keyval[1]);
                }
                param= param.substring(last+1,param.length());
                System.out.println("param2--->"+param);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String[] list = param.split("&");
        for (String str : list) {
            int first = str.indexOf("=");
            if (first != -1) {
                String[] keyval = new String[]{str.substring(0, first), str.substring(first + 1, str.length())};
                if (keyval.length == 2) {
                    result.put(keyval[0], keyval[1]);
                }
            }
        }


        return result;
    }
}