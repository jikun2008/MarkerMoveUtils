package com.yisingle.map.move.demo;

import android.os.Bundle;
import android.view.View;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.yisingle.map.move.demo.base.BaseMapActivity;
import com.yisingle.map.move.library.MoveUtils;

public class MainActivity extends BaseMapActivity {

    protected MoveUtils moveUtils;

    protected Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextureMapView textureMapView = findViewById(R.id.textureMapView);
        initMapView(savedInstanceState, textureMapView);

        MarkerOptions markerOptions = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.move_car))
                .anchor(0.5f, 0.5f);
        marker = getAmap().addMarker(markerOptions);

        moveUtils = new MoveUtils();

        moveUtils.setCallBack(new MoveUtils.OnCallBack() {
            @Override
            public void onSetLatLng(LatLng latLng, float rotate) {
                marker.setPosition(latLng);
                //车辆方向
                float carDirection = 360.0F - rotate + getAmap().getCameraPosition().bearing;
                marker.setRotateAngle(carDirection);
            }
        });

        testMove(null);

    }


    public void testMove(View view) {
        LatLng center = new LatLng(30.546284, 104.06934);
        moveUtils.startMove(TestRandomUtils.getRandomMoveList(center), 5000, false);

        moveCamre(center);
    }


    public void testStop(View view) {
        moveUtils.stop();

    }

    private void moveCamre(LatLng center) {


        getAmap().animateCamera(CameraUpdateFactory.newLatLngZoom(center, 14));
    }
}
