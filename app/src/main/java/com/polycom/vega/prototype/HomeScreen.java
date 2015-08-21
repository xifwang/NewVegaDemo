package com.polycom.vega.prototype;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.polycom.vega.fundamental.IActivity;
import com.polycom.vega.fundamental.IDataBind;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity implements IActivity, IDataBind, AdapterView.OnItemClickListener {
    private DrawerLayout drawerLayout;
    private ListView navigationListView;
    private ArrayList<String> menuList;
    private ArrayAdapter<String> navigationListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        Bundle bundle = new Bundle();
        String response = getIntent().getStringExtra("response");
        bundle.putString("response", response);
        HomeScreenFragment homeScreenFragment = new HomeScreenFragment();
        homeScreenFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, homeScreenFragment).commit();

        this.initComponent();
        this.initComponentState();
        this.registerNotification();
        this.dataBind();
    }

    @Override
    public void initComponent() {
        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        this.menuList = new ArrayList<String>();
        this.menuList.add(this.getString(R.string.navigationmenu_home));
        this.menuList.add(this.getString(R.string.navigationmenu_placeACall));
        this.menuList.add(this.getString(R.string.navigationmenu_showContent));
        this.menuList.add(this.getString(R.string.navigationmenu_settings));
        this.menuList.add(this.getString(R.string.navigationmenu_help));

        this.navigationListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, this.menuList);

        this.navigationListView = (ListView) findViewById(R.id.navigationListView);
        this.navigationListView.setAdapter(this.navigationListAdapter);
        this.navigationListView.setOnItemClickListener(this);
    }

    @Override
    public void initComponentState() {

    }

    @Override
    public void initAnimation() {
        ScaleAnimation animation = new ScaleAnimation(0, 1, 0, 1);
        animation.setDuration(500);
        LayoutAnimationController animationController = new LayoutAnimationController(animation, 0.3f);

        this.navigationListView.setLayoutAnimation(animationController);
    }

    @Override
    public void registerNotification() {

    }

    @Override
    public void dataBind() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 3) {
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_fragment, new SettingsFragment()).commit();
        }

        this.drawerLayout.closeDrawer(this.navigationListView);
    }
}
