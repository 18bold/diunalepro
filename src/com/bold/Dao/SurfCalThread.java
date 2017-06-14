package com.bold.Dao;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.features2d.DMatch;
import com.bold.SIFT.SIFT;

public class SurfCalThread extends Thread
{
    private String filepath;
    private int j;
    public static Map<Integer,Integer> rusult = new TreeMap<Integer,Integer>();
    public SurfCalThread(String filepath, int j)
    {
        System.load(new File("D:/opencv249/opencv/build/java/x64/opencv_java249.dll").getAbsolutePath());
        this.filepath = "D:/Picture/lost/" + filepath;
        this.j = j;
    }
    
    @Override
    public void run()
    {
        long startTime=System.currentTimeMillis();   //获取开始时间
        Mat descriptors_object = SIFT.surfcal(filepath);
        List<MatOfDMatch> matches = new ArrayList<MatOfDMatch>();
        SIFT.matcher.knnMatch(descriptors_object, PictureSurfDao.descriptors_scene, matches, 5);
        
        long startTime2=System.currentTimeMillis();   //获取开始时间
        // ratio test
        LinkedList<DMatch> good_matches = new LinkedList<DMatch>();
        for (Iterator<MatOfDMatch> iterator = matches.iterator(); iterator.hasNext();) 
        {
            MatOfDMatch matOfDMatch = (MatOfDMatch)iterator.next();
            if (matOfDMatch.toArray()[0].distance / matOfDMatch.toArray()[1].distance < 0.6)
            {
                good_matches.add(matOfDMatch.toArray()[0]);
            }
        }
        rusult.put(good_matches.size(),j);
        long endTime=System.currentTimeMillis(); //获取结束时间
        System.out.println("第"+(j+1)+"图"+filepath+"匹配数"  + good_matches.size()
            + " 计算时间： "+(endTime-startTime)+"ms" +" 匹配时间： "+(endTime-startTime2)+"ms");
    }
}
