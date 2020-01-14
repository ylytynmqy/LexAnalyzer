package com.company;

import java.util.*;

public class DFA {
    int Number = -1;
    int tag = -1;
    Set<NFA> nfaSet;
    Map<Character, DFA> following = new HashMap<>();

    DFA(Set<NFA> nfaSet) {
        this.nfaSet = nfaSet;
    }

    DFA(int Number, Set<NFA> nfaSet) {
        this.Number = Number;
        this.nfaSet = nfaSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DFA state = (DFA) o;

        return Objects.equals(nfaSet, state.nfaSet);
    }

    static List<DFA> process(NFA nfa) {
        NumberProcessing numberProcessing = new NumberProcessing();
        List<DFA> dfaList = new ArrayList<>();
        // Add I_0
        dfaList.add(new DFA(numberProcessing.produceNew(), nfa.copy()));
        for (int i = 0; i < dfaList.size(); i++) {
            DFA dfaItem = dfaList.get(i);
            Map<Character, Set<NFA>> moves = new HashMap<>();
            for (NFA j : dfaItem.nfaSet) {
                for (Character c : j.following.keySet()) {
                    if (c != null) {
                        if (!moves.containsKey(c)) {
                            moves.put(c, new HashSet<>());
                        }
                        for (NFA nextNFA : j.following.get(c)) {
                            moves.get(c).addAll(nextNFA.copy());
                        }
                    }
                }
            }
            for (Map.Entry<Character, Set<NFA>> entry : moves.entrySet()) {
                DFA nextState = new DFA(entry.getValue());
                int index = dfaList.indexOf(nextState);
                if (index >= 0) {
                    nextState = dfaList.get(index);
                } else {
                    nextState.Number = numberProcessing.produceNew();
                    for (NFA nfaState : entry.getValue()) {
                        if (nfaState.tag >= 0 && nextState.tag < nfaState.tag) {
                            nextState.tag = nfaState.tag;
                        }
                    }
                    dfaList.add(nextState);
                }
                dfaItem.following.put(entry.getKey(), nextState);
            }
        }
        return dfaList;
    }
}
