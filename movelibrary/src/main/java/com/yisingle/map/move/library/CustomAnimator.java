package com.yisingle.map.move.library;
import com.autonavi.amap.mapcore.IPoint;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author jikun
 * Created by jikun on 2018/6/6.
 */
public class CustomAnimator {

    private ExecutorService mThreadPools;


    private CustomRunnable customRunnable;


    private OnTimeListener onTimeListener;


    List<IPoint> iPointList;


    public CustomAnimator() {
        mThreadPools = new ThreadPoolExecutor(0,
                1,
                200L,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(1),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r);
                    }
                }
                , new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    public CustomAnimator ofIPoints(IPoint start, IPoint end) {

        List<IPoint> list = new ArrayList<>();
        list.add(start);
        list.add(end);
        return ofIPoints(list);

    }


    public CustomAnimator ofIPoints(IPoint... points) {
        List<IPoint> list = new ArrayList<>();
        for (IPoint point : points) {
            list.add(point);
        }
        return ofIPoints(list);

    }


    public CustomAnimator ofIPoints(List<IPoint> list) {
        this.iPointList = list;
        return this;

    }


    public void start(long duration) {
        end();

        customRunnable = new CustomRunnable(iPointList, duration);
        customRunnable.exitFlag.set(false);
        mThreadPools.submit(customRunnable);
    }


    public void end() {
        if (null != customRunnable) {
            customRunnable.exitFlag.set(true);
        }
        //----cancale
    }

    public class CustomRunnable implements Runnable {
        AtomicBoolean exitFlag = new AtomicBoolean(false);

        private long startTime;

        private long duration = 1000l;


        private List<IPoint> pointList;


        private int index = 0;


        private CustomRunnable(List<IPoint> list, long duration) {
            this.duration = duration;
            index = 0;
            this.pointList = list;
            startTime = System.currentTimeMillis();

        }


        @Override
        public void run() {
            if (pointList.size() >= 2) {
                IPoint start = pointList.get(0);
                while (!exitFlag.get()) {

                    if (index + 1 < pointList.size()) {

                        long currentTime = System.currentTimeMillis();
                        long time = currentTime - startTime;
                        IPoint end = pointList.get(index + 1);
                        int plugX = end.x - start.x;
                        int plugY = end.y - start.y;


                        int intervalSize = pointList.size() - 1;

                        float intervalDuration = (float) duration / intervalSize;

                        float percent = (float) time / intervalDuration;

                        IPoint moveIPoint = new IPoint((int) ((double) start.x + (double) plugX * (percent - index)), (int) ((double) start.y + (double) plugY * (percent - index)));
                        if (percent - index >= 1) {
                            index = index + 1;
                            start = moveIPoint;
                        }


                        if (null != onTimeListener) {
                            onTimeListener.onUpdate(pointList.get(index), moveIPoint, pointList.get(index + 1));
                        }
                    } else {
                        exitFlag.set(true);
                    }

                }
            }
            //--线程结束
        }
    }


    public CustomAnimator setOnTimeListener(OnTimeListener onTimeListener) {
        this.onTimeListener = onTimeListener;
        return this;
    }

    public void destory() {
        end();
        mThreadPools.shutdownNow();

    }

    public interface OnTimeListener {
        /**
         * 更新
         *
         * @param moveIPoint IPoint
         */
        void onUpdate(IPoint start, IPoint moveIPoint, IPoint end);


    }


}