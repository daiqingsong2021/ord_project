package com.wisdom.acm.szxm.controller.rygl;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.szxm.common.ImageUtil;
import com.wisdom.acm.szxm.common.SzxmCommonUtil;
import com.wisdom.acm.szxm.form.rygl.*;
import com.wisdom.acm.szxm.service.rygl.SpecialWorkCertService;
import com.wisdom.acm.szxm.service.rygl.SpecialWorkerService;
import com.wisdom.acm.szxm.service.rygl.CertGlService;
import com.wisdom.acm.szxm.vo.rygl.CertGlVo;
import com.wisdom.acm.szxm.vo.rygl.SpecialWorkCertVo;
import com.wisdom.acm.szxm.vo.rygl.SpecialWorkerVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import com.wisdom.base.common.util.ResourceUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * 特殊工种控制器
 */
@Controller @RestController @RequestMapping("rygl/specialWorker") public class SpecialWorkerController
{
    @Autowired private HttpServletRequest request;

    @Autowired private SzxmCommonUtil szxmCommonUtil;

    @Autowired private SpecialWorkerService specialWorkerService;

    @Autowired private CertGlService certGlService;

    @Autowired private SpecialWorkCertService specialWorkCertService;

    private static final Logger logger = LoggerFactory.getLogger(SpecialWorkerController.class);

    @GetMapping(value = "/getSpecialWorkerList/{pageSize}/{currentPageNum}")
    public ApiResult getSpecialWorkerList(@RequestParam Map<String, Object> mapWhere,
            @PathVariable("pageSize") Integer pageSize, @PathVariable("currentPageNum") Integer currentPageNum)
    {
        String projectId = request.getParameter("projectId");//获取项目ID
        String sectionIds = request.getParameter("sectionIds");//获取标段ID集合

        List<String> sectionList = szxmCommonUtil.getSectionList(projectId, sectionIds);//查询能看到的所有标段List
        PageInfo<SpecialWorkerVo> querySpecialWorkerVoList =
                specialWorkerService.selectSpecialWorkerList(mapWhere, sectionList, pageSize, currentPageNum);
        return new TableResultResponse(querySpecialWorkerVoList);
    }

    @GetMapping(value = "/getWarningInformation/{pageSize}/{currentPageNum}")
    public ApiResult getWarningInformation(@RequestParam Map<String, Object> mapWhere,
                                           @PathVariable("pageSize") Integer pageSize, @PathVariable("currentPageNum") Integer currentPageNum)
    {
        return  ApiResult.success(specialWorkerService.getWarningInformation(mapWhere, pageSize, currentPageNum));
    }

    @GetMapping(value = "/getFlowSpecialWorkerList")
    public ApiResult getFlowSpecialWorkerList(@RequestParam Map<String, Object> mapWhere)
    {
        String ids = request.getParameter("ids");//获取项目ID
        List<SpecialWorkerVo> specialWorkerVoList;
        if (ObjectUtils.isEmpty(ids))
        {
            String projectId = request.getParameter("projectId");//获取项目ID
            String sectionIds = request.getParameter("sectionIds");//获取标段ID集合
            List<String> sectionList = szxmCommonUtil.getSectionList(projectId, sectionIds);//查询能看到的所有标段List
            specialWorkerVoList = specialWorkerService.getFlowSpecialWorkerList(mapWhere, sectionList);
        }
        else
        {
            specialWorkerVoList = specialWorkerService.getFlowSpecialWorkerList(mapWhere, null);
        }
        return ApiResult.success(specialWorkerVoList);
    }

    @GetMapping(value = "/getSpecialWorker/{id}") public ApiResult getSpecialWorker(@PathVariable("id") Integer id)
    {
        if (ObjectUtils.isEmpty(id))
        {
            return ApiResult.result(1001, "id不能为空");
        }
        return ApiResult.success(specialWorkerService.selectBySpecialWorkerId(id));
    }

    @GetMapping(value = "/getCertGlList") public ApiResult getCertGlList(@RequestParam Map<String, Object> mapWhere)
    {
        List<CertGlVo> swTypeChoseVoList = certGlService.getCertGlList(mapWhere);
        return ApiResult.success(swTypeChoseVoList);
    }

    @PostMapping(value = "/addSpecialWorker")
    public ApiResult addSpecialWorker(@RequestBody @Valid SpecialWorkerAddForm specialWorkerAddForm)
    {
        SpecialWorkerVo specialWorkerVo = specialWorkerService.addSpecialWorker(specialWorkerAddForm);
        return ApiResult.success(specialWorkerVo);
    }

