package com.wisdom.acm.license;

import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.wisdom.base.common.constant.CommonConstants;
import com.wisdom.base.common.util.SpringBootResourceUtils;
import de.schlichtherle.license.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * License校验类
 *
 * @author zifangsky
 * @date 2018/4/20
 * @since 1.0.0
 */
public class LicenseVerify {
    private static Logger logger = LogManager.getLogger(LicenseVerify.class);

    /**
     * 安装License证书
     *
     * @author zifangsky
     * @date 2018/4/20 16:26
     * @since 1.0.0
     */
    public synchronized LicenseContent install(LicenseVerifyParam param) {
        LicenseContent result = null;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        StringBuffer sb = new StringBuffer();
        sb.append("\n----------ACM Authorization----------").append("\n\n");

        //1. 安装证书
        try {
            LicenseManager licenseManager = LicenseManagerHolder.getInstance(initLicenseParam(param));
            licenseManager.uninstall();

            Resource resource = new ClassPathResource(param.getLicensePath());
            //File file = resource.getFile();

//            InputStream inputStream = resource.getInputStream();
//            File file = new File("templicense.lic");
//            OutputStream out = new FileOutputStream(file);
//            IOUtils.copy(inputStream, out);
//            out.flush();
//            out.close();

            File file = SpringBootResourceUtils.getResource(param.getLicensePath());

            result = licenseManager.install(file);
            //logger.info(MessageFormat.format("证书安装成功，证书有效期：{0} - {1}", format.format(result.getNotBefore()), format.format(result.getNotAfter())));
            LicenseCheckModel extra = (LicenseCheckModel) result.getExtra();
            sb.append("PM Serial : " + extra.getSerialNumber()).append("\n");
            sb.append("From & End Date : " + format.format(result.getNotBefore()) + " -- " + format.format(result.getNotAfter())).append("\n");
            sb.append("Licence Type : " + (extra.isOfficial() ? "正式许可" : "临时许可")).append("\n");
            sb.append("Licence Business : " + extra.getBundles()).append("\n");
            sb.append("Agreement To : " + extra.getCompany()).append("\n");
            List<String> bundles = extra.getBundles();
            if (!CollectionUtils.isEmpty(bundles)) {
                CommonConstants.BUNDLESPERMISSION = String.join(",", bundles);
            }
            sb.append("\n----------ACM Authorization----------\n");
            System.out.println(sb.toString());
        } catch (Exception e) {
            // logger.error("证书安装失败！", e);
            e.printStackTrace();
            sb.append("证书安装失败！" + e.getMessage()).append("\n");
            sb.append("\n----------ACM Authorization----------\n");
            System.out.println(sb.toString());
            System.exit(0); // 强制系统启动终止
        }

        // 许可过期
        if (result.getNotAfter().before(new Date())) {
            System.out.println("Warning: To ACM Licence Expiry Date, Server 30 seconds after the termination of the exit!----------");
            for (int i = 0; i < 30; i++) {
                System.out.println("\nWarning: 许可已过期," + (30 - i) + "秒后系统自动退出启动!----------");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
            System.exit(0); // 强制系统启动终止
        }
        return result;
    }

    /**
     * 校验License证书
     *
     * @return boolean
     * @author zifangsky
     * @date 2018/4/20 16:26
     * @since 1.0.0
     */
    public boolean verify() {
        LicenseManager licenseManager = LicenseManagerHolder.getInstance(null);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //2. 校验证书
        try {
            LicenseContent licenseContent = licenseManager.verify();
//            System.out.println(licenseContent.getSubject());

            logger.info(MessageFormat.format("证书校验通过，证书有效期：{0} - {1}", format.format(licenseContent.getNotBefore()), format.format(licenseContent.getNotAfter())));
            return true;
        } catch (Exception e) {
            logger.error("证书校验失败！", e);
            return false;
        }
    }

    /**
     * 初始化证书生成参数
     *
     * @param param License校验类需要的参数
     * @return de.schlichtherle.license.LicenseParam
     * @author zifangsky
     * @date 2018/4/20 10:56
     * @since 1.0.0
     */
    private LicenseParam initLicenseParam(LicenseVerifyParam param) {
        Preferences preferences = Preferences.userNodeForPackage(LicenseVerify.class);

        CipherParam cipherParam = new DefaultCipherParam(param.getStorePass());

        KeyStoreParam publicStoreParam = new CustomKeyStoreParam(LicenseVerify.class
                , param.getPublicKeysStorePath()
                , param.getPublicAlias()
                , param.getStorePass()
                , null);

        return new DefaultLicenseParam(param.getSubject()
                , preferences
                , publicStoreParam
                , cipherParam);
    }

}
