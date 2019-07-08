package com.yisingle.map.move.library;

import android.util.Log;

import com.amap.api.maps.model.LatLng;
import com.autonavi.amap.mapcore.DPoint;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.MapProjection;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jikun
 * Created by jikun on 2018/6/7.
 */
public class MoveUtils implements CustomAnimator.OnTimeListener {


    private CustomAnimator customAnimator = new CustomAnimator();

    private IPoint startIPoint = new IPoint(0, 0);


    private OnCallBack callBack;


    public MoveUtils() {
        customAnimator.setOnTimeListener(this);
    }


    /**
     * @param latLng     坐标
     * @param time       时间 毫秒
     * @param isContinue 是否在以上次停止后的坐标点继续移动 当list.size()=1 isContinue 就会变的非常有用
     *                   注意:如果调用 startMove(list,time,isContinue) 如果list.size=1 只传递了一个点并且isContinue=false
     *                   那么 onSetGeoPoint回调方法返回的角度是0 因为只有一个点是无法计算角度的
     */
    public void startMove(LatLng latLng, long time, boolean isContinue) {
        List<LatLng> list = new ArrayList<>();
        startMove(list, time, isContinue);
    }

    /**
     * @param list       坐标数组
     * @param time       时间   毫秒 多长时间走完这些数组
     * @param isContinue 是否在以上次停止后的坐标点继续移动 当list.size()=1
     *                   注意:如果调用 startMove(list,time,isContinue) 如果list.size=1 只传递了一个点并且isContinue=false
     *                   那么 onSetGeoPoint回调方法返回的角度是0 因为只有一个点是无法计算角度的
     */
    public void startMove(List<LatLng> list, long time, boolean isContinue) {
        if (time <= 0) {
            //如果传递过来的参数时间小于等于0
            time = 10;
        }
        List<IPoint> pointList = new ArrayList<>();
        if (isContinue && startIPoint.x != 0 && startIPoint.y != 0) {
            pointList.add(startIPoint);
        }

        for (LatLng latLng : list) {
            IPoint point = new IPoint();
            MapProjection.lonlat2Geo(latLng.longitude, latLng.latitude, point);
            pointList.add(point);
        }


        if (null != list && pointList.size() >= 2) {

            customAnimator.ofIPoints(pointList).start(time);


        } else if (null != list && pointList.size() == 1) {
            if (null != callBack) {
                startIPoint = pointList.get(0);
                DPoint dPoint = new DPoint();
                MapProjection.geo2LonLat(startIPoint.x, startIPoint.y, dPoint);
                callBack.onSetLatLng(new LatLng(dPoint.y, dPoint.x), 0);
            }

        } else {
            Log.e("MoveUtils", "MoveUtils move list is null");

        }


    }

    /**
     * 停止移动
     */
    public void stop() {
        customAnimator.end();
    }

    /**
     * 摧毁
     */
    public void destory() {
        callBack = null;
        customAnimator.setOnTimeListener(null);
        customAnimator.destory();
    }


    public OnCallBack getCallBack() {
        return callBack;
    }


    /**
     * 设置监听回调
     *
     * @param callBack OnCallBack
     */
    public void setCallBack(OnCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onUpdate(IPoint start, IPoint moveIPoint, IPoint end) {
        if (null != callBack) {
            startIPoint = moveIPoint;

            DPoint dPoint = new DPoint();
            MapProjection.geo2LonLat(startIPoint.x, startIPoint.y, dPoint);

            callBack.onSetLatLng(new LatLng(dPoint.y, dPoint.x), getRotate(start, end));
        }

    }


    public interface OnCallBack {


        /**
         * @param latLng IPoint 移动坐标IPoint
         * @param rotate 角度 角度返回  这里的角度返回是根据两个点坐标来计算汽车在地图上的角度的
         *               并不是传感器返回的
         *               如果调用 startMove(list,time,isContinue) 如果list.size=1 只传递了一个点并且isContinue=false
         *               那么 onSetGeoPoint回调方法返回的角度是0
         */
        void onSetLatLng(LatLng latLng, float rotate);
    }


    private float getRotate(IPoint var1, IPoint var2) {
        if (var1 != null && var2 != null) {
            double var3 = (double) var2.y;
            double var5 = (double) var1.y;
            double var7 = (double) var1.x;
            return (float) (Math.atan2((double) var2.x - var7, var5 - var3) / 3.141592653589793D * 180.0D);
        } else {
            return 0.0F;
        }
    }
}