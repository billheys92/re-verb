package com.re.reverb.androidBackend;

public abstract class Region
{
    boolean readPermission = true;
    boolean writePermission = false;

    int regionId;

    public int getRegionId(){
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }



}
