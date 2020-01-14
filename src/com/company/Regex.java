package com.company;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class Regex {
    static List<RegNode> parse(String re, Map<String, List<RegNode>> reMap) throws IOException {
        List<RegNode> result = new ArrayList<>();
        result.add(new RegNode(RegNode.Type.AUXILIARY, '('));
        boolean escape = false;
        for (int i = 0; i < re.length(); i++) {
            char c = re.charAt(i);
            if (escape) {
                escape = false;//to //
                switch (c) {
                    case 'n':
                        result.add(new RegNode(RegNode.Type.CONTENT, '\n'));
                        break;
                    case 'r':
                        result.add(new RegNode(RegNode.Type.CONTENT, '\r'));
                        break;
                    case 't':
                        result.add(new RegNode(RegNode.Type.CONTENT, '\t'));
                        break;
                    default:
                        result.add(new RegNode(RegNode.Type.CONTENT, c));
                }
                continue;
            }
            switch (c) {
                case '|':
                case '*':
                case '?':
                case '(':
                case ')':
                    result.add(new RegNode(RegNode.Type.AUXILIARY, c));
                    break;
                case '[':
                    result.add(new RegNode(RegNode.Type.AUXILIARY, '('));
                    i++;
                    while (re.charAt(i) != ']') {
                        char start = re.charAt(i);
                        i++;
                        if (re.charAt(i) != '-') {
                            throw new IOException("wrong Regex Parse item" + i);
                        }
                        i++;
                        char end = re.charAt(i);
                        for (char j = start; j <= end; j++) {
                            result.add(new RegNode(RegNode.Type.CONTENT, j));
                            result.add(new RegNode(RegNode.Type.AUXILIARY, '|'));
                        }
                        i++;
                    }
                    result.remove(result.size() - 1);
                    result.add(new RegNode(RegNode.Type.AUXILIARY, ')'));
                    break;
                case '{':
                    int index = re.indexOf('}', i);
                    if (index < 0 || re.charAt(index - 1) == '\\') {
                        throw new IOException("wrong Regex Parse item" + i);
                    }
                    String refName = re.substring(i + 1, index);
                    if (reMap != null && reMap.containsKey(refName)) {
                        result.addAll(reMap.get(refName));
                        i = index;
                    } else {
                        throw new  IOException("wrong Regex Parse item" + i);
                    }
                    break;
                case '+':
                    RegNode last = result.get(result.size() - 1);
                    if (last.type == RegNode.Type.CONTENT) {
                        result.add(last);
                        result.add(new RegNode(RegNode.Type.AUXILIARY, '*'));
                    } else if (last.value == ')') {
                        int temp = 1;
                        for (int j = result.size() - 2; j >= 0; j--) {
                            if (result.get(j).type == RegNode.Type.AUXILIARY) {
                                if (result.get(j).value == ')') {
                                    temp++;
                                } else if (result.get(j).value == '(') {
                                    temp--;
                                }
                                if (temp == 0) {
                                    result.addAll(result.subList(j, result.size()));
                                    result.add(new RegNode(RegNode.Type.AUXILIARY, '*'));
                                    break;
                                }
                            }
                        }
                    } else {
                        throw new  IOException("wrong Regex Parse item" + i);
                    }
                    break;
                case '\\'://escape char
                    escape = true;
                    break;
                case '.':
                    result.add(new RegNode(RegNode.Type.CONTENT, (char) 0));
                    break;
                default:
                    //real char none regex
                    result.add(new RegNode(RegNode.Type.CONTENT, c));
            }
        }
        result.add(new RegNode(RegNode.Type.AUXILIARY, ')'));
        return result;
    }

}
