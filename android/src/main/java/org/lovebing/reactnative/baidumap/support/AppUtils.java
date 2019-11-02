/**
 * Copyright (c) 2016-present, lovebing.org.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package org.lovebing.reactnative.baidumap.support;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import org.lovebing.reactnative.baidumap.constant.RequestCode;

/**
 * @author lovebing
 * @date 2019/10/31
 */
public class AppUtils {

    public static void checkPermission(Activity activity, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(activity, permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{permission}, RequestCode.CODE_ASK_PERMISSIONS);
                }
            }
        }
    }
}
