package com.example.backofficer.model;

import java.io.Serializable;

public class MCP implements Serializable {
    String mcp;

    public MCP(){
        mcp = "";
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
}
