package com.wisdom.acm.hrb.sys.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author zll
 * 2020/10/20/020 10:06
 * Description:<线路管理信息>
 */
@Data
@Table(name="odr_line_foundation")
public class LineFoundationPo extends BasePo {
      /**
       *线路名称
       */
      @Column(name = "line")
      private String line;
      /**
       *线路编号
       */
      @Column(name = "line_code")
      private String lineCode;
      /**
       *投入运行时间
       */
      @Column(name = "operation_time")
      private Date operationTime;
      /**
       * 安全运行起始日期
       */
      @Column(name = "saft_operation_time")
      private Date saftOperationTime;
      /**
       *  运营公司
       */
      @Column(name = "company")
      private String company;
}
