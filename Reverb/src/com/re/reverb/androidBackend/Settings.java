package com.re.reverb.androidBackend;

/**
 * Created by Bill on 2014-09-27.
 */
public class Settings {

    /*
        I think this class should definitely be changed to something more
        complicated/useful. At some point we need to store the settings with
        the android Preference API
     */

    private static Settings ourInstance = new Settings();

    private String mapType = "satellite";

    public String getMapType() {
        return mapType;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
    }

    public static Settings getInstance() {
        return ourInstance;
    }

    private Settings() {
    }
}
