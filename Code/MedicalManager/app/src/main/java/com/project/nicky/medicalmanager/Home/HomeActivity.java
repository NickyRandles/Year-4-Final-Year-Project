package com.project.nicky.medicalmanager.Home;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.project.nicky.medicalmanager.R;
import com.project.nicky.medicalmanager.Search.SearchActivity;
import com.project.nicky.medicalmanager.Startup.UserInformation;
import com.project.nicky.medicalmanager.Startup.WelcomeActivity;

public class HomeActivity extends AppCompatActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewPager = (ViewPager)findViewById(R.id.viewpager);
        PagerTabStrip pagerTabStrip = (PagerTabStrip)findViewById(R.id.pagerTabStrip);

        Fragment[] fragments = new Fragment[3];
        fragments[0] = new CalendarFragment();
        fragments[1] = new PlannerFragment();
        fragments[2] = new ContactsFragment();

        String[] titles = {"Calendar", "Planner", "Contacts"};

        MyPagerAdapter homePagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), titles, fragments);
        viewPager.setAdapter(homePagerAdapter);
        viewPager.setCurrentItem(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        UserInformation userInformation = new UserInformation(this);
        String userType = userInformation.getUserType();
        if(userType.equals("patient")){
            getMenuInflater().inflate(R.menu.main, menu);
        }
        else{
            getMenuInflater().inflate(R.menu.main2, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.search){
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.logout){
            UserInformation userInformation = new UserInformation(this);
            userInformation.logOut();
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
