package com.bold.SIFT;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Rect;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.highgui.Highgui;

public class SIFT
{
    public static FeatureDetector  detector =FeatureDetector.create(FeatureDetector.SIFT);
    public static DescriptorExtractor extractor=DescriptorExtractor.create(DescriptorExtractor.SIFT); 
    public static DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);
    
    public static Mat surfcal(String filepath)
    {
        synchronized (SIFT.class)
        {
            Mat object = Highgui.imread(filepath);
            object.convertTo(object, CvType.CV_8UC3);
            object = object.submat(new Rect(0,0,object.width(),object.height()));
            MatOfKeyPoint keypoints_object = new MatOfKeyPoint();
            detector.detect(object, keypoints_object);
            Mat descriptors_object = new Mat();
            extractor.compute(object, keypoints_object, descriptors_object);
            return descriptors_object;
        }
        
    }
}
