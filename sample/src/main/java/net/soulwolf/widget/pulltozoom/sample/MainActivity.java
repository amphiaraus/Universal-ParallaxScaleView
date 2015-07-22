package net.soulwolf.widget.pulltozoom.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    ImageView mPictureView;

    ListView  mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mPictureView = (ImageView) findViewById(R.id.pic);
        mListView = (ListView) findViewById(R.id.listview);
        initialize();
    }

    private void initialize() {
        Picasso.with(this).load("http://img.zcool.cn/community/event/5360559f7ec26ac72520eee32867.jpg").into(mPictureView);
        mListView.setAdapter(new ArrayAdapter<String>(this,
                R.layout.simple_text,
                android.R.id.text1,getSimpleData()));
    }


    List<String> getSimpleData(){
        List<String> list = new ArrayList<>();
        for(int i=0;i<50;i++) {
            list.add("PullToZoomLayout:" + i);
        }
        return list;
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
}
