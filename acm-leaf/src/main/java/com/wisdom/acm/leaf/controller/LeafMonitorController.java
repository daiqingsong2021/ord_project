package com.wisdom.acm.leaf.controller;

import com.wisdom.acm.leaf.po.SegmentBufferView;
import com.wisdom.acm.leaf.service.impl.SegmentService;
import com.wisdom.leaf.segment.SegmentIDGenImpl;
import com.wisdom.leaf.segment.model.LeafAlloc;
import com.wisdom.leaf.segment.model.SegmentBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "leaf/monitor")
public class LeafMonitorController {
    private Logger logger = LoggerFactory.getLogger(LeafMonitorController.class);
    @Autowired
    SegmentService segmentService;

    @GetMapping(value = "cache")
    public List<SegmentBufferView> getCache() {
        List<SegmentBufferView> list =new ArrayList<>();
        SegmentIDGenImpl segmentIDGen = segmentService.getIdGen();
        if (segmentIDGen == null) {
            throw new IllegalArgumentException("You should config leaf.segment.enable=true first");
        }
        Map<String, SegmentBuffer> cache = segmentIDGen.getCache();
        for (Map.Entry<String, SegmentBuffer> entry : cache.entrySet()) {
            SegmentBufferView sv = new SegmentBufferView();
            SegmentBuffer buffer = entry.getValue();
            sv.setInitOk(buffer.isInitOk());
            sv.setKey(buffer.getKey());
            sv.setPos(buffer.getCurrentPos());
            sv.setNextReady(buffer.isNextReady());
            sv.setMax0(buffer.getSegments()[0].getMax());
            sv.setValue0(buffer.getSegments()[0].getValue().get());
            sv.setStep0(buffer.getSegments()[0].getStep());

            sv.setMax1(buffer.getSegments()[1].getMax());
            sv.setValue1(buffer.getSegments()[1].getValue().get());
            sv.setStep1(buffer.getSegments()[1].getStep());

            list.add(sv);
        }
        logger.info("Cache info {}", list);
        return list;
    }

    @GetMapping(value = "db")
    public List<LeafAlloc> getDb() {
        SegmentIDGenImpl segmentIDGen = segmentService.getIdGen();
        if (segmentIDGen == null) {
            throw new IllegalArgumentException("You should config leaf.segment.enable=true first");
        }
        List<LeafAlloc> items = segmentIDGen.getAllLeafAllocs();
        logger.info("DB info {}", items);
        return items;
    }

}
