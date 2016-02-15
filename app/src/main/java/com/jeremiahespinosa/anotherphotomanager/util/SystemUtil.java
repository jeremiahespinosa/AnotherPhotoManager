package com.jeremiahespinosa.anotherphotomanager.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.jeremiahespinosa.anotherphotomanager.R;

/**
 * Created by jespinosa on 2/14/16.
 */
public class SystemUtil {

    public static void startActivity(Context context, Intent intent, boolean shouldFinishPrevActivity) {
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.animator.push_left_in, R.animator.push_left_out);
        if (shouldFinishPrevActivity)
            ((Activity) context).finish();
    }

}
