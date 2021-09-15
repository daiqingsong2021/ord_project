package com.wisdom.acm.sys.vo;

import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.TreeVo;
import lombok.Data;

import java.util.List;

@Data
public class SysOrgUserTreeVo extends TreeVo<SysOrgUserTreeVo> {

    private String name;

    private String code;

    private String orgType;

    private String type;

    private List<Integer> roleId;

    public void addChildrens(List<SysOrgUserTreeVo> list){
        if(list != null){
            if(this.getChildren() != null){
                this.getChildren().addAll(list);
            }else{
                this.setChildren(list);
            }
        }
    }
}
