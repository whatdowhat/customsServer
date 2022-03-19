package com.keepgo.whatdo.entity;

/*
 * 
 * 수량 그룹코드
 * MASTER : UNIT
 * 
 * 
 * */
public enum BaseCommon {

	PCS("PCS"),
    M("M"),
    YD("YD"),
    SET("SET");
    
    public final String label;

    private BaseCommon(String label) {
        this.label = label;
    }
    
    public static BaseCommon valueOfLabel(String label) {
        for (BaseCommon e : values()) {
            if (e.label.equals(label)) {
                return e;
            }
        }
        return null;
    }
}
