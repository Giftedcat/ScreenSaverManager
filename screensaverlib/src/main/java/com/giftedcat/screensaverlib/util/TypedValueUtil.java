package com.giftedcat.screensaverlib.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * dp,px等相互转换
 */
public class TypedValueUtil {

    /**
     * dp转px
     *
     * @param context
     * @param dp
     * @return px的值
     */
    public static float dp2px(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     * @param context
     * @param sp
     * @return
     */
    public static float sp2px(Context context, float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                sp, context.getResources().getDisplayMetrics());
    }
}
