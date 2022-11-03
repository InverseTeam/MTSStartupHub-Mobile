package ramble.sokol.msh.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import ramble.sokol.msh.R;
import ramble.sokol.msh.fragments.BonusFragment;
import ramble.sokol.msh.fragments.EventsFragment;
import ramble.sokol.msh.fragments.MessageFragment;
import ramble.sokol.msh.fragments.ProfileFragment;
import ramble.sokol.msh.fragments.StartupFragment;

public class MainMenuActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private BonusFragment bonusFragment;
    private EventsFragment eventsFragment;
    private StartupFragment startupFragment;
    private ProfileFragment profileFragment;
    private MessageFragment messageFragment;
    private long backPressedTime;
    private Toast backToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        init();
    }

    private void init(){
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bonusFragment = new BonusFragment();
        eventsFragment = new EventsFragment();
        startupFragment = new StartupFragment();
        profileFragment = new ProfileFragment();
        messageFragment = new MessageFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_menu, bonusFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_bonus:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_menu, bonusFragment).commit();
                        return true;
                    case R.id.menu_message:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_menu, messageFragment).commit();
                        return true;
                    case R.id.menu_startup:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_menu, startupFragment).commit();
                        return true;
                    case R.id.menu_events:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_menu, eventsFragment).commit();
                        return true;
                    case R.id.menu_profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_menu, profileFragment).commit();
                        return true;

                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            return;
        }else{
            backToast = Toast.makeText(getBaseContext(), "Нажмите ещё раз, чтобы выйти", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}