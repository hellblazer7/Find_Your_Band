package com.example.findyourband;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    TextView fullname;
    DatabaseReference reference;
    FirebaseUser firebaseUser;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main_activity);
        toolbar.inflateMenu(R.menu.options_menu);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getItemId()==R.id.log_out)
                {
                    mAuth=FirebaseAuth.getInstance();
                    mAuth.signOut();
                    Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
                    MainActivity.this.startActivity(myIntent);
                    finish();
                    return true;
                }
                else if(item.getItemId()== R.id.search_for_users)
                {
                    Intent myIntent = new Intent(MainActivity.this, SearchActivity.class);
                    MainActivity.this.startActivity(myIntent);
                    return true;
                }
                return false;
            }
        });
        fullname=findViewById(R.id.fullname_textview);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager=findViewById(R.id.view_Pager);
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new ChatsFragment(),"Chats");
        viewPagerAdapter.addFragment(new ExistingUsersFragment(),"Registered Users");
        viewPagerAdapter.addFragment(new ProfileFragment(),"Profile");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            HelperClass user=snapshot.getValue(HelperClass.class);            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    class ViewPagerAdapter extends FragmentPagerAdapter{
        ArrayList<Fragment> fragments;//passing the fragments
        ArrayList<String>fragmenttitles;//passing the fragment titles
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments=new ArrayList<>();
            this.fragmenttitles=new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
        public void addFragment(Fragment fragment,String title){
            fragments.add(fragment);
            fragmenttitles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmenttitles.get(position);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}