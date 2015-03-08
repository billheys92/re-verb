package com.re.reverb.androidBackend.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class GenericOverLay implements MessageOverlay
{
    View currentOverlay;
    Activity activity;

    public GenericOverLay(Activity activity)
    {
        this.activity = activity;
    }

    public View displayOverlay(int layoutResource, int containerId)
    {
        removeOverlays();
        LayoutInflater vi = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(layoutResource, null);
        this.currentOverlay = v;

        FrameLayout myLayout = (FrameLayout) activity.findViewById(containerId);
        myLayout.addView(v);
        return v;

    }

    public void removeOverlays()
    {
        if(this.currentOverlay != null)
        {
            ViewGroup parent = (ViewGroup) this.currentOverlay.getParent();
            parent.removeView(this.currentOverlay);
            this.currentOverlay = null;
        }
    }
}
