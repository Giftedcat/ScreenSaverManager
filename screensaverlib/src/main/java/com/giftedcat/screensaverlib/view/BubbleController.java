package com.giftedcat.screensaverlib.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import com.giftedcat.screensaverlib.bean.Bubble;
import com.giftedcat.screensaverlib.helper.ChargingHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * author : GiftedCat
 * date   : 2022-04-07
 * desc   : 气泡
 */
public class BubbleController {

    private String TAG = "BubbleController";

    private Random rd;

    /*** 气泡的最小半径 */
    private float minBubbleRadius;
    /*** 气泡的最大半径 */
    private float maxBubbleRadius;
    /*** 半径变化率 */
    private float radiusRadio;
    /*** 气泡的最小速度 */
    private float minBubbleSpeedY;
    /*** 气泡的最大速度 */
    private float maxBubbleSpeedY;

    /*** 气泡最多有几个 */
    private int maxBubbleCount;
    /*** 气泡颜色 */
    private int color;

    private int viewHeight;
    private int viewWidth;

    /*** 气泡集合 */
    public List<Bubble> mBubbleList = new ArrayList<>();
    /*** 气泡关于路径的map */
    private Map<Bubble, Path> mBubblePathMap = new HashMap<>();
    //气泡与圆环黏和的点数组
    private PointF[] bubblePoints = new PointF[7];

    public BubbleController(BubbleControllerBuilder builder) {
        if (builder.rd == null) throw new NullPointerException("builder.rd == null");
        if (builder.maxBubbleRadius <= builder.minBubbleRadius)
            throw new IllegalArgumentException("builder.maxBubbleRadius <= builder.minBubbleRadius");
        if (builder.maxBubbleSpeedY <= builder.minBubbleSpeedY)
            throw new IllegalArgumentException("builder.maxBubbleSpeedY <= builder.minBubbleSpeedY");
        this.rd = builder.rd;
        this.minBubbleRadius = builder.minBubbleRadius;
        this.maxBubbleRadius = builder.maxBubbleRadius;
        this.radiusRadio = builder.radiusRadio;
        this.minBubbleSpeedY = builder.minBubbleSpeedY;
        this.maxBubbleSpeedY = builder.maxBubbleSpeedY;
        this.maxBubbleCount = builder.maxBubbleCount;

        this.viewWidth = builder.viewWidth;
        this.viewHeight = builder.viewHeight;
        this.color = builder.color;
        for (int i = 0; i < bubblePoints.length; i++) {
            bubblePoints[i] = new PointF();
        }
    }

    static class BubbleControllerBuilder {

        private Random rd;

        private int color;

        /*** 气泡的最小半径 */
        private float minBubbleRadius;
        /*** 气泡的最大半径 */
        private float maxBubbleRadius;
        /*** 半径变化率 */
        private float radiusRadio;
        /*** 气泡的最小速度 */
        private float minBubbleSpeedY;
        /*** 气泡的最大速度 */
        private float maxBubbleSpeedY;
        /*** 气泡最多有几个 */
        private int maxBubbleCount;

        /** view的宽度*/
        private int viewWidth;
        /** view的高度*/
        private int viewHeight;

        public BubbleControllerBuilder setColor(int color) {
            this.color = color;
            return this;
        }

        public BubbleControllerBuilder setMinBubbleRadius(float minBubbleRadius) {
            this.minBubbleRadius = minBubbleRadius;
            return this;
        }

        public BubbleControllerBuilder setMaxBubbleRadius(float maxBubbleRadius) {
            this.maxBubbleRadius = maxBubbleRadius;
            return this;
        }

        public BubbleControllerBuilder setRadiusRadio(float radiusRadio) {
            this.radiusRadio = radiusRadio;
            return this;
        }

        public BubbleControllerBuilder setMinBubbleSpeedY(float minBubbleSpeedY) {
            this.minBubbleSpeedY = minBubbleSpeedY;
            return this;
        }

        public BubbleControllerBuilder setMaxBubbleSpeedY(float maxBubbleSpeedY) {
            this.maxBubbleSpeedY = maxBubbleSpeedY;
            return this;
        }

        public BubbleControllerBuilder setMaxBubbleCount(int maxBubbleCount) {
            this.maxBubbleCount = maxBubbleCount;
            return this;
        }

        public BubbleControllerBuilder setRd(Random rd) {
            this.rd = rd;
            return this;
        }

        public BubbleControllerBuilder setViewWidth(int width) {
            this.viewWidth = width;
            return this;
        }

        public BubbleControllerBuilder setViewHeight(int height) {
            this.viewHeight = height;
            return this;
        }

        public BubbleController build() {
            return new BubbleController(this);
        }
    }

    /**
     * 生成气泡
     */
    public void generateBubble() {

        if (mBubbleList.size() >= maxBubbleCount) return;
        //延迟生成气泡的作用
        if (rd.nextBoolean() || rd.nextBoolean() || rd.nextBoolean()) return;

        int bubbleAlpha = 255 / 2 + rd.nextInt(255 / 2);
        float radius = minBubbleRadius + rd.nextInt((int) (maxBubbleRadius - minBubbleRadius));

        //气泡的x起始坐标
        float x = rd.nextInt(viewWidth);
        //气泡的y起始坐标
        float y = viewHeight + radius;
        //气泡的起始速度
        float speedY = minBubbleSpeedY + rd.nextFloat() * (maxBubbleSpeedY - minBubbleSpeedY);
        float sizeRadio = radiusRadio * rd.nextFloat();

        ChargingHelper.generateBubble(mBubbleList, x, y, radius, speedY, bubbleAlpha, color, sizeRadio);
    }

    /**
     * 遍历气泡
     */
    public void performTraversals() {

        for (int i = 0; i < mBubbleList.size(); i++) {
            Bubble b = mBubbleList.get(i);
            if (b.getRadius() < minBubbleRadius || b.getY() <= 0) {
                mBubbleList.remove(b);
                if (mBubblePathMap.containsKey(b)) {
                    mBubblePathMap.remove(b);
                }
                i--;
                continue;
            }

            b.setY(b.getY() - b.getSpeedY());

            if (!b.isUnAccessible()) {
                //改变半径
                b.setRadius(b.getRadius() - b.getSizeRadio());
            }
            changeBubblePath(b);
        }

    }


    /**
     * 改变气泡的路径
     *
     * @param b
     */
    private void changeBubblePath(Bubble b) {

        Path path = mBubblePathMap.get(b);
        if (path == null) {
            path = new Path();
            mBubblePathMap.put(b, path);
        } else {
            path.reset();
        }

        generatePath(b, path);
    }

    /**
     * 生成路径
     * */
    private void generatePath(Bubble b, Path path) {
        path.addCircle(b.getX(), b.getY(), b.getRadius(), Path.Direction.CCW);

        b.setSpeedY(b.getOriginalSpeedY());
    }

    /**
     * 修改颜色
     *
     * @param color
     */
    public void setColor(int color) {
        this.color = color;
        mBubblePathMap.clear();
        mBubbleList.clear();
    }

    /**
     * 绘制气泡
     *
     * @param canvas
     */
    public void drawBubble(Canvas canvas, Paint paint) {
        for (int i = 0; i < mBubbleList.size(); i++) {
            Bubble b = mBubbleList.get(i);
            paint.setColor(b.getColor());
            Path path = mBubblePathMap.get(b);
            if (path != null) {
                canvas.drawPath(path, paint);
            }
        }
    }

}
