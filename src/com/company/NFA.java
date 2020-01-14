package com.company;

import java.util.*;

class NFA {
    int Number;
    int tag = -1;
    MyMap<Character, NFA> following;

    NFA(int Number) {
        this.Number = Number;
        following = new MyMap<>();
    }

    Set<NFA> copy() {
        Set<NFA> newOne = new HashSet<>();
        copyDFS(newOne);
        return newOne;
    }

    private void copyDFS(Set<NFA> root) {
        if (root.contains(this)) {
            return;
        }
        root.add(this);
        for (NFA item : following.get(null)) {
            item.copyDFS(root);
        }
    }

    static NFA process(int tag, List<RegNode> regNodes, NumberProcessing numberProcessing) {
        // Init
        int idOffset = numberProcessing.get();
        Stack<Character> opStack = new Stack<>();
        Stack<NFA> stateStack = new Stack<>();
        // Construct root
        List<NFA> list = new ArrayList<>();
        NFA root = new NFA(numberProcessing.produceNew());
        list.add(root);
        // Parse Regex
        NFA lastBlockFrom = null;
        NFA blockStart = root;
        NFA pointer = root;
        for (RegNode node : regNodes) {
            if (node.type == RegNode.Type.AUXILIARY) {
                switch (node.value) {
                    case '(':
                        opStack.push(node.value);
                        stateStack.push(blockStart);
                        blockStart = pointer;
                        break;
                    case ')':
                        while (true) {
                            char op = opStack.pop();
                            if (op == '(') {
                                lastBlockFrom = blockStart;
                                blockStart = stateStack.pop();
                                break;
                            } else if (op == '|') {
                                stateStack.pop().following.add(null, pointer);
                            }
                        }
                        break;
                    case '|':
                        opStack.push(node.value);
                        stateStack.push(pointer);
                        pointer = blockStart;
                        break;
                    case '?':
                        assert lastBlockFrom != null;
                        lastBlockFrom.following.add(null, pointer);
                        break;
                    case '*':
                        NFA copyState = new NFA(numberProcessing.produceNew());
                        list.add(copyState);
                        assert lastBlockFrom != null;
                        for (int j = idOffset; j < lastBlockFrom.Number; j++) {
                            for (Character ch : list.get(j - idOffset).following.keySet()) {
                                if (list.get(j - idOffset).following.get(ch).contains(lastBlockFrom)) {
                                    list.get(j - idOffset).following.delete(ch, lastBlockFrom);
                                    list.get(j - idOffset).following.add(ch, copyState);
                                }
                            }
                        }
                        if (blockStart == lastBlockFrom) {
                            blockStart = copyState;
                        }
                        if (root == lastBlockFrom) {
                            root = copyState;
                        }
                        copyState.following.add(null, lastBlockFrom);
                        pointer.following.add(null, copyState);

                        // Add a null state
                        NFA nullState = new NFA(numberProcessing.produceNew());
                        list.add(nullState);
                        copyState.following.add(null, nullState);
                        pointer = nullState;
                        break;
                }
            } else {
                NFA newState = new NFA(numberProcessing.produceNew());
                list.add(newState);
                pointer.following.add(node.value, newState);
                lastBlockFrom = pointer;
                pointer = newState;
            }
        }
        pointer.tag = tag;
        return root;
    }

    static NFA merge(List<NFA> nfaList, NumberProcessing numberProcessing) {
        if (nfaList.size() == 1) {
            return nfaList.get(0);
        }
        NFA merged = new NFA(numberProcessing.produceNew());
        for (NFA nfa : nfaList) {
            merged.following.add(null, nfa);
        }
        return merged;
    }
}
