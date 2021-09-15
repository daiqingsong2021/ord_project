package com.wisdom.acm.sys.controller;

import com.wisdom.acm.sys.basisrc.DESUtil;
import com.wisdom.acm.sys.license.AbstractServerInfos;
import com.wisdom.acm.sys.license.LicenseCheckModel;
import com.wisdom.acm.sys.license.LinuxServerInfos;
import com.wisdom.acm.sys.license.WindowsServerInfos;
import com.wisdom.base.common.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生成许可序列号
 *
 * @author zifangsky
 * @date 2018/4/26
 * @since 1.0.0
 */
@RestController
@RequestMapping("/license")
public class LicenseSerialNumberController {


    /**
     * 生成序列号
     *
     * @return
     */
    @RequestMapping(value = "/getLicense/serialNumber", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public String getLicenseSerialNumber(@RequestParam(value = "osName", required = false) String osName) {
        //操作系统类型
        if (StringUtils.isBlank(osName)) {
            osName = System.getProperty("os.name");
        }
        osName = osName.toLowerCase();

        AbstractServerInfos abstractServerInfos = null;

        //根据不同操作系统类型选择不同的数据获取方法
        if (osName.startsWith("windows")) {
            abstractServerInfos = new WindowsServerInfos();
        } else if (osName.startsWith("linux")) {
            abstractServerInfos = new LinuxServerInfos();
        } else {//其他服务器类型
            abstractServerInfos = new LinuxServerInfos();
        }

        LicenseCheckModel licenseCheckModel = abstractServerInfos.getServerInfos();
        //生成序列号
        if (!ObjectUtils.isEmpty(licenseCheckModel)) {
            Map<String,Object> infoMap = new HashMap<>();
            //获取允许ip地址
            List<String> ips = licenseCheckModel.getIpAddress();
            infoMap.put("ipAddress",ips);
            //获取MAC地址
            List<String> macs = licenseCheckModel.getMacAddress();
            infoMap.put("macAddress",macs);
            //获取CPU序列号
            if (!ObjectUtils.isEmpty(licenseCheckModel.getCpuSerial())) {
                infoMap.put("cpuSerial",licenseCheckModel.getCpuSerial());
            }
            //获取主板序列号
            if (!ObjectUtils.isEmpty(licenseCheckModel.getMainBoardSerial())) {
                infoMap.put("mainBoardSerial",licenseCheckModel.getMainBoardSerial());
            }

            String jsonSeria = JsonUtil.toJson(infoMap);
            try {
                String entry1 = DESUtil.encode(jsonSeria);
                String decode1 = DESUtil.decode(entry1);
                return entry1;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return "";
    }
}
