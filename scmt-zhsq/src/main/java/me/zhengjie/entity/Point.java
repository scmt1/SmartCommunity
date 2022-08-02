package me.zhengjie.entity;

/***
 * 坐标点
 */
public class Point {
    private Double x;
    private Double y;

    public Point(){

    }

    public Point (Double x , Double y) {
        this.x = x;
        this.y = y;
    }
    public Double getX() {
        return x;
    }
    public void setX(Double x) {
        this.x = x;
    }
    public Double getY() {
        return y;
    }
    public void setY(Double y) {
        this.y = y;
    }
}
