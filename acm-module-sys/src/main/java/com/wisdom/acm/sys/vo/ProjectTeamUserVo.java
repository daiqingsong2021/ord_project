package com.wisdom.acm.sys.vo;

import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.OrgVo;
import com.wisdom.base.common.vo.UserVo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
public class ProjectTeamUserVo {

    private Integer id;

    private Integer teamId;

    /**
     * 团队代码
     */
    private String teamCode;

    private String teamName;

    private UserVo user;

    private List<GeneralVo> roles;
    /**
     * 职务
     */
    private String position;
    /**
     * 专业
     */
    private String professional;

    /**
     * 员工状态
     */
    private GeneralVo staffStatus;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 出生日期
     */
    private Date birth;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 用户性别
     */
    private GeneralVo sex;

    private String email;

    /**
     * 证件类型
     */
    private String cardType;

    /**
     * 证件号码
     */
    private String cardNum;
    /**
     * 状态
     */
    private GeneralVo status;
    /**
     * 密级
     */
    private GeneralVo level;

    private Integer sortNum;

}
