package com.wisdom.leaf;

import com.wisdom.leaf.common.Result;

public interface IDGen {
    Result get(String key, int step);
    Result get(String key);
    boolean init();
}
