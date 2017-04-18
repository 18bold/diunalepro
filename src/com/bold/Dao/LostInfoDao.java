package com.bold.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bold.Model.LostInfo;
import com.bold.Util.DB.DataBaseUtil;

public class LostInfoDao
{
    /**返回所有的失物招领信息
     * return list
     */
    public static List<LostInfo> getAllInfo()
    {
        LostInfo lostinfo = null;
        List<LostInfo> lostInfoList = new ArrayList<LostInfo>();
        // 获取数据库连接对象
        Connection conn = DataBaseUtil.getConnection();
        // 根据指定用户名查询用户信息
        String sql = "SELECT * FROM tb_lost ";
        try
        {
            // 获取PreparedStatement对象
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                // 实例化一个用户对象
                lostinfo = new LostInfo();
                // 对用户对象属性进行赋值
                lostinfo.setName(rs.getString("name"));
                lostinfo.setLng(rs.getDouble("lng"));
                lostinfo.setLat(rs.getDouble("lat"));
                lostinfo.setPic("/Piclost/"+rs.getString("pic"));
                lostInfoList.add(lostinfo);
            }
            rs.close();
            ps.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            DataBaseUtil.closeConnection(conn);
        }
        return lostInfoList;
    }
    
    public static boolean insertLostInfo(LostInfo li)
    {
        // 获取数据库连接对象
        Connection conn = DataBaseUtil.getConnection();
        // 根据指定用户名查询用户信息
        String sql = "Insert into tb_lost(name,lng,lat,pic) values (?,?,?,?) ";
        try
        {
            // 插入用户注册信息的sql
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, li.getName());
            ps.setDouble(2, li.getLng());
            ps.setDouble(3, li.getLat());
            ps.setString(4, li.getPic());
            ps.executeUpdate();
            ps.close();    
            return true;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            DataBaseUtil.closeConnection(conn);
        }
    }
}
