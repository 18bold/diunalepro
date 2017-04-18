package com.bold.Model;

public class LostInfo
{
    private String name;    // 失物名称
    private double lng;    // 经度
    private double lat;    // 纬度
    private String pic;    // 图片
    
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public double getLng()
    {
        return lng;
    }
    public void setLng(double lng)
    {
        this.lng = lng;
    }
    public double getLat()
    {
        return lat;
    }
    public void setLat(double lat)
    {
        this.lat = lat;
    }
    public String getPic()
    {
        return pic;
    }
    public void setPic(String pic)
    {
        this.pic = pic;
    }
}
