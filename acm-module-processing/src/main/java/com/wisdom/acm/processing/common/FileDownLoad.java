package com.wisdom.acm.processing.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author zll
 * 2020/8/26/026 14:25
 * Description:<描述>
 */
public class FileDownLoad {
    /**
     * 文件下载
     * @param fileUrl 下载路径
     * @param savePath 存放地址 示例：D:/ceshi/1.png
     * @throws Exception
     */
    public static void downloadFile(String fileUrl,String savePath) throws Exception {
        File file=new File(savePath);
        //判断文件是否存在，不存在则创建文件
        if(!file.exists()){
            file.createNewFile();
        }
        URL url = new URL(fileUrl);
        HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
        urlCon.setConnectTimeout(6000);
        urlCon.setReadTimeout(6000);
        int code = urlCon.getResponseCode();
        if (code != HttpURLConnection.HTTP_OK) {
            throw new Exception("文件读取失败");
        }
        DataInputStream in = new DataInputStream(urlCon.getInputStream());
        DataOutputStream out = new DataOutputStream(new FileOutputStream(savePath));
        byte[] buffer = new byte[2048];
        int count = 0;
        while ((count = in.read(buffer)) > 0) {
            out.write(buffer, 0, count);
        }
        try {
            if(out!=null) {
                out.close();
            }
            if(in!=null) {
                in.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//
//    public static void main(String[] args) {
//        String fileUrl="http://192.168.2.68:8400/group1/M00/00/09/wKgCNV9F50mAJwV7AACBrJGWluM66.docx";
//        String fileLocal=System.getProperty("user.dir") + "/temporary"+"/yyrb_" + System.currentTimeMillis() + ".docx";
//        try {
//            downloadFile(fileUrl,fileLocal);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }

    public static void main(String[] args) {
//        List<String> list = Arrays.asList("AA", "BB", "CC", "BB", "CC", "AA", "AA");
//        long l = list.stream().distinct().count();
//        System.out.println("No. of distinct elements:"+l);
//        String output = list.stream().distinct().collect(Collectors.joining(","));
//        System.out.println(output);

//        List<String> list = new ArrayList<>();
//        list.add("Mxy");
//        list.add("StringUtils");
//        list.add("join");
//        String join = StringUtils.join(list,"-");//传入String类型的List集合，使用"-"号拼接
//        System.out.println(join);

    }

}
