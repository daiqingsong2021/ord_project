package com.wisdom.acm.processing.common.officeUtils;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 平台上下文环境变量
 * @author
 */
public class LoadingContext {

    private static final String fontsFolder =initContext();

    private LoadingContext() {
    }

    public static String initContext() {
        String dir = System.getProperty("user.dir");
        String fontsFolderStr= dir + File.separator+"fonts"+ File.separator;
        File fontsFolder=new File(fontsFolderStr);
        if(!fontsFolder.exists())
            fontsFolder.mkdirs();
        try {
            copyFilesByResourceFolder(fontsFolderStr,"/fonts/*");
        } catch (IOException e) {
            System.out.println("找不到字体资源!");
        }
        return fontsFolderStr;
    }

    public static String getFontsFolder (){
        return LoadingContext.fontsFolder;
    }

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

    /**
     * 将资源文件夹下的资源复制为文件
     *
     * @param toFolder
     * @param resouresFolder
     * @throws IOException
     */
    public static void copyFilesByResourceFolder(String toFolder,String resouresFolder) throws IOException {
        Resource[] findResouresByFoler = findResouresByFoler(resouresFolder);
        copyFilesByResources(toFolder,findResouresByFoler);
    }

    /**
     * 将资源复制为文件
     *
     * @param toFolder
     * @param fromResoures
     * @throws IOException
     */
    public static void copyFilesByResources(String toFolder, Resource... fromResoures ) throws IOException {

        if(fromResoures != null && fromResoures.length > 0){
            for(Resource resource : fromResoures) {
                //获得文件流，因为在jar文件中，不能直接通过文件资源路径拿到文件，但是可以在jar包中拿到文件流
                InputStream stream = resource.getInputStream();
                String targetFilePath = toFolder + resource.getFilename();
                File ttfFile = new File(targetFilePath);
                FileUtils.copyInputStreamToFile(stream, ttfFile);
                // new FileUtils().copyInputStreamToFile(stream, ttfFile);
            }
        }
    }

    public static InputStream findResoureFile(String resoureName){
        try{
            Resource rs = new ClassPathResource(resoureName);
            InputStream is = rs.getInputStream();
            return is;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
