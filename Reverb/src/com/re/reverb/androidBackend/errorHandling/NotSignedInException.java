package com.re.reverb.androidBackend.errorHandling;

/**
 * Created by Bill on 2014-09-12.
 */
public class NotSignedInException extends Throwable {

    public NotSignedInException(){
        super("You must be signed in to perform this action");
    }

    public NotSignedInException(String action){
        super(action+" failed! You must sign in first.");
    }
}
