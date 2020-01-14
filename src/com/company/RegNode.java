package com.company;

class RegNode {
    Type type;

    char value;

    RegNode(Type type, char value) {
        this.type = type;
        this.value = value;
    }

    public enum Type {
        CONTENT, AUXILIARY
    }
}
