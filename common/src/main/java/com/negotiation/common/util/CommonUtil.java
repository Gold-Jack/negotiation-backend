package com.negotiation.common.util;

import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.baidubce.model.ApiExplorerResponse;
import io.swagger.annotations.ApiOperation;

import java.util.*;

public class CommonUtil {

    @ApiOperation("将逗号隔开的String转换为HashSet<Integer>")
    public static HashSet<Integer> stringToHash(String commaSplitString) {
        commaSplitString = commaSplitString.replaceAll("，", StrUtil.COMMA); // 中文逗号转英文逗号
        HashSet<Integer> hashSet = Arrays.stream(
                StrUtil.splitToInt(commaSplitString.strip(), StrUtil.COMMA))
                .collect(HashSet::new, HashSet::add, HashSet::addAll);
        return hashSet;
    }

    @ApiOperation("将HashSet转换为逗号隔开的String<Integer>")
    public static String hashToString(HashSet<Integer> hashSet) {
        String commaSplitString = StrUtil.EMPTY;
        for (Integer i : hashSet) {
            commaSplitString = commaSplitString.concat(StrUtil.COMMA.concat(i.toString()));
        }
        // 去除第一个逗号
        commaSplitString = commaSplitString.substring(1);
        return commaSplitString;
    }


    @ApiOperation("百度api返回Json转Map")
    public static Map<String, String> baiduJsonToMap(ApiExplorerResponse response) {
        Map<String, String> map = new HashMap<>();
        String responseStr = response.getResult();
        responseStr = responseStr.substring(1, responseStr.length()-1);
        List<String> responseDic = StrUtil.split(responseStr, StrUtil.COMMA);
        responseDic.forEach((pair) -> {
            String prettyPair = pair.strip();
            prettyPair = removeChar(prettyPair, CharUtil.DOUBLE_QUOTES);
            List<String> keyAndValue = StrUtil.split(prettyPair, StrUtil.COLON);
            if (keyAndValue.size() == 2) {
//                keyAndValue.replaceAll(string -> removeChar(string, CharUtil.DOUBLE_QUOTES));
                map.put(keyAndValue.get(0), keyAndValue.get(1));
            } else {
//                int lastIndex = keyAndValue.size() - 1;
//                map.put(keyAndValue.get(lastIndex-1), keyAndValue.get(lastIndex));
                System.out.println("*有歧义的键值对：" + keyAndValue);
            }
        });

        return map;
    }

    @ApiOperation("截取最大长度，符合条件的subString，头尾都不包含")
    public static String subString(String string, Character startChar, Character endChar) {
        int startCnt = string.length();
        int endCnt = -1;
        char[] chars = string.toCharArray();
        boolean haveStartCharPos = false;
        for(int i=0; i< chars.length; i++) {
            if (chars[i] == startChar && !haveStartCharPos) {
                startCnt = i;
                haveStartCharPos = true;
            }
            if (chars[i] == endChar) {
                endCnt = i;
            }
        }
        if (startChar >= endChar) {
            return StrUtil.EMPTY;
        }

        return string.substring(startCnt+1, endCnt);
    }

    @ApiOperation("移除String中特定的字符")
    public static String removeChar(String string, Character tgtChar) {
        char[] chars = string.toCharArray();
        String resString = StrUtil.EMPTY;
        for (Character c : chars) {
            if (c != tgtChar) {
                resString = resString.concat(c.toString());
            }
        }
        return resString;
    }

    @ApiOperation("移除String中所有特定字符")
    public static String removeAllChar(String string, Collection<Character> tgtChars) {
        for (Character tgtChar : tgtChars) {
            string = CommonUtil.removeChar(string, tgtChar);
        }
        return string;
    }

}
