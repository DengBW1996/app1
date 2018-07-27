package com.example.a_199.myapplication;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private DrawerLayout mDrawerLayout;

    private ViewPager viewPager;

    private ArrayList<View> pageView;

    private TextView scanLayout;

    private TextView favoriteLayout;

    private SwipeRefreshLayout swipeRefresh;

    private FavoriteItem[] favoriteItems = {new FavoriteItem("NoSavedItem",
            R.drawable.no_saved_item)};

    private List<FavoriteItem> favoriteItemList = new ArrayList<>();

    private FavoriteItemAdapter adapter;

    private FavoriteItem currentItem;

    private TextView mResult;

    private ImageView scrollBar; //滚动条图片

    private int offset = 0; //滚动条初始偏移量

    private int currentIndex = 0; //当前页编号

    private int bmpW; //滚动条宽度

    private int one; //一倍滚动量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        //查找布局文件用LayoutInflater.inflate
        LayoutInflater inflater = getLayoutInflater();
        final View scanView = inflater.inflate(R.layout.scan_view, null);
        View favoriteView = inflater.inflate(R.layout.favorite_view, null);
        scanLayout = (TextView) findViewById(R.id.scan_layout);
        favoriteLayout = (TextView) findViewById(R.id.favorite_layout);
        scrollBar = (ImageView) findViewById(R.id.scroll_bar);
        mResult = scanView.findViewById(R.id.name_of_scan);
        currentItem = null;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();

        //从左侧滑动显示mini菜单
        // ----
        // \  /
        //  \/
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }
        navView.setCheckedItem(R.id.nav_settings);
        navView.setNavigationItemSelectedListener(new NavigationView.
                OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_settings:
                        /**
                         * 打开设置页面
                         */
                        Intent intentSettings = new Intent(MainActivity.this,
                                Settings.class);
                        startActivity(intentSettings);
                        mDrawerLayout.closeDrawers();
                        break;

                    case R.id.nav_feedback:
                        /**
                         * 打开反馈页面
                         */
                        Intent intentFeedback = new Intent(MainActivity.this,
                                Feedback.class);
                        startActivity(intentFeedback);
                        mDrawerLayout.closeDrawers();
                        break;

                    case R.id.nav_exit:
                        /**
                         * 退出
                         */
                        finish();
                        break;

                }
                return true;
            }
        });
        //  /\
        // /  \
        // ----

        //主页面实现滑动切换界面
        // ----
        // \  /
        //  \/


        scanLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
            }
        });
        favoriteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });
        pageView = new ArrayList<View>();
        //添加想要切换的页面
        pageView.add(scanView);
        pageView.add(favoriteView);
        //数据适配器
        PagerAdapter mPagerAdapter = new PagerAdapter() {

            //获取当前窗体界面数
            @Override
            public int getCount() {
                return pageView.size();
            }

            //判断是否由对象生成界面
            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            //从ViewGroup中移出当前View
            public void destroyItem(View arg0, int arg1){
                ((ViewPager) arg0).removeView(pageView.get(arg1));
            }

            //返回一个对象，表明PagerAdapter适配器选择哪个对象放在当前的ViewPager中
            public Object instantiateItem(View arg0, int arg1){
                ((ViewPager)arg0).addView(pageView.get(arg1));
                return pageView.get(arg1);
            }
        };
        //绑定适配器
        viewPager.setAdapter(mPagerAdapter);
        //设定viewPager的初始界面为第一个界面
        viewPager.setCurrentItem(0);
        //添加切换界面的监听器
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        // 获取滚动条的宽度
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.scrollbar).getWidth();
        //为了获取屏幕宽度，新建一个DisplayMetrics对象
        DisplayMetrics displayMetrics = new DisplayMetrics();
        //将当前窗口的一些信息放在DisplayMetrics类中
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //得到屏幕的宽度
        int screenWidth = displayMetrics.widthPixels;
        //计算出滚动条初始的偏移量
        offset = (screenWidth / 2 - bmpW) / 2;
        //计算出切换一个界面时，滚动条的位移量
        one = offset * 2 + bmpW;
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        //将滚动条的初始位置设置成与左边界间隔一个offset
        scrollBar.setImageMatrix(matrix);
        //  /\
        // /  \
        // ----

        //添加tapToScan按钮及点击事件
        Button tapToScan = (Button) scanView.findViewById(R.id.tap_to_scan);
        tapToScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan();
            }
        });

        //添加保存按钮
        FloatingActionButton fab = (FloatingActionButton) scanView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ----  <--添加保存操作-->
                // \  /
                //  \/


                //  /\
                // /  \
                // ----
                Snackbar.make(view, "保存成功", Snackbar.LENGTH_SHORT)
                        .setAction("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this, "取消成功",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });

        //下拉刷新
        // ----
        // \  /
        //  \/

        //  /\
        // /  \
        // ----

        //
        // ----
        // \  /
        //  \/



        //  /\
        // /  \
        // ----

    }

    private void scan(){
        try {
            startActivityForResult(new Intent(MainActivity.this, CaptureActivity.class), 0);
        } catch (SecurityException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            String result = bundle.getString(Constant.CODED_CONTENT);
            mResult.setText(result);
        }
    }

    //

    //初始化保存项
    private void initFavoriteItems(){

    }

    //刷新已保存项
    private void refreshFavoriteItems(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //<--读取图片-->
            }
        }).start();
    }




    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.more:

                break;
            case R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageSelected(int position) {
            Animation animation = null;
            switch (position){
                case 0:
                    /**
                     * TranslateAnimation的四个属性分别为
                     * float fromXDelta 动画开始的点离当前View X坐标上的差值
                     * float toXDelta 动画结束的点离当前View X坐标上的差值
                     * float fromYDelta 动画开始的点离当前View Y坐标上的差值
                     * float toYDelta 动画开始的点离当前View Y坐标上的差值
                     */
                    animation = new TranslateAnimation(one, 0, 0, 0);
                    break;
                case 1:
                    animation = new TranslateAnimation(offset, one, 0, 0);
                    break;
            }
            //position为切换到的页的页码
            currentIndex = position;
            //将此属性设置为true可以使得图片停在动画结束时的位置
            animation.setFillAfter(true);
            //动画持续时间，单位为毫秒
            animation.setDuration(200);
            //滚动条开始动画
            scrollBar.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
