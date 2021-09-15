package com.wisdom.acm.sys.form;


import lombok.Data;
import javax.persistence.Column;
import java.util.List;

@Data
public class SysUserIptAddForm {

    private Integer userId;

    private List<Integer> roleIds;
}