    @DeleteMapping(value = "/deleteSpecialWorker") public ApiResult deleteSpecialWorker(@RequestBody List<Integer> ids)
    {
        if (ObjectUtils.isEmpty(ids))
        {
            return ApiResult.result(1001, "id不能为空");
        }
        specialWorkerService.deleteSpecialWorker(ids);
        return ApiResult.success();
    }

    @PutMapping(value = "/updateSpecialWorker")
    public ApiResult updateSpecialWorker(@RequestBody @Valid SpecialWorkerUpdateForm specialWorkerUpdateForm)
    {
        SpecialWorkerVo specialWorkerVo = specialWorkerService.updateSpecialWorker(specialWorkerUpdateForm);
        return ApiResult.success(specialWorkerVo);
    }

    @PostMapping(value = "/addCertGl") public ApiResult addCertGl(@RequestBody @Valid CertGlAddForm sertGlAddForm)
    {
        CertGlVo certGlVo = certGlService.addCertGl(sertGlAddForm);
        return ApiResult.success(certGlVo);
    }

    @GetMapping(value = "/getCertGlList/{pageSize}/{currentPageNum}")
    public ApiResult getCertGlList(@RequestParam Map<String, Object> mapWhere,
            @PathVariable("pageSize") Integer pageSize, @PathVariable("currentPageNum") Integer currentPageNum)
    {
        String projectId = request.getParameter("projectId");//获取项目ID
        if (StringUtils.isEmpty(projectId))
        {
            throw new BaseException("项目ID不能为空");
        }
        PageInfo<CertGlVo> certGlVoList = certGlService.selectCertGlList(mapWhere, pageSize, currentPageNum);
        return new TableResultResponse(certGlVoList);
    }

    @GetMapping(value = "/getCertGl/{id}") public ApiResult getCertGl(@PathVariable("id") Integer id)
    {
        if (ObjectUtils.isEmpty(id))
        {
            return ApiResult.result(1001, "id不能为空");
        }
        return ApiResult.success(certGlService.selectByCertGlById(id));
    }

    @DeleteMapping(value = "/deleteCertGl") public ApiResult deleteCertGl(@RequestBody List<Integer> ids)
    {
        if (ObjectUtils.isEmpty(ids))
        {
            return ApiResult.result(1001, "id不能为空");
        }
        certGlService.deleteCertGl(ids);
        return ApiResult.success();
    }

    @PutMapping(value = "/updateCertGl")
    public ApiResult updateCertGl(@RequestBody @Valid CertGlUpdateForm certGlUpdateForm)
    {
        CertGlVo certGlVo = certGlService.updateCertGl(certGlUpdateForm);
        return ApiResult.success(certGlVo);
    }

    @DeleteMapping(value = "/deleteSpecialWorkCert")
    public ApiResult deleteSpecialWorkCert(@RequestBody List<Integer> ids)
    {
        if (ObjectUtils.isEmpty(ids))
        {
            return ApiResult.result(1001, "id不能为空");
        }
        specialWorkCertService.deleteSpecialWorkCert(ids);
        return ApiResult.success();
    }

    @PutMapping(value = "/updateSpecialWorkCert")
    public ApiResult updateSpecialWorkCert(@RequestBody @Valid SpecialWorkCertUpdateForm specialWorkCertUpdateForm)
    {
        SpecialWorkCertVo specialWorkCertVo = specialWorkCertService.updateSpecialWorkCert(specialWorkCertUpdateForm);
        return ApiResult.success(specialWorkCertVo);
    }

    @PostMapping(value = "/addSpecialWorkCert")
    public ApiResult addCertGl(@RequestBody @Valid SpecialWorkCertAddForm specialWorkCertAddForm)
    {
        SpecialWorkCertVo specialWorkCertVo = specialWorkCertService.addSpecialWorkCert(specialWorkCertAddForm);
        return ApiResult.success(specialWorkCertVo);
    }

