package com.wisdom.acm.szxm.vo.doc;

import lombok.Data;

@Data
public class DocumentVo
{
   private Integer id;

   private Integer isFolder;

   private String title;

   private Integer fileId;

   private String size;

   private Integer status;              //状态（0-编制中，1-审批中，2-已发布[1,2,3]）

   private SimpleVo section;            //标段

   private String name;
   /**
    * 文件后缀名
    */
   private String suffix;

}
