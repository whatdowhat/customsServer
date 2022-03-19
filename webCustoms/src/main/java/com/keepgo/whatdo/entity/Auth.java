package com.keepgo.whatdo.entity;

public enum Auth {

	LD("리더멘토"),
    ST("학생"),
    NLD("멘토"),
    GOV("기관"),
    SUPER("웹관리자");
    
    public final String label;

    private Auth(String label) {
        this.label = label;
    }
    
    public static Auth valueOfLabel(String label) {
        for (Auth e : values()) {
            if (e.label.equals(label)) {
                return e;
            }
        }
        return null;
    }
}
