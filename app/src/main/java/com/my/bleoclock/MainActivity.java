package com.my.bleoclock;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.data.BleDevice;
import com.my.bleoclock.Fragment.FragmentAlarmClock;
import com.my.bleoclock.Fragment.FragmentAnther;
import com.my.bleoclock.Fragment.FragmentStyle;
import com.my.bleoclock.adapter.MainSectionsPagerAdapter;
import com.my.bleoclock.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private MainSectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private BleDevice bleDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        bleDevice = getIntent().getParcelableExtra("BleDevice");
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        initViewPager();
    }

    private void initViewPager() {
        List<Fragment> fragmentList = new ArrayList<>(4);

        Fragment fragment0 = FragmentAlarmClock.newInstance(bleDevice);
        Fragment    fragment1 = FragmentStyle.newInstance("one","two");
        Fragment    fragment2 = FragmentAnther.newInstance("one","two");

        fragmentList.add(fragment0);
        fragmentList.add(fragment1);
        fragmentList.add(fragment2);

        mSectionsPagerAdapter = new MainSectionsPagerAdapter(getSupportFragmentManager(),fragmentList);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BleManager.getInstance().disconnectAllDevice();
        BleManager.getInstance().destroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // 在RESULT_OK之后保证是data使我们操作之后的结果
//        if (resultCode == RESULT_OK) {
//            if (requestCode == 1) {
//                // 获取传递的数据
//                Bundle bundle = data.getExtras();
//                // 获得返回值
//                boolean istrue = bundle.getBoolean("boolean");
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_notifications) {
            Toast.makeText(this,"点击了搜索",Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
