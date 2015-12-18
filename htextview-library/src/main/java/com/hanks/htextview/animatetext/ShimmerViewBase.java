package com.hanks.htextview.animatetext;
import com.hanks.htextview.util.HTextViewHelper;
/**
 * Shimmer
 * User: romainpiel
 * Date: 10/03/2014
 * Time: 17:33
 */
public interface ShimmerViewBase {

    public float getGradientX();
    public void setGradientX(float gradientX);
    public boolean isShimmering();
    public void setShimmering(boolean isShimmering);
    public boolean isSetUp();
    public void setAnimationSetupCallback(HTextViewHelper.AnimationSetupCallback callback);
    public int getPrimaryColor();
    public void setPrimaryColor(int primaryColor);
    public int getReflectionColor();
    public void setReflectionColor(int reflectionColor);
}
