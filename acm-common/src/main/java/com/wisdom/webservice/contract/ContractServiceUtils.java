package com.wisdom.webservice.contract;

import com.wisdom.webservice.PropertiesLoaderUtils;
import com.wisdom.webservice.contract.entity.ArrayOfContractListForGC;
import com.wisdom.webservice.contract.entity.ArrayOfPaymentForGC;
import com.wisdom.webservice.contract.entity.ArrayOfSectionEntity;
import com.wisdom.webservice.contract.entity.ArrayOfSegContractEntity;
import com.wisdom.webservice.contract.entity.ArrayOfWFDoneList;
import com.wisdom.webservice.contract.entity.ArrayOfWFTodoList;
import com.wisdom.webservice.contract.entity.ContractListForGC;
import com.wisdom.webservice.contract.entity.EntityRequestOfContractListForGC;
import com.wisdom.webservice.contract.entity.EntityRequestOfSectionEntity;
import com.wisdom.webservice.contract.entity.EntityResponseOfContractListForGC;
import com.wisdom.webservice.contract.entity.EntityResponseOfPaymentForGC;
import com.wisdom.webservice.contract.entity.EntityResponseOfSectionEntity;
import com.wisdom.webservice.contract.entity.EntityResponseOfSegContractEntity;
import com.wisdom.webservice.contract.entity.EntityResponseOfWFDoneList;
import com.wisdom.webservice.contract.entity.EntityResponseOfWFTodoList;
import com.wisdom.webservice.contract.entity.GCService;
import com.wisdom.webservice.contract.entity.GCServiceSoap;
import com.wisdom.webservice.contract.entity.PaymentForGC;
import com.wisdom.webservice.contract.entity.SectionEntity;
import com.wisdom.webservice.contract.entity.SegContractEntity;
import com.wisdom.webservice.contract.entity.WFDoneList;
import com.wisdom.webservice.contract.entity.WFTodoList;
import com.wisdom.webservice.token.MyHeaderHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;
import java.util.ArrayList;
import java.util.List;

public class ContractServiceUtils {
    private static final ContractServiceUtils INSTANCE = new ContractServiceUtils();
    private static Logger logger = LoggerFactory.getLogger(ContractServiceUtils.class);
    private String userName;

    private String password;

    public static ContractServiceUtils getInstance() {
        PropertiesLoaderUtils prop = new PropertiesLoaderUtils("webServiceConfig.yml");
        String userName = prop.getProperty("webservice.esb.username");
        String password = prop.getProperty("webservice.esb.password");
        return new ContractServiceUtils(userName, password);
    }

