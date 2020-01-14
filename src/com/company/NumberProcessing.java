package com.company;

class NumberProcessing {
    private int No = 0;

    int produceNew() {
        No++;
        return No;
    }

    int get() {
        return No;
    }
}
