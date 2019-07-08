# MarkerMoveUtils
可以在高德地图平滑移动marker工具

### 效果图如下:

![image](https://raw.githubusercontent.com/jikun2008/MarkerMoveUtils/master/pic/show.gif)


#### 如何使用
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
	
	
	
		dependencies {
	        implementation 'com.github.jikun2008:MarkerMoveUtils:Tag'
	}
```


#### code
```java
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
        
        
             LatLng center = new LatLng(30.546284, 104.06934);
        moveUtils.startMove(List<LatLng> 5000, false);
```
