package com.keepgo.whatdo.entity;

import java.io.Serializable;
import java.util.Map;

public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwttoken;
    private final String name;
    private final Map<String,Object> claims;

    public JwtResponse(String jwttoken,Map<String,Object> claims,String name) {
        this.jwttoken = jwttoken;
        this.claims = claims;
        this.name = name;
    }

    public String getToken() {
        return this.jwttoken;
    }
    public Map<String,Object> getClaims() {
        return this.claims;
    }
    public String getName() {
        return this.name;
    }
}
