package com.bold.Dao;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Rect;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;

import com.bold.Util.DB.DataBaseUtil;

public class PictureSurfDao extends Thread
{
    static{
        System.load(new File("D:/opencv249/opencv/build/java/x64/opencv_java249.dll").getAbsolutePath());
    }
    private String filepath;
    public PictureSurfDao(String filepath)
    {
        this.filepath = filepath;
    }
    @Override
    public void run()
    {
        getPoints(filepath);
    }
    public void getPoints(String filepath) 
    {
        
        System.out.println(filepath);
        Mat object=Highgui.imread(filepath);
        object.convertTo(object, CvType.CV_8UC3);
        
        Mat scene=object.submat(new Rect(0,0,object.width(),object.height()));
        FeatureDetector detector = FeatureDetector.create(FeatureDetector.SURF);
        DescriptorExtractor descriptor = DescriptorExtractor.create(DescriptorExtractor.SURF);
        
        Mat descriptors_scene= new Mat();
        MatOfKeyPoint keypoints_scene= new MatOfKeyPoint();
        
        detector.detect(scene, keypoints_scene);
        descriptor.compute(scene, keypoints_scene, descriptors_scene);
        System.out.println(keypoints_scene.toList().size());
        //System.out.println(descriptors_scene.dump());
        System.out.println(descriptors_scene.nativeObj);
        
        /*// 获取数据库连接对象
        Connection conn = DataBaseUtil.getConnection();
        // 根据指定用户名查询用户信息
        String sql = "Insert into tb_keypoints(ptx,pty,size,angle,response,octave,class_id) values (?,?,?,?,?,?,?) ";
        try
        {
            // 插入用户注册信息的sql
            PreparedStatement ps = conn.prepareStatement(sql);;
            for (KeyPoint mk: keypoints_scene.toList())
            {
                ps.setDouble(1, mk.pt.x);
                ps.setDouble(2, mk.pt.y);
                ps.setFloat(3, mk.size);
                ps.setFloat(4, mk.angle);
                ps.setFloat(5, mk.response);
                ps.setInt(6, mk.octave);
                ps.setInt(7, mk.class_id);
                ps.executeUpdate();
            }
            ps.close();    
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            DataBaseUtil.closeConnection(conn);
        }*/
    }
}
