package in.practix.app;

import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class QuickTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        }

        super.onCreate(savedInstanceState);

        // ====================================
        // Status Bar
        // ====================================

        getWindow().setStatusBarColor(
                ContextCompat.getColor(
                        this,
                        R.color.status_bar
                )
        );


        boolean isDarkMode =
                AppCompatDelegate.getDefaultNightMode()
                        == AppCompatDelegate.MODE_NIGHT_YES;


        if (isDarkMode) {

            // Dark mode → WHITE status bar icons

            getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(0);

        } else {

            // Light mode → DARK status bar icons

            getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    );
        }


        setContentView(
                R.layout.activity_quick_test
        );

        // Home <-> Prep Center transition is used only when
        // this Activity was opened from Home.
        if (getIntent().getBooleanExtra("FROM_HOME", false)) {
            configureHomePrepTransitions();
        }


        BottomNavigationView bottomNavigationView =
                findViewById(
                        R.id.bottomNavigationView
                );


        // Quick Test is the current screen
        bottomNavigationView.setSelectedItemId(
                R.id.nav_quick_test
        );


        bottomNavigationView.setOnItemSelectedListener(
                item -> {

                    int id =
                            item.getItemId();


                    if (id == R.id.nav_home) {

                        // Home -> Prep Center -> Home is a real
                        // Activity return transition. This lets the
                        // Prep Center content exit while Home content
                        // enters at the same time. Bottom navigation
                        // is not a transition target.
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                                && getIntent().getBooleanExtra("FROM_HOME", false)) {

                            finishAfterTransition();
                            return true;
                        }

                        // Preserve the existing behavior if QuickTest
                        // was opened from another Activity.
                        Intent intent =
                                new Intent(
                                        QuickTestActivity.this,
                                        SecondActivity.class
                                );

                        intent.addFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_SINGLE_TOP
                        );

                        startActivity(intent);

                        finish();

                        overridePendingTransition(
                                R.anim.slide_in_right,
                                R.anim.slide_out_left
                        );

                        return true;


                    } else if (
                            id == R.id.nav_quick_test
                    ) {

                        // Already on Quick Test
                        return true;


                    } else if (
                            id == R.id.nav_profile
                    ) {

                        Intent intent =
                                new Intent(
                                        QuickTestActivity.this,
                                        ProfileActivity.class
                                );

                        startActivity(intent);

                        finish();
                        overridePendingTransition(
                                R.anim.slide_in_right,
                                R.anim.slide_out_left
                        );

                        return true;
                    }


                    return false;
                }
        );


    }


    // =====================================================
    // HOME <-> PREP CENTER TRANSITIONS
    // =====================================================

    private void configureHomePrepTransitions() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        ViewGroup quickTestContent =
                findViewById(R.id.quickTestScrollView);

        if (quickTestContent != null) {
            quickTestContent.setTransitionGroup(true);
        }


        // Prep Center enters from the left.
        Slide enter = new Slide(Gravity.LEFT);
        enter.setDuration(350);
        enter.setInterpolator(
                new DecelerateInterpolator()
        );

        enter.addTarget(R.id.quickTestScrollView);
        enter.addTarget(R.id.btnSelectedPractice);

        // Prep Center exits to the right when returning to Home.
        Slide returnTransition = new Slide(Gravity.RIGHT);
        returnTransition.setDuration(350);
        returnTransition.setInterpolator(
                new AccelerateInterpolator()
        );
        returnTransition.addTarget(R.id.quickTestScrollView);
        returnTransition.addTarget(R.id.btnSelectedPractice);

        getWindow().setEnterTransition(enter);
        getWindow().setReturnTransition(returnTransition);

        // Enter and return transitions should overlap with the
        // corresponding transition in the other Activity.
        getWindow().setAllowEnterTransitionOverlap(true);
        getWindow().setAllowReturnTransitionOverlap(true);
    }
}
