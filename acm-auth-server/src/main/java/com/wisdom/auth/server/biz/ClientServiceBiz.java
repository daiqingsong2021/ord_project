package com.wisdom.auth.server.biz;

import com.wisdom.auth.server.entity.ClientService;
import com.wisdom.auth.server.mapper.ClientServiceMapper;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class ClientServiceBiz {

    @Autowired
    protected ClientServiceMapper mapper;

    public int insertSelective(ClientService entity) {
        EntityUtils.setCreateInfo(entity);
        int count = mapper.insertSelective(entity);
        return count > 0 ? count : null;
    }


}
