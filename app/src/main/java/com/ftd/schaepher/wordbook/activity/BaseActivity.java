package com.ftd.schaepher.wordbook.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.ftd.schaepher.wordbook.R;

public class BaseActivity extends AppCompatActivity {
    protected DrawerLayout drawerLayout;
    protected ListView drawerList;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    protected FrameLayout frameLayout;
    protected boolean showOptionMenu = true;
    private Menu menu;
    protected final int ITEMS_QUERY = 0;
    protected final int ITEMS_BROWSE = 1;
    protected final int ITEMS_ABOUT = 2;
    protected final int ITEMS_SETTING = 3;
    protected final int ITEMS_EXIT = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        frameLayout = (FrameLayout) drawerLayout.findViewById(R.id.content_frame);
        getLayoutInflater().inflate(layoutResID, frameLayout, true);
        super.setContentView(drawerLayout);

        setUpToolBar();
        setUpNavigation();
    }

    private void setUpToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void setUpNavigation() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        String[] planetTitles = getResources().getStringArray(R.array.planets_array);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        drawerList.setAdapter(new ArrayAdapter<>(BaseActivity.this,
                R.layout.list_item_drawer, planetTitles));

        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        drawerToggle = new ActionBarDrawerToggle(BaseActivity.this, drawerLayout,
                toolbar, R.string.app_name, R.string.app_name);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (showOptionMenu) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            this.menu = menu;
        }
        super.onCreateOptionsMenu(menu);
        return true;
    }

    protected void hideOptionMenu() {
        showOptionMenu = false;
    }

    protected void hideOption(int id) {
        menu.findItem(id).setVisible(false);
    }

    protected void showOption(int id) {
        menu.findItem(id).setVisible(true);
    }

    /**
     * this method should be override if you use tool bar in sub-activity.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    protected void selectItem(int position) {
        switch (position) {
            case ITEMS_QUERY:
                break;
            case ITEMS_BROWSE:
                Intent intent = new Intent(getApplicationContext(), WorldsBrowsingActivity.class);
                startActivity(intent);
                break;
            case ITEMS_ABOUT:
                break;
            case ITEMS_SETTING:
                break;
            case ITEMS_EXIT:
                break;
            default:
                break;
        }
    }

}
