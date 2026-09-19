package in.practix.app;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class ContentTransition {

    public static void animate(
            Activity activity,
            View content,
            boolean enterFromRight
    ) {

        if (content == null) {
            return;
        }

        int animationRes;

        if (enterFromRight) {
            animationRes = R.anim.slide_in_right;
        } else {
            animationRes = R.anim.slide_in_left;
        }

        Animation animation =
                AnimationUtils.loadAnimation(
                        activity,
                        animationRes
                );

        content.startAnimation(animation);
    }
}