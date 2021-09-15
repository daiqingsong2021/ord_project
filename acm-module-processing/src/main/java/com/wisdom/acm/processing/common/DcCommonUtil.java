package com.wisdom.acm.processing.common;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wisdom.acm.processing.mapper.report.SysMessageMapper;
import com.wisdom.acm.processing.po.report.SysMessageRecvPo;
import com.wisdom.acm.processing.po.report.SysMessageUserPo;
import com.wisdom.base.common.dc.util.StringHelper;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.*;
import com.wisdom.base.common.form.SysMessageAddForm;
import com.wisdom.base.common.form.WfRuningProcessForm;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.util.TreeUtil;
import com.wisdom.base.common.util.calc.calendar.PcCalendar;
import com.wisdom.base.common.util.calc.calendar.PmCalendar;
import com.wisdom.base.common.vo.*;
import com.wisdom.base.common.vo.wf.WfActivityVo;
import com.wisdom.base.common.vo.wf.WfCandidateGroupVo;
import com.wisdom.base.common.vo.wf.WfCandidateUserVo;
import com.wisdom.webservice.DateUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zll
 * 2020/8/4/004 15:23
 * Description:<描述>
 */
@Component
public class DcCommonUtil {
    private static final Logger logger = LoggerFactory.getLogger(DcCommonUtil.class);
    @Autowired
    private  CommUserService commUserService;
    @Autowired
    private org.dozer.Mapper dozerMapper;
    @Autowired
    private LeafService leafService;
    @Autowired
    private  CommSysMenuService commSysMenuService;
    @Autowired
    private SysMessageMapper sysMessageMapper;
    /**
     * 定时器配置读取doc的ip地址
     * zll
     * @return
     */
    public String getDocUrl(){
        PropertiesLoaderUtils prop = new PropertiesLoaderUtils("application.yml");
        return prop.getProperty("doc.odrUrl");
    }

    /**
     * 生成pdf路径出错引发的报错，需要重新生成数据
     * zll
     * @return
     */
    public String getDocPdfError(){
        PropertiesLoaderUtils prop = new PropertiesLoaderUtils("application.yml");
        return prop.getProperty("doc.PdfError");
    }

    /**
     * 获取天气的url地址
     * zll
     * @return
     */
    public String getWeatherUrl(){
        PropertiesLoaderUtils prop = new PropertiesLoaderUtils("application.yml");
        return prop.getProperty("weather.url");
    }

    /**
     * 生成预览的PDF给前端
     * @param response 响应
     * @param pdfPath  本地生成的PDF地址
     */
    public  void preViewPdf(HttpServletResponse response, String pdfPath) {
        if (StringHelper.isNullAndEmpty(pdfPath)) {
            throw new BaseException("生成预览审批单pdf出错!");
        }

        File newFile = null;
        FileInputStream fileInputStream;
        try {
            //临时生成文件
            newFile = new File(pdfPath);
            fileInputStream = new FileInputStream(pdfPath);
            // 设置响应头和客户端保存文件名
            String fileName = URLEncoder.encode(newFile.getName(), "UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment;Filename=" + fileName);
            IOUtils.copy(fileInputStream, response.getOutputStream());
            response.getOutputStream().flush();
            response.getOutputStream().close();
            if (null != fileInputStream) {
                fileInputStream.close();
            }
        } catch (Exception e) {
            logger.info("导出预览审批单出错, 原因为{}", e);
            throw new BaseException("导出预览审批单出错!");
        } finally {
            if (!ObjectUtils.isEmpty(newFile)) {
                newFile.delete();
            }
        }
    }

    /**
     * 根据数据字典 Map 查询code对应的name
     *
     * @param dictMap
     * @param dictionaryCode
     * @return
     */
    public String getDictionaryName(Map<String, DictionaryVo> dictMap, String dictionaryCode) {
        String dictionaryName = "";
        if (!ObjectUtils.isEmpty(dictMap) && !StringUtils.isEmpty(dictionaryCode)) {
            dictionaryName = String.valueOf(dictMap.get(dictionaryCode).getName());
        }
        return dictionaryName;
    }

    public <T> void approveFlowAndSendMessage(String bizType, List<T> rawPos) {
        List<Integer> rewiewers = ListUtil.toValueList(rawPos, "reviewer", Integer.class);//审批人，非发起人
        Map<Integer, UserVo> mapByUserIds = commUserService.getUserVoMapByUserIds(rewiewers);
        String menuName = getMenuName(bizType);
        for (T po : rawPos) {
            Date initTime = null;
            Integer initiatorId = null;
            try {
                initTime = (Date)po.getClass().getMethod("getReportName").invoke(po);
                initiatorId = (Integer) po.getClass().getMethod("getReviewer").invoke(po);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                logger.info("获取属性值错误");
                e.printStackTrace();
            }
            if(ObjectUtils.isEmpty(initTime) || ObjectUtils.isEmpty(initiatorId)){
                logger.info("获取表单创建时间或审批人错误，无法发送消息。业务模块为：{}", menuName);
                continue;
            }
            SysMessageAddForm sysMessageAddForm = new SysMessageAddForm();
            sysMessageAddForm.setTitle("哈尔滨项目日报管理系统--流程审批提醒");
            sysMessageAddForm.setContent("您于" + DateUtil.getDateFormat(initTime,DateUtil.DATE_CHINA_FORMAT) +
                    "创建:" + menuName + "表单已审批通过，详情请登录哈尔滨项目日报管理系统查看。");
            List<UserVo> recvUsers = Lists.newArrayList();
            if (!ObjectUtils.isEmpty(mapByUserIds) && !ObjectUtils.isEmpty(mapByUserIds.get(initiatorId))) {
                recvUsers.add(mapByUserIds.get(initiatorId));
                sysMessageAddForm.setRecvUser(recvUsers);
                sysMessageAddForm.setClaimDealTime(new Date());
                sendMessageRecv(sysMessageAddForm);
            }else {
                logger.info("审批通过推送消息错误，创建人为空");
            }
        }
    }

