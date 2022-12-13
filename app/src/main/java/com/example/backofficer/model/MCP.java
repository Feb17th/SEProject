package com.example.backofficer.model;

import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;

public class MCP implements Serializable {
    String mcp;
    GeoPoint geoPoint;
    String name;

    public MCP(){
        mcp = "";
        geoPoint = new GeoPoint(0, 0);
        name = "";
    }

    public MCP(String mcp) {
        this.mcp = mcp;
    }

    public String getMcp() {
        return mcp;
    }

    public void setMcp(String mcp) {
        this.mcp = mcp;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
