package in.practix.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class QuickTestActivity extends AppCompatActivity {

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
                R.layout.activity_quick_test
        );

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
}