    private String getMenuName(String menuCode){
        return commSysMenuService.queryMenuNameByCode(menuCode).getData().toString();
    }

    public void sendMessageRecv(SysMessageAddForm sysMessageAddForm) {
        //推送系统消息
        this.sendXtMessageRecv(sysMessageAddForm);
        //推送中科软消息
        //ewxService.sendEwxMessage(sysMessageAddForm);
    }

    private void sendXtMessageRecv(SysMessageAddForm sysMessageAddForm) {
        if (StringHelper.isNotNullAndEmpty(sysMessageAddForm.getPcContent()))
            sysMessageAddForm.setContent(sysMessageAddForm.getPcContent());

        SysMessageRecvPo sysMessageRecvPo = dozerMapper.map(sysMessageAddForm, SysMessageRecvPo.class);
        //发件时间
        sysMessageRecvPo.setSendTime(new Date());
        //设置del值为0，普通消息
        sysMessageRecvPo.setDel(0);
        //设置消息类型
        sysMessageRecvPo.setType("SYSTEMNOTICE");
        //默认为未收藏
        sysMessageRecvPo.setCollect(0);
        sysMessageRecvPo.setOptType(0);
        sysMessageRecvPo.setParentId(0);
        sysMessageRecvPo.setSource(1);
        //插入wsd_sys_message
        Integer messageId = leafService.getId();
        sysMessageRecvPo.setId(messageId);
        sysMessageMapper.addSysMessage(sysMessageRecvPo);

        //保存到草稿箱收件人表
        //保存所有收件人
        if (!ObjectUtils.isEmpty(sysMessageAddForm.getRecvUser())) {
            for (UserVo userVo : sysMessageAddForm.getRecvUser()) {
                Integer recvUserId = userVo.getId();
                SysMessageUserPo sysMessageUserPo = new SysMessageUserPo();
                //消息id
                sysMessageUserPo.setMessageId(messageId);
                //收件人
                sysMessageUserPo.setRecvUser(recvUserId);
                //收件时间
                sysMessageUserPo.setRecvTime(new Date());
                //收件人类型(1代表收件人)
                sysMessageUserPo.setType(1);
                //未删除
                sysMessageUserPo.setDel(0);
                //未收藏
                sysMessageUserPo.setCollect(0);
                //未读
                sysMessageUserPo.setRealStatus(0);
                //保存
                Integer messageUserId = leafService.getId();
                sysMessageUserPo.setId(messageUserId);
                sysMessageMapper.addSysMessageUser(sysMessageUserPo);
            }
        }
    }

    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) throws IOException {
//        // TODO Auto-generated method stub
////        System.out.println(System.getProperty("user.dir"));
////        List<Integer>one=Lists.newArrayList();
////        one.add(1);
////        one.add(2);
////        one.add(3);
////        List<Integer>two=Lists.newArrayList();
////        two.add(3);
////        two.add(2);
////        two.add(1);
////        System.out.println(ListUtils.isEqualList(one,two));
////        System.out.println(one.containsAll(two));



//        InputStream fileInputStrem= ResourceUtil.findResoureFile("template/yyrbmb.docx");
//        //先把word表格写进去
//        XWPFDocument document= new XWPFDocument(fileInputStrem);
//        //逻辑：由于使用过一次写word则fileInputStrem再次使用的时候会关闭流，则只能从新生成一个新的word
//        //生成临时目录
//        String tempFlolderStr = System.getProperty("user.dir") + "/temporary";
//        System.out.println("user.dir : "+System.getProperty("user.dir"));
//        File tempFlolder = new File(tempFlolderStr);
//        if (!tempFlolder.isDirectory())
//        {
//            tempFlolder.mkdirs();
//        }
//        String fileName = "/运营日报" +"1号线  "+ System.currentTimeMillis() + ".docx";
//        String outFileName = tempFlolderStr + fileName;
//        //临时生成文件
//        File newFile1 = new File(outFileName);
//        FileOutputStream output = new FileOutputStream(outFileName);
//        document.write(output);
//        //关闭流
//        fileInputStrem.close();
//        output.close();




//        String file = "/系统生成运营日报" +"1号线"+ "%20" +new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".docx";
//        file=file.substring(1);
//        System.out.println(file);
    }
}