    @PostMapping(value = "/uploadSpecialWorkCert")
    public ApiResult uploadSpecialWorkCert(@RequestParam("file") MultipartFile file)
    {
        String fileName = file.getOriginalFilename();//文件名
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);//文件类型
        try
        {
            if ("jpg".equals(ext) || "jepg".equals(ext) || "bmp".equals(ext) || "png".equals(ext))
            {//如果是图片格式
                //生成临时目录
                String tempFlolderStr=System.getProperty( "user.dir")+ "/temporary";
                File tempFlolder=new File(tempFlolderStr);
                if(!tempFlolder.isDirectory())
                    tempFlolder.mkdirs();
                //创建临时文件URL
                String tempFileUrl =
                        System.getProperty( "user.dir" ) + "/temporary/" + fileName;
                InputStream logoStrem = ResourceUtil.findResoureFile("images/logo.png");
                ImageUtil.markImage(file.getInputStream(), tempFileUrl, logoStrem, ImageUtil.PAINT_MODE.TILED.getMode(), 50, 50, 0.5f, -10);
                //对图片压缩
                // 进行压缩
                Thumbnails.of(tempFileUrl).scale(1f).outputQuality(0.5f).toFile(tempFileUrl);

                FileSystemResource fileSystemResource = new FileSystemResource(tempFileUrl);
                Map<String,Object> returnMap=uploadFile(fileSystemResource);
                File newFile=new File(tempFileUrl);
                newFile.delete();
                if(logoStrem!=null)
                    logoStrem.close();
                if("200".equals(String.valueOf(returnMap.get("status"))) && "true".equals(String.valueOf(returnMap.get("success"))))
                {

                    return ApiResult.success(returnMap.get("data"));
                }
                else
                {
                    return ApiResult.error("调用doc服务异常");

                }
            }
            else
            {
                //生成临时目录
                String tempFlolderStr=System.getProperty( "user.dir" )+ "/temporary";
                File tempFlolder=new File(tempFlolderStr);
                if(!tempFlolder.isDirectory())
                    tempFlolder.mkdirs();
                //创建临时文件URL
                String tempFileUrl =
                        System.getProperty( "user.dir" ) + "/temporary/" + fileName;
                File newFile=new File(tempFileUrl);
                if(!newFile.exists())
                   newFile.createNewFile();
                file.transferTo(newFile);

                FileSystemResource fileSystemResource = new FileSystemResource(tempFileUrl);
                Map<String,Object> returnMap=uploadFile(fileSystemResource);
                newFile.delete();
                if("200".equals(String.valueOf(returnMap.get("status"))) && "true".equals(String.valueOf(returnMap.get("success"))))
                {
                    return ApiResult.success(returnMap.get("data"));
                }
                else
                {
                    return ApiResult.error("调用doc服务异常");

                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new BaseException("添加水印异常!");
        }
    }

    public Map<String,Object> uploadFile(FileSystemResource fileSystemResource)
    {
        String token=request.getHeader("Authorization");//获取认证Token
        String gateWayHost=request.getHeader("gateway_host");//获取网关请求地址
        logger.info("gateWayHost:"+gateWayHost);
        logger.info("token:"+token);
        RestTemplate restTemplate=new RestTemplate();
        // 发送请求
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("multipart/form-data");
        headers.setContentType(type);
        headers.set("Authorization",token);
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("file", fileSystemResource);
        form.add("filename",fileSystemResource.getFilename());
        HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity<>(form, headers);
        Map<String,Object> returnMap = restTemplate.postForObject("http://"+gateWayHost+"/api/doc/file/upload", files, Map.class);
        return returnMap;
    }

    @GetMapping(value = "/{specialWorkerId}/getSpecialWorkCertList")
    public ApiResult getSpecialWorkCertList(@RequestParam Map<String, Object> mapWhere,
            @PathVariable("specialWorkerId") Integer specialWorkerId)
    {
        mapWhere.put("specialWorkerId", specialWorkerId);
        List<SpecialWorkCertVo> specialWorkCertVoList = specialWorkCertService.selectSpecialWorkCert(mapWhere);
        return ApiResult.success(specialWorkCertVoList);
    }


    /**
     * 根据人员id 查找相关证书
     * @param mapWhere
     * @return
     */
    @GetMapping(value = "/getSectionCertGlList/{userId}")
    public ApiResult getSectionCertGlList(@RequestParam Map<String, Object> mapWhere, @PathVariable("userId") Integer userId)
    {
       // String userId=request.getHeader("userId");//用户id
        if (ObjectUtils.isEmpty(userId))
        {
            return ApiResult.result(1001, "userId不能为空");
        }
        List<SpecialWorkCertVo> swTypeChoseVoList = specialWorkCertService.getCertByUserIdList(mapWhere);
        return ApiResult.success(swTypeChoseVoList);
    }


    public static void main(String args[])
    {

        try
        {
            File file=new File("F:/22.txt");
            if(file.exists())
                file.delete();
            file.createNewFile();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }







}
