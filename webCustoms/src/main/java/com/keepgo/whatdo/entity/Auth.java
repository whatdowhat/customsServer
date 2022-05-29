package com.keepgo.whatdo.entity;

public enum Auth {

	SUPER("리더멘토"),
    ADMIN("학생"),
    USER("멘토");
//    SUPER("웹관리자");
    
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