    private ContractServiceUtils(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    private ContractServiceUtils() {

    }

    /**
     * +
     * 获取调用对象
     *
     * @return
     */
    private GCServiceSoap getService() {
        GCService gcService = new GCService();
        GCServiceSoap gcServiceSoap = null;
        try {
            //添加Header信息
            gcService.setHandlerResolver(new HandlerResolver() {
                @Override
                public List<Handler> getHandlerChain(PortInfo portInfo) {
                    List<Handler> handlerList = new ArrayList<>();

                    handlerList.add(new MyHeaderHandler(userName, password));
                    return handlerList;
                }
            });
            gcServiceSoap = gcService.getGCServiceSoap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gcServiceSoap;
    }

    /**
     * 获取标段信息
     *
     * @param request
     * @return
     */
    public List<SectionEntity> getAllSection(
            EntityRequestOfSectionEntity request) {

        EntityResponseOfSectionEntity section = getService()
                .getSection(request);
        if (0 != section.getErrorCode()) {
            return null;
        }
        ArrayOfSectionEntity results = section.getResults();
        if (ObjectUtils.isEmpty(results)) {
            return null;
        }
        return results.getSectionEntity();
    }

    /**
     * 根据标段查询合同
     *
     * @param segCode
     * @return
     */
    public List<SegContractEntity> getContractBySegCode(String segCode) {
        EntityResponseOfSegContractEntity contractBySegCode = getService()
                .getContractBySegCode(segCode);
        if (0 != contractBySegCode.getErrorCode()) {
            return null;
        }
        ArrayOfSegContractEntity results = contractBySegCode.getResults();
        if (null == results) {
            return null;
        }
        List<SegContractEntity> segContractEntity = results
                .getSegContractEntity();
        return segContractEntity;
    }

    /**
     * 根据标段查询主合同编号
     *
     * @param segCode 标段号
     * @return 主合同编号
     */
    public String getContractNo(String segCode) {
        List<SegContractEntity> contractBySegCode = null;
        try {
            EntityResponseOfSegContractEntity contract = getService().getContractBySegCode(segCode + "标");
            if (ObjectUtils.isEmpty(contract) || ObjectUtils.isEmpty(contract.getResults()) || ObjectUtils.isEmpty(contract.getResults().getSegContractEntity())) {
                // 标段号 中部分 末尾是 “标” ，有的标段 仅是字符组成。但在本系统中 标段号 不包含 “标”，所以在此查询两遍
                contract = getService().getContractBySegCode(segCode);
                if (ObjectUtils.isEmpty(contract) || ObjectUtils.isEmpty(contract.getResults()) || ObjectUtils.isEmpty(contract.getResults().getSegContractEntity())) {
                    return " ";
                }
            }
            contractBySegCode = contract.getResults().getSegContractEntity();
        } catch (Exception e) {
            logger.info("调用品高获取合同接口异常，{}", e);
        }
        String hth = "";
        if (!ObjectUtils.isEmpty(contractBySegCode)) {
            for (int i = 0; i < contractBySegCode.size(); i++) {
                SegContractEntity contractEntity = contractBySegCode.get(i);
                String hasfxyj = contractEntity.getHASFXYJ();
                if ("true".equalsIgnoreCase(hasfxyj)) {
                    hth = contractEntity.getContractCode();
                    break;
                }
                if (i == contractBySegCode.size() - 1) {
                    hth = contractBySegCode.get(0).getContractCode();
                }
            }
        } else {
            hth = " ";
        }
        return hth;
    }

    /**
     * 根据合同获取合同的计量支付
     *
     * @param contrctCode
     * @return
     */
    public List<PaymentForGC> getPaymentByContractCode(String contrctCode) {
        EntityResponseOfPaymentForGC paymentByContractCode = getService()
                .getPaymentByContractCode(contrctCode);
        if (0 != paymentByContractCode.getErrorCode()) {
            return null;
        }
        ArrayOfPaymentForGC results = paymentByContractCode.getResults();
        List<PaymentForGC> paymentForGC = results.getPaymentForGC();
        return paymentForGC;
    }

    /**
     * 获取所有合同清单数据
     *
     * @param request
     * @return
     */
    Integer getAllContractList(EntityRequestOfContractListForGC request) {
        EntityResponseOfContractListForGC allContractList = getService()
                .getAllContractList(request);
        if (0 != allContractList.getErrorCode()) {
            return null;
        }
        ArrayOfContractListForGC results = allContractList.getResults();
        List<ContractListForGC> contractListForGC = results
                .getContractListForGC();

        int totalRecords = allContractList.getTotalRecords();
        return totalRecords;
    }

    /**
     * 根据合同查询清单
     *
     * @param contrctCode
     * @return
     */
    public List<ContractListForGC> getContractListByCode(String contrctCode) {
        EntityResponseOfContractListForGC contractListByCode = getService()
                .getContractListByCode(contrctCode);
        if (0 != contractListByCode.getErrorCode()) {
            return null;
        }
        ArrayOfContractListForGC results = contractListByCode.getResults();
        List<ContractListForGC> contractListForGC = results
                .getContractListForGC();
        return contractListForGC;
    }

    /**
     * 获取待办任务列表
     *
     * @param partID
     * @return
     */
    public List<WFTodoList> getToDoList(String partID) {
        EntityResponseOfWFTodoList toDoList = getService().getToDoList(partID);
        if (0 != toDoList.getErrorCode()) {
            return null;
        }
        ArrayOfWFTodoList results = toDoList.getResults();
        List<WFTodoList> wfTodoList = results.getWFTodoList();
        return wfTodoList;
    }

    /**
     * 获取已办任务列表
     *
     * @param partID
     * @return
     */
    public List<WFDoneList> getDoneList(String partID) {
        EntityResponseOfWFDoneList doneList = getService().getDoneList(partID);
        if (0 != doneList.getErrorCode()) {
            return null;
        }
        ArrayOfWFDoneList results = doneList.getResults();
        List<WFDoneList> wfDoneList = results.getWFDoneList();
        return wfDoneList;
    }

    public static void main(String[] args) {

        // EntityRequestOfSectionEntity request = new EntityRequestOfSectionEntity();
        // request.setIsPaging(true);
        // request.setPageIndex(1);
        // request.setPageCount(0);
        // request.setSortingField("LastUpdateDate Desc");
        // request.setLastUpdateDate(DateUtil
        //         .convertToXMLGregorianCalendar("1970-01-01", DateUtil.DATE_DEFAULT_FORMAT));
        // List<SectionEntity> sectionEntitys = ContractServiceUtils.getInstance()
        //         .getAllSection(request);
        // for (SectionEntity sectionEntity : sectionEntitys) {
        //     System.out.println(sectionEntity.toString());
        // }

        List<SegContractEntity> contractBySegCode =
                ContractServiceUtils.getInstance().getContractBySegCode("SRT5-2-1标");
        System.out.println(contractBySegCode.toString());

        //获取合同计量信息
        // List<PaymentForGC> paymentByContractCode = ContractServiceUtils
        //  		.getInstance().getPaymentByContractCode("SZGY05MM202000436");
        //  for(PaymentForGC paymentForGC:  paymentByContractCode)
        //  {
        // 	System.out.println(paymentForGC.toString());
        //  }
        //
		// EntityRequestOfContractListForGC contractListForGC = new EntityRequestOfContractListForGC();
		// contractListForGC.setIsPaging(false);
		// contractListForGC.setPageCount(0);
		// contractListForGC.setPageIndex(0);
		// contractListForGC.setSortingField("LastUpdateDate Desc");
		// contractListForGC.setLastUpdateDate( DateUtil
		// 		.convertToXMLGregorianCalendar("2019-01-01", DateUtil.DATE_DEFAULT_FORMAT));
		// Integer allContractList = ContractServiceUtils.getInstance()
		// 		.getAllContractList(contractListForGC);
		// System.out.println("all Contract total :" + allContractList);

        // //获取合同物料列表信息
		// List<ContractListForGC> contractListByCode = ContractServiceUtils
		// 		.getInstance().getContractListByCode("SZZG08CG1050012");
		// for(ContractListForGC listForGC: contractListByCode)
		// {
		// 	System.out.println(listForGC.toString());
		// }

    }
}
