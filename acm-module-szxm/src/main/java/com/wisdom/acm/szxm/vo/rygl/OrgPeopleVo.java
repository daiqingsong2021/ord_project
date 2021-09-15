package com.wisdom.acm.szxm.vo.rygl;

import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.TreeVo;
import lombok.Data;

import java.util.List;

@Data
public class OrgPeopleVo extends TreeVo
{

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 组织名称
     */
    private String orgName;

    /**
     * 单位分类Code 对应的 Name base.org.classification
     */
    private GeneralVo orgCategoryVo=new GeneralVo();


    /**
     * 单位类型Code 对应的 Name base.org.type
     */
    private GeneralVo orgTypeVo=new GeneralVo();

    /**
     * projInfoId
     */
    private Integer projInfoId;

    /**
     * 人员职务 名称
     */
    private String positionName;

    /**
     * 人员分类
     */
    private String ryType;

    /**
     * 工种名称
     */
    private String workTypeName;
    /**
     * org 组织结构 people 人员
     */
    private String type;

    public void addChildrens(List<OrgPeopleVo> list){
        if(list != null && !list.isEmpty()){
            if(this.getChildren() != null){
                this.getChildren().addAll(list);
            }else{
                this.setChildren(list);
            }
        }
    }

}
