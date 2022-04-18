# ScreenSaverManager
一个简单的Android屏保工具，一行代码启动，防止设备烧屏

# （一）介绍
如今android的智能设备应用以及越来越广泛，在实际应用场景中，一些android设备屏幕长期处于开启状态，并且页面上没有较大的变动，会出现烧屏的现象

![1634667688BqrHm1.jpg](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/56f1b0e313d343d69bbf7e54d45a4474~tplv-k3u1fbpfcp-watermark.image?)
为降低这些设备的维护成本，可以使用一个简单的屏保，不停屏幕的每个区域，同时也不影响这些屏幕的正常使用

![screencapture-1601552540158.gif](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/ff971e9b35e9495dbbf6cd8136833941~tplv-k3u1fbpfcp-watermark.image?)

# （二）如何使用
#### 2.1添加依赖
首先我们需要在app/build.gradle文件中添加ScreenSaver的依赖，如下所示。
````
implementation 'com.github.Giftedcat:ScreenSaverManager:1.1'
````
#### 2.2初始化
可以在首页进行初始化，并且可以根据需要对气泡的颜色、大小、速度、变化速率等参数进行调整
````
ScreenSaverManager.getInstance()
        .init(this)
        .setBackgroundColorResource(R.color.black)
        .setCountDownTime(10)
        .setBubbleColorResource(R.color.white)
        .setMinBubbleRadius(10)
        .setMaxBubbleRadius(30)
        .setRadiusRadio(0.1f)
        .setMinBubbleSpeedY(4)
        .setMaxBubbleSpeedY(10)
        .setMaxBubbleCount(20);
````
#### 2.3激活
初始化之后，可以在基类（baseActivity）的触摸事件中，调用该工具的active函数，用于在用户触摸屏幕后重新进行空闲事件计时
````
@Override
public boolean dispatchTouchEvent(MotionEvent ev) {
    switch (ev.getAction()) {
        case MotionEvent.ACTION_UP:
            /** 用户触摸屏幕后  屏保重新计时*/
            ScreenSaverManager.getInstance().active();
            break;
    }
    return super.dispatchTouchEvent(ev);
}
````
#### 2.4销毁
在不需要使用或者主页面销毁时，及时关闭

````
ScreenSaverManager.getInstance().destroy();
````

欢迎star和follow
