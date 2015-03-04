package com.re.reverb.androidBackend.utils;

import android.view.View;

/**
 * Created by christopherhowse on 15-03-04.
 */
public interface MessageOverlay
{
    public View displayOverlay(int layoutResource, int containerId);

    public void removeOverlays();
}
