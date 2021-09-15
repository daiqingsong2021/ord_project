package com.wisdom.leaf.common;

import com.wisdom.leaf.IDGen;

public class ZeroIDGen implements IDGen {
    @Override
    public Result get(String key) {
        return new Result(0, Status.SUCCESS);
    }
    @Override
    public Result get(String key, int max) {
        return new Result(0, Status.SUCCESS);
    }
    @Override
    public boolean init() {
        return true;
    }
}
