package in.practix.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                R.layout.activity_profile

        );

        // ====================================
        // Bottom Navigation
        // ====================================

        BottomNavigationView bottomNavigationView =
                findViewById(
                        R.id.bottomNavigationView
                );


        // Profile is the current screen
        bottomNavigationView.setSelectedItemId(
                R.id.nav_profile
        );


        bottomNavigationView.setOnItemSelectedListener(
                item -> {

                    int id =
                            item.getItemId();


                    // ====================================
                    // HOME
                    // ====================================

                    if (id == R.id.nav_home) {

                        Intent intent =
                                new Intent(
                                        ProfileActivity.this,
                                        SecondActivity.class
                                );

                        intent.addFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_SINGLE_TOP
                        );

                        startActivity(intent);

                        finish();
                        overridePendingTransition(
                                R.anim.slide_in_left,
                                R.anim.silde_out_right
                        );

                        return true;
                    }


                    // ====================================
                    // QUICK TEST
                    // ====================================

                    else if (
                            id == R.id.nav_quick_test
                    ) {

                        Intent intent =
                                new Intent(
                                        ProfileActivity.this,
                                        QuickTestActivity.class
                                );

                        startActivity(intent);

                        finish();
                        overridePendingTransition(
                                R.anim.slide_in_left,
                                R.anim.silde_out_right
                        );

                        return true;
                    }


                    // ====================================
                    // PROFILE
                    // ====================================

                    else if (
                            id == R.id.nav_profile
                    ) {

                        // Already on Profile
                        return true;
                    }


                    return false;
                }
        );
    }
}
