package com.yisingle.map.move.demo;

import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jikun
 * Created by jikun on 2018/9/27.
 */
public class TestRandomUtils {


    public static List<LatLng> getRandomMoveList(LatLng center) {
        List<LatLng> list = new ArrayList<>();

        for (int i = 0; i < 5; i++) {

            LatLng newLatLng = new LatLng(center.latitude + Math.random() * 0.01, center.longitude + Math.random() * 0.01);
            list.add(newLatLng);
        }

        return list;
    }
}
