package com.yisingle.map.move.demo.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;

/**
 * @author jikun
 * Created by jikun on 2018/9/23.
 */
public class BaseMapActivity extends AppCompatActivity {

    private TextureMapView textureMapView;


    protected TextureMapView getMapView() {
        return textureMapView;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected void initMapView(Bundle savedInstanceState, TextureMapView textureMapView) {
        this.textureMapView = textureMapView;


        if (null != getMapView() && null != getMapView().getMap()) {

            getMapView().getMap().setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
                @Override
                public void onMapLoaded() {
                    //地图MapView加载完成后回调 用来UiSetting设置参数
                    initMapUiSetting();


                }
            });

            //调用TextureMapView.onCreate方法来
            getMapView().onCreate(savedInstanceState);

        }
    }


    /**
     * 方法必须重写
     */
    @Override

    public void onResume() {
        super.onResume();
        if (null != getMapView()) {
            getMapView().onResume();
        }

    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        if (null != getMapView()) {
            getMapView().onPause();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (null != getMapView()) {
            getMapView().onSaveInstanceState(outState);
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

        if (null != getMapView()) {
            getMapView().onDestroy();
        }
    }


    private void initMapUiSetting() {

        //实例化UiSettings类对象
        UiSettings uiSettings = textureMapView.getMap().getUiSettings();
        //设置是否允许显示缩放按钮
        uiSettings.setZoomControlsEnabled(false);
        //设置Logo在底部右下角
        uiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);
        //禁止旋转手势
        uiSettings.setRotateGesturesEnabled(false);
        //禁止倾斜手势
        uiSettings.setTiltGesturesEnabled(false);

    }


    public AMap getAmap() {
        return textureMapView.getMap();
    }
}
