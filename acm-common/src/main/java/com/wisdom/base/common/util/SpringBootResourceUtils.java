package com.wisdom.base.common.util;

import com.wisdom.base.common.util.calc.calendar.Tools;
import com.wisdom.leaf.common.PropertyFactory;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.util.Map;

public class SpringBootResourceUtils {
    /**
     * @param config
     * @return
     * @throws IOException
     */
    public static File getResource(String config) throws IOException {
        Resource resource = new ClassPathResource(config);
        //File file = resource.getFile();
        InputStream inputStream = resource.getInputStream();
        String filename = resource.getFilename();
        File tempFile = File.createTempFile("temp", filename);
        OutputStream out = new FileOutputStream(tempFile);
        IOUtils.copy(inputStream, out);
        out.flush();
        out.close();
        tempFile.deleteOnExit();
        return tempFile;
    }

    /**
     * 得到工程的app配置文件
     * @return
     */
    public static Map<String, Object> getApplicationProperties(){
        InputStream in = PropertyFactory.class.getClassLoader().getResourceAsStream("application.yml");//文件路径是相对类目录(src/main/java)的相对路径
        Map<String, Object> yml = ResourceUtil.getYmlPropertie(in);
        String active = Tools.toString(yml.get("spring.profiles.active"));
        if(!ObjectUtils.isEmpty(active)){
            String[] actives = active.split(",");
            for(String name : actives){
                in = PropertyFactory.class.getClassLoader().getResourceAsStream("application-" + name + ".yml");//文件路径是相对类目录(src/main/java)的相对路径
                Map<String, Object> _yml = ResourceUtil.getYmlPropertie(in);
                yml.putAll(_yml);
            }
        }
        return yml;
    }
}
