package com.wisdom.acm.szxm.common;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifDirectoryBase;
import net.coobird.thumbnailator.Thumbnails;

public class ImageUtil
{

    /**
     * 给图片添加水印
     *
     * @param originImgPath 原始图片的路径
     * @param targetImgPath 添加水印后图片的保存路径
     * @param markImgPath   水印的路径
     * @param mode          内部枚举类，用于指定水印铺设的样式，平铺，拉伸等
     * @param margin_x      水印之间的水平间距
     * @param margin_y      水印之间的垂直间距
     * @param opacity       水印透明度
     * @param markAngle     水印旋转角度，应在正负45度之间
     *         issue：1.只贴了平铺样式，拉伸只需要将水印图片的长宽设置为和源图片一样大。常规样式只需不循环遍历即可。
     *
     * 　　　　　2.旋转有一定的空间浪费，我本想通过三角函数计算旋转特定角度后循环的起止X，y轴位置，但是计算结果应用后并不能达到预期效果，只好给一个较大的定值。
     *
     * 　　　　　3.添加文字水印更为简单，只需调用graphics2D的drawString方法，并设置字体和颜色即可。希望有需求的朋友多多动手。
     * @throws IOException
     */
    public static void markImage(String originImgPath, String targetImgPath, String markImgPath, int mode, int margin_x,
            int margin_y, float opacity, double markAngle) throws IOException
    {
        if (markAngle > 45 || markAngle < -45)
        {
            throw new RuntimeException("旋转角度必须在正负45度之间。");
        }
        BufferedImage originImg = ImageIO.read(new File(originImgPath));
        BufferedImage markImage = ImageIO.read(new File(markImgPath));
        Graphics2D graphics = (Graphics2D) originImg.getGraphics();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, opacity));
        graphics.rotate(markAngle);

        if (mode == PAINT_MODE.TILED.mode)
        {
            int canvasHeight = originImg.getHeight();
            int canvasWidth = originImg.getWidth();
            int markHeight = markImage.getHeight();
            int markWidth = markImage.getWidth();
            int interval = markWidth + markHeight;
            for (int i = -canvasHeight; i < canvasWidth + canvasHeight; i = i + interval + margin_x)
            {
                for (int j = -canvasWidth; j < canvasHeight + canvasWidth; j = j + interval + margin_y)
                {
                    graphics.drawImage(markImage, i, j, markImage.getWidth(), markImage.getHeight(), null);
                }
            }
        }
        if (mode == PAINT_MODE.REGULAR.mode)
        {
            int canvasHeight = originImg.getHeight();
            int canvasWidth = originImg.getWidth();
            int markHeight = markImage.getHeight();
            int markWidth = markImage.getWidth();
            graphics.drawImage(markImage, canvasWidth/2, canvasHeight/2, markImage.getWidth(), markImage.getHeight(), null);
        }
        if (mode == PAINT_MODE.STRETCHED.mode)
        {

        }
        graphics.dispose();
        ImageIO.write(originImg, "png", new File(targetImgPath));
    }


    public static void markImage(InputStream originImgStream, String targetImgPath, InputStream markImgStream, int mode, int margin_x,
            int margin_y, float opacity, double markAngle) throws IOException
    {
        if (markAngle > 45 || markAngle < -45)
        {
            throw new RuntimeException("旋转角度必须在正负45度之间。");
        }
        BufferedImage originImg = ImageIO.read(originImgStream);
        BufferedImage markImage = ImageIO.read(markImgStream);
        Graphics2D graphics = (Graphics2D) originImg.getGraphics();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, opacity));
        graphics.rotate(markAngle);

        if (mode == PAINT_MODE.TILED.mode)
        {
            int canvasHeight = originImg.getHeight();
            int canvasWidth = originImg.getWidth();
            int markHeight = markImage.getHeight();
            int markWidth = markImage.getWidth();
            int interval = markWidth + markHeight;
            for (int i = -canvasHeight; i < canvasWidth + canvasHeight; i = i + interval + margin_x)
            {
                for (int j = -canvasWidth; j < canvasHeight + canvasWidth; j = j + interval + margin_y)
                {
                    graphics.drawImage(markImage, i, j, markImage.getWidth(), markImage.getHeight(), null);
                }
            }
        }
        if (mode == PAINT_MODE.REGULAR.mode)
        {
            int canvasHeight = originImg.getHeight();
            int canvasWidth = originImg.getWidth();
            int markHeight = markImage.getHeight();
            int markWidth = markImage.getWidth();
            graphics.drawImage(markImage, canvasWidth/2, canvasHeight/2, markImage.getWidth(), markImage.getHeight(), null);
        }
        if (mode == PAINT_MODE.STRETCHED.mode)
        {

        }
        graphics.dispose();
        ImageIO.write(originImg, "png", new File(targetImgPath));
    }


    //内部枚举类
    public enum PAINT_MODE
    {
        REGULAR(0),//常规
        TILED(1),//平铺
        STRETCHED(2);//拉伸
        private int mode;

        PAINT_MODE(int mode)
        {
            this.mode = mode;
        }
        public int getMode()
        {
            return this.mode;
        }
    }


    /**
     * 获取图片正确显示需要旋转的角度（顺时针）
     *
     * @return
     */
    public static int writePhoto(File tempFile, String destUrl)
    {
        int angle = 0;
        Metadata metadata;
        try
        {
            metadata = JpegMetadataReader.readMetadata(tempFile);
            Directory directory = metadata.getFirstDirectoryOfType(ExifDirectoryBase.class);
            int orientation = 0;
            if (directory != null && directory.containsTag(ExifDirectoryBase.TAG_ORIENTATION))
            { // Exif信息中有保存方向,把信息复制到缩略图
                orientation = directory.getInt(ExifDirectoryBase.TAG_ORIENTATION); // 原图片的方向信息
                // 原图片的方向信息
                if (6 == orientation)
                {
                    // 6旋转90
                    angle = 90;
                }
                else if (3 == orientation)
                {
                    // 3旋转180
                    angle = 180;
                }
                else if (8 == orientation)
                {
                    // 8旋转90
                    angle = 270;
                }
                // 先构建
                BufferedImage src = ImageIO.read(tempFile);
                BufferedImage des = Rotate(src, angle);
                ImageIO.write(des, "jpg", new File(destUrl));
                tempFile.delete();
                // 进行压缩
                Thumbnails.of(destUrl).scale(1f).outputQuality(0.5f).toFile(destUrl);
            }
            else
            {// 没有旋转，原样信息
                BufferedImage src = ImageIO.read(tempFile);
                ImageIO.write(src, "jpg", new File(destUrl));
                tempFile.delete();
                // 进行压缩
                Thumbnails.of(destUrl).scale(1f).outputQuality(0.5f).toFile(destUrl);
            }
        }
        catch (JpegProcessingException e)
        {
            e.printStackTrace();
        }
        catch (MetadataException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return angle;
    }

    /**
     * 旋转图片
     * @param src 原文件
     * @param angel 压缩比
     * @return
     */
    private static BufferedImage Rotate(Image src, int angel)
    {
        int src_width = src.getWidth(null);
        int src_height = src.getHeight(null);
        // calculate the new image size
        Rectangle rect_des = CalcRotatedSize(new Rectangle(new Dimension(src_width, src_height)), angel);

        BufferedImage res = null;
        res = new BufferedImage(rect_des.width, rect_des.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = res.createGraphics();
        // transform
        g2.translate((rect_des.width - src_width) / 2, (rect_des.height - src_height) / 2);
        g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);

        g2.drawImage(src, null, null);
        return res;
    }

    private static Rectangle CalcRotatedSize(Rectangle src, int angel)
    {
        // if angel is greater than 90 degree, we need to do some conversion
        if (angel >= 90)
        {
            if (angel / 90 % 2 == 1)
            {
                int temp = src.height;
                src.height = src.width;
                src.width = temp;
            }
            angel = angel % 90;
        }

        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
        double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
        double angel_dalta_width = Math.atan((double) src.height / src.width);
        double angel_dalta_height = Math.atan((double) src.width / src.height);

        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_width));
        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_height));
        int des_width = src.width + len_dalta_width * 2;
        int des_height = src.height + len_dalta_height * 2;
        return new Rectangle(new Dimension(des_width, des_height));
    }

    public static void main(String[] args)
    {

        try
        {
            markImage("F:/学位证.jpg", "F:/new_学位证.jpg", "F:/logo.png", ImageUtil.PAINT_MODE.TILED.mode, 50, 50, 0.5f, -10);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
