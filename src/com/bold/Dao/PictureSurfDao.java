package com.bold.Dao;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import com.bold.Model.LostInfo;
import com.bold.SIFT.SIFT;
import com.bold.Util.DB.DataBaseUtil;

public class PictureSurfDao 
{
    private String filepath;
    private List<String> piclist;
    public static Mat descriptors_scene = null;
    public PictureSurfDao(String filepath)
    {
        System.load(new File("D:/opencv249/opencv/build/java/x64/opencv_java249.dll").getAbsolutePath());
        this.filepath = filepath;
        getFiles();
    }
    public void getFiles() 
    {
        piclist = new ArrayList<String>();
        // 获取数据库连接对象
        Connection conn = DataBaseUtil.getConnection();
        // 根据指定用户名查询用户信息
        String sql = "select pic from tb_lost ";
        try
        {
            // 获取PreparedStatement对象
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                piclist.add(rs.getString("pic"));
            }
            rs.close();
            ps.close();
            System.out.println(piclist);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            DataBaseUtil.closeConnection(conn);
        }
    }
    // 计算和匹配
    public LostInfo calpiclist()
    {
        descriptors_scene = SIFT.surfcal(filepath);
        for (int j =0; j < 1; j++)
        {
            for (int i = 0; i < piclist.size(); i++)
            {
                new SurfCalThread(piclist.get(i), i).start();
            }
        }
        while (true)
        {
            if (SurfCalThread.rusult.size() == piclist.size()) 
            {
                System.out.println(SurfCalThread.rusult);
                Object[] w = SurfCalThread.rusult.keySet().toArray();
                return getLost(piclist.get(SurfCalThread.rusult.get(w[piclist.size()-1])));
            }
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
    /**
     * 获取失物招领信息
     */
    public LostInfo getLost(String picname)
    {
        // 获取数据库连接对象
        Connection conn = DataBaseUtil.getConnection();
        // 根据指定用户名查询用户信息
        String sql = "select * from tb_lost where pic= ?";
        try
        {
            // 获取PreparedStatement对象
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, picname);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                LostInfo lostinfo = new LostInfo();
                lostinfo.setName(rs.getString("name"));
                lostinfo.setLng(rs.getDouble("lng"));
                lostinfo.setLat(rs.getDouble("lat"));
                lostinfo.setPic("/Piclost/"+rs.getString("pic"));
                return lostinfo;
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
        return null;
    }
    
}
