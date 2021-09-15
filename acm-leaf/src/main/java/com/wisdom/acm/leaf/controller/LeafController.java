package com.wisdom.acm.leaf.controller;


import com.wisdom.acm.leaf.exception.LeafServerException;
import com.wisdom.acm.leaf.exception.NoKeyException;
import com.wisdom.acm.leaf.service.impl.SegmentService;
import com.wisdom.leaf.common.Result;
import com.wisdom.leaf.common.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "leaf")
public class LeafController {
    private Logger logger = LoggerFactory.getLogger(LeafController.class);
    @Autowired
    SegmentService segmentService;

    //@Autowired
    //SnowflakeService snowflakeService;

    @GetMapping(value = "/segment/get/{key}/{step}")
    public String getSegmentID(@PathVariable("key") String key, @PathVariable("step") int step) {
        return get(key, segmentService.getId(key, step));
    }

    @GetMapping(value = "/segment/get/{key}")
    public String getSegmentID(@PathVariable("key") String key) {
        return get(key, segmentService.getId(key));
    }

    private String get(@PathVariable("key") String key, Result id) {
        Result result;
        if (key == null || key.isEmpty()) {
            throw new NoKeyException();
        }

        result = id;
        if (result.getStatus().equals(Status.EXCEPTION)) {
            throw new LeafServerException(result.toString());
        }
        return String.valueOf(result.getId());
    }
}
