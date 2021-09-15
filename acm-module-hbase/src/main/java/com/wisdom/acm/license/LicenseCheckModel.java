package com.wisdom.acm.license;

import java.io.Serializable;
import java.util.List;

/**
 * 自定义需要校验的License参数
 *
 * @author zifangsky
 * @date 2018/4/23
 * @since 1.0.0
 */
public class LicenseCheckModel implements Serializable {

    private static final long serialVersionUID = 8600137500316662317L;

    /**
     * 授权公司
     */
    private String company = null;

    /**
     * 正式许可|临时许可
     */
    private boolean isOfficial = false;

    /**
     * 产品序列号
     */
    private String serialNumber = null;

    /**
     * 授权模块
     */
    private List<String> bundles = null;

    /**
     * 可被允许的IP地址
     */
    private List<String> ipAddress;

    /**
     * 可被允许的MAC地址
     */
    private List<String> macAddress;

    /**
     * 可被允许的CPU序列号
     */
    private String cpuSerial;

    /**
     * 可被允许的主板序列号
     */
    private String mainBoardSerial;

    public List<String> getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(List<String> ipAddress) {
        this.ipAddress = ipAddress;
    }

    public List<String> getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(List<String> macAddress) {
        this.macAddress = macAddress;
    }

    public String getCpuSerial() {
        return cpuSerial;
    }

    public void setCpuSerial(String cpuSerial) {
        this.cpuSerial = cpuSerial;
    }

    public String getMainBoardSerial() {
        return mainBoardSerial;
    }

    public void setMainBoardSerial(String mainBoardSerial) {
        this.mainBoardSerial = mainBoardSerial;
    }

    public void setBundles(List<String> bundles) {
        this.bundles = bundles;
    }

    public List<String> getBundles() {
        return bundles;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getCompany() {
        return company;
    }

    public void setOfficial(boolean official) {
        isOfficial = official;
    }

    public boolean isOfficial() {
        return isOfficial;
    }

    @Override
    public String toString() {
        return "LicenseCheckModel{" +
                "company=" + company +
                "serialNumber=" + serialNumber +
                "isOfficial=" + false +
                "bundles=" + bundles +
                "ipAddress=" + ipAddress +
                ", macAddress=" + macAddress +
                ", cpuSerial='" + cpuSerial + '\'' +
                ", mainBoardSerial='" + mainBoardSerial + '\'' +
                '}';
    }
}
