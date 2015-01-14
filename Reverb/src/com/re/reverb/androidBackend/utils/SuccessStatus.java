package com.re.reverb.androidBackend.utils;

/**
 * Created by Bill on 2014-11-15.
 */
public class SuccessStatus {

    private String reason;
    private boolean success;

    public String reason() {
        return reason;
    }

    public boolean success() {
        return success;
    }

    public SuccessStatus(boolean success, String reason) {
        this.success = success;
        this.reason = reason;
    }

    public SuccessStatus(boolean success) {
        this.success = success;
        this.reason = "Operation successful";
    }

    public void throwErrorIfFailed(){
        if(!success()){
            throw new RuntimeException(reason);
        }
    }

}
