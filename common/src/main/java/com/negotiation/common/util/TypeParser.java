package com.negotiation.common.util;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiOperation;

import java.util.Arrays;
import java.util.HashSet;

public class TypeParser extends StrUtil {

    @ApiOperation("将逗号隔开的String转换为HashSet")
    public static HashSet<Integer> stringToHash(String commaSplitString) {
        HashSet<Integer> hashSet = Arrays.stream(
                StrUtil.splitToInt(commaSplitString.strip(), StrUtil.COMMA))
                .collect(HashSet::new, HashSet::add, HashSet::addAll);
        return hashSet;
    }

    @ApiOperation("将HashSet转换为逗号隔开的String")
    public static String hashToString(HashSet<Integer> hashSet) {
        String commaSplitString = StrUtil.EMPTY;
        for (Integer i : hashSet) {
            commaSplitString = commaSplitString.concat(StrUtil.COMMA.concat(i.toString()));
        }
        // 去除第一个逗号
        commaSplitString = commaSplitString.substring(1);
        return commaSplitString;
    }
}
