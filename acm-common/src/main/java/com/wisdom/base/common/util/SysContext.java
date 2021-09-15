package com.wisdom.base.common.util;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 平台上下文环境变量
 * @author
 */
public class SysContext {

    private static String rootPath = "";

    private static String fontsFolder = "";

    private SysContext() {

    }

    /**
     * 日志记录对象
     */
    private static final Logger log = LoggerFactory.getLogger(SysContext.class);

    /**
     * the application Context
     */
    private static ApplicationContext springApplicationContextContext;

    /**
     * 存储spring 的applicationContext实例化对象
     * @param applicationContext spring的ApplicationContext
     */
    public static void setApplicationContext(ApplicationContext applicationContext) {
        if (springApplicationContextContext == null) {
            springApplicationContextContext = applicationContext;
        }
    }

    /**
     * 获取spring实例化的ApplicationContext对象
     * @return ApplicationContext 对象
     */
    public static ApplicationContext getApplicationContext() {
        return springApplicationContextContext;
    }

    /**
     * 获取spring中装载的bean对象
     * @param bean beanID
     * @return spring所管理的bean对象
     */
    public static Object getBean(String bean) {
        return springApplicationContextContext.getBean(bean);
    }

    /**
     * 获取spring中装载的bean对象
     * @param bean beanID
     * @return spring所管理的bean对象
     */
    public static <T> T getBean(Class<T> bean) {

        return springApplicationContextContext.getBean(bean);
    }

    /**
     * 获取spring中装载的bean对象
     * @param <T>
     * @param bean beanID
     * @return spring所管理的bean对象
     */
    public static <T> Map<String, T> getBeansOfType(Class<T> bean) {
        return springApplicationContextContext.getBeansOfType(bean);
    }

    public static String getRootPath(){
        return SysContext.rootPath;
    }

    public static String getFontsFolder (){ return SysContext.fontsFolder;}

    /**
     * 根据文件夹和文件夹内的参数获取资源集合（例如/fonts/*.txt,查询fonts文件夹下的所有txt文件）
     *
     * @param foler
     * @return
     */
    public static Resource[] findResouresByFoler(String foler) throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(foler);
        return resources;
    }

    public static InputStream findResoureFile(String resoureName){
        try{
            Resource rs = new ClassPathResource(resoureName);
            InputStream is = rs.getInputStream();
            return is;
        }catch (IOException e){
            e.printStackTrace();
            log.error("找不到资源："+resoureName);
        }
        return null;
    }




    public static String findResoureFilePath(String filePath){

        //获取容器资源解析器
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        String retFilePath = "";
        try {
            //获取所有匹配的文件
            Resource[] resources = resolver.getResources(filePath);
            for(Resource resource : resources) {
                //获得文件流，因为在jar文件中，不能直接通过文件资源路径拿到文件，但是可以在jar包中拿到文件流
                InputStream stream = resource.getInputStream();
                log.info("读取的文件流  [" + stream + "]");
                String rootPath = SysContext.getRootPath().replace("file:\\","").replace("file:","");
                retFilePath = rootPath +"\\"+ filePath;
                System.out.println("放置位置  [" + retFilePath + "]");
                log.info("放置位置  [" + retFilePath + "]");
                File ttfFile = new File(retFilePath);

                if (!ttfFile.getParentFile().exists()&&!ttfFile.isDirectory()){
                    ttfFile.getParentFile().mkdirs();
                }
                byte[] bates = FileCopyUtils.copyToByteArray(stream);
                FileCopyUtils.copy(bates,ttfFile);
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.warn("读取文件流失败，写入本地库失败！ " + e);
        }

        return retFilePath;
    }
}
