package com.wisdom.acm.szxm.form.rygl;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SearchAdboForm
{
    @NotNull(message = "项目ID不能为空")
    private Integer projectId;

    private String queryName;

    @NotNull(message = "组织机构信息不能为空")
    private List<SearchAdboForm.SearchOrgVo> searchOrgs;

    @Data
    public static class SearchOrgVo
    {
        private Integer orgId;

        private String source; //来源 0 项目团队 1组织信息

        public SearchOrgVo() {

        }
    }

}
