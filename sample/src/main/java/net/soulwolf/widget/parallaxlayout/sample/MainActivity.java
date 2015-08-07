package net.soulwolf.widget.parallaxlayout.sample;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import net.soulwolf.widget.parallaxlayout.OnScalingListener;
import net.soulwolf.widget.parallaxlayout.ParallaxLayout;
import net.soulwolf.widget.parallaxlayout.ParallaxLayoutPresenter;


public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, OnScalingListener {

    ParallaxLayout mParallaxLayout;

    ViewPager  mViewPager;

    SimpleParallaxDelegate mSimpleParallaxDelegate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mParallaxLayout = (ParallaxLayout) findViewById(R.id.parallax_layout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(4);

        mSimpleParallaxDelegate = new SimpleParallaxDelegate(this);
        mParallaxLayout.setParallaxDelegate(mSimpleParallaxDelegate);
        mViewPager.setAdapter(new SimpleFragmentAdapter(getSupportFragmentManager()));
        mSimpleParallaxDelegate.setViewPager(mViewPager);
        mSimpleParallaxDelegate.addOnPageChangeListener(this);

        mParallaxLayout.setOnScalingListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mParallaxLayout.onScrollableSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDestroy() {
        ParallaxLayoutPresenter.onDetach(this);
        super.onDestroy();
    }

    @Override
    public void onScaling() {
        Toast.makeText(this,"onScaling()",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onScaled() {
        Toast.makeText(this,"onScaled()",Toast.LENGTH_SHORT).show();
    }
}
