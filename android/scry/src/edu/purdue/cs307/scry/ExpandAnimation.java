package edu.purdue.cs307.scry;

import android.widget.RelativeLayout.LayoutParams;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

public class ExpandAnimation extends Animation{
	View mView;
	LayoutParams LP;
	LinearLayout LL;
	boolean visibility = false;
	int marginStart, marginEnd;
	boolean mWasEndedAlready = false;

	
	 public ExpandAnimation(View view, int duration) {

	        setDuration(duration);
	        mView = view;
	        LP = (LayoutParams) view.getLayoutParams();
	        LL = (LinearLayout) view;
	        
	        // decide to show or hide the view
	        visibility = (view.getVisibility() == View.VISIBLE);

	        marginStart = LP.bottomMargin;
	        marginEnd = (marginStart == 0 ? (0- view.getHeight()) : 0);

	        view.setVisibility(View.VISIBLE);
	    }
	
	
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        
        if (interpolatedTime < 1.0f) {
            // Calculating the new bottom margin, and setting it
            LP.bottomMargin = marginStart
                    + (int) ((marginEnd - marginStart) * interpolatedTime);
 
            // Invalidating the layout, making us seeing the changes we made
            mView.requestLayout();
        }  else if (!mWasEndedAlready) {


            if (visibility) {
                mView.setVisibility(View.GONE);
            }
            mWasEndedAlready = true;
            LP.bottomMargin = marginEnd;
            mView.requestLayout();
        }
    }
}

