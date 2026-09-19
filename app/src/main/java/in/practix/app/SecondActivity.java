package in.practix.app;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SecondActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private SecondActivityViewModel viewModel;

    private int currentNavId = R.id.nav_home;


    // =====================================================
    // ON CREATE
    // =====================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);


        // ====================================
        // VIEWMODEL
        // ====================================

        viewModel =
                new ViewModelProvider(this)
                        .get(SecondActivityViewModel.class);


        // ====================================
        // STATUS BAR
        // ====================================

        updateStatusBar();


        // ====================================
        // CHECK SPLASH REQUIREMENT
        // ====================================

        boolean skipSplash =
                getIntent().getBooleanExtra(
                        "SKIP_SPLASH",
                        false
                );


        boolean firstLaunch =
                savedInstanceState == null
                        && !skipSplash;


        // ====================================
        // FIRST APP LAUNCH
        // → SHOW BRANDING SPLASH
        // ====================================

        if (firstLaunch) {

            setContentView(
                    R.layout.layout_splash_branding
            );


            new Handler(
                    Looper.getMainLooper()
            ).postDelayed(
                    () -> {

                        if (!isFinishing()
                                && !isDestroyed()) {

                            initializeMainScreen();
                        }

                    },
                    4000
            );


        } else {

            // ====================================
            // RECREATION / RETURN
            // → NO BRANDING SPLASH
            // ====================================

            initializeMainScreen();
        }
    }


    // =====================================================
    // STATUS BAR
    // =====================================================

    private void updateStatusBar() {

        getWindow().setStatusBarColor(
                ContextCompat.getColor(
                        this,
                        R.color.status_bar
                )
        );


        boolean isDarkMode =
                (getResources()
                        .getConfiguration()
                        .uiMode
                        & Configuration.UI_MODE_NIGHT_MASK)
                        == Configuration.UI_MODE_NIGHT_YES;


        if (isDarkMode) {

            getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(0);

        } else {

            getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    );
        }
    }


    // =====================================================
    // INITIALIZE HOST SCREEN
    // =====================================================

    private void initializeMainScreen() {

        setContentView(
                R.layout.activity_second
        );


        updateStatusBar();


        // ====================================
        // BOTTOM NAVIGATION
        // ====================================

        bottomNavigationView =
                findViewById(
                        R.id.bottomNavigationView
                );


        // ====================================
        // CHECK EXISTING FRAGMENT
        // ====================================

        Fragment currentFragment =
                getSupportFragmentManager()
                        .findFragmentById(
                                R.id.fragmentContainer
                        );


        if (currentFragment == null) {

            currentNavId =
                    R.id.nav_home;


            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            R.id.fragmentContainer,
                            new HomeFragment()
                    )
                    .commit();

        } else {

            // ====================================
            // RESTORED FRAGMENT
            // ====================================

            if (currentFragment
                    instanceof PrepCenterFragment) {

                currentNavId =
                        R.id.nav_quick_test;

            } else if (currentFragment
                    instanceof ProfileFragment) {

                currentNavId =
                        R.id.nav_profile;

            } else {

                currentNavId =
                        R.id.nav_home;
            }
        }


        // ====================================
        // SELECT CURRENT TAB
        // ====================================

        bottomNavigationView.setSelectedItemId(
                currentNavId
        );


        // ====================================
        // BOTTOM NAVIGATION LISTENER
        // ====================================

        bottomNavigationView.setOnItemSelectedListener(
                item -> {

                    int id =
                            item.getItemId();


                    if (id == currentNavId) {
                        return true;
                    }


                    showFragment(id);


                    return true;
                }
        );
    }


    // =====================================================
    // SHOW FRAGMENT
    // =====================================================

    private void showFragment(int id) {

        Fragment fragment;


        if (id == R.id.nav_home) {

            fragment =
                    new HomeFragment();

        } else if (id == R.id.nav_quick_test) {

            fragment =
                    new PrepCenterFragment();

        } else if (id == R.id.nav_profile) {

            fragment =
                    new ProfileFragment();

        } else {

            return;
        }


        int enterAnimation;
        int exitAnimation;


// =====================================================
// HOME → PREP CENTER
// =====================================================

        if (currentNavId == R.id.nav_home
                && id == R.id.nav_quick_test) {

            enterAnimation =
                    R.anim.slide_in_left;

            exitAnimation =
                    R.anim.silde_out_right;


// =====================================================
// PREP CENTER → HOME
// =====================================================

        } else if (currentNavId == R.id.nav_quick_test
                && id == R.id.nav_home) {

            enterAnimation =
                    R.anim.slide_in_right;

            exitAnimation =
                    R.anim.slide_out_left;


// =====================================================
// HOME → PROFILE
// =====================================================

        } else if (currentNavId == R.id.nav_home
                && id == R.id.nav_profile) {

            enterAnimation =
                    R.anim.slide_in_right;

            exitAnimation =
                    R.anim.slide_out_left;


// =====================================================
// PROFILE → HOME
// =====================================================

        } else if (currentNavId == R.id.nav_profile
                && id == R.id.nav_home) {

            enterAnimation =
                    R.anim.slide_in_left;

            exitAnimation =
                    R.anim.silde_out_right;


// =====================================================
// PREP CENTER → PROFILE
// =====================================================

        } else if (currentNavId == R.id.nav_quick_test
                && id == R.id.nav_profile) {

            enterAnimation =
                    R.anim.slide_in_right;

            exitAnimation =
                    R.anim.slide_out_left;


// =====================================================
// PROFILE → PREP CENTER
// =====================================================

        } else if (currentNavId == R.id.nav_profile
                && id == R.id.nav_quick_test) {

            enterAnimation =
                    R.anim.slide_in_left;

            exitAnimation =
                    R.anim.silde_out_right;


        } else {

            return;
        }

        // ====================================
        // FRAGMENT TRANSACTION
        // ====================================

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        enterAnimation,
                        exitAnimation
                )
                .setReorderingAllowed(true)
                .replace(
                        R.id.fragmentContainer,
                        fragment
                )
                .commit();


        currentNavId =
                id;
    }


    // =====================================================
    // HANDLE NEW INTENT
    // =====================================================

    @Override
    protected void onNewIntent(
            Intent intent
    ) {

        super.onNewIntent(intent);


        setIntent(intent);


        if (bottomNavigationView != null) {

            bottomNavigationView.setSelectedItemId(
                    R.id.nav_home
            );
        }
    }
}