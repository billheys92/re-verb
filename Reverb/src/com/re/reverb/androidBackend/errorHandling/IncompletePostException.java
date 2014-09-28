package com.re.reverb.androidBackend.errorHandling;

import java.util.Collection;

/**
 * Created by Bill on 2014-09-12.
 */
public class IncompletePostException extends Throwable {

    private Collection<String> incompleteFields;

    public IncompletePostException(Collection<String> incompleteFields){
        this.incompleteFields = incompleteFields;
    }


}
