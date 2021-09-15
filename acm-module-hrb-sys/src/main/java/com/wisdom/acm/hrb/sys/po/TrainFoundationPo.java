package com.wisdom.acm.hrb.sys.po;
import com.wisdom.base.common.po.BasePo;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Table;
@Data
@Table(name="odr_train_foundation")
public class TrainFoundationPo extends BasePo {
    @Column(name = "train_code")
    private String trainCode;
    @Column(name = "line")
    private String line;
}
