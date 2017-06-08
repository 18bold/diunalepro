package com.bold.Servlet;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.catalina.core.ApplicationPart;
import org.opencv.core.Core;

import com.bold.Dao.LostInfoDao;
import com.bold.Dao.PictureSurfDao;
import com.bold.Model.LostInfo;


/**
 * Servlet implementation class AddLostInfoServlet
 */
@WebServlet("/AddLostInfoServlet")
@MultipartConfig(location = "D:/Picture/lost")
public class AddLostInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddLostInfoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8"); 
        response.setContentType("text/html;charset=UTF-8");
        String name = request.getParameter("name");
        String lng = request.getParameter("lng");
        String lat = request.getParameter("lat");
        Part p =request.getPart("pic");
        if(p.getContentType().contains("image")){
            try
            {
                ApplicationPart ap = (ApplicationPart) p;
                String fname1 = ap.getSubmittedFileName();
                DateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
                String time=format.format(new Date());
                String fname2 = time + "." + fname1.split("\\.")[1];
                p.write(fname2);
                LostInfo lostinfo = new LostInfo();
                lostinfo.setName(name);
                lostinfo.setLng(Double.parseDouble(lng));
                lostinfo.setLat(Double.parseDouble(lat));
                lostinfo.setPic(fname2);
                //boolean issuccess = LostInfoDao.insertLostInfo(lostinfo);
                //new PictureSurfDao("D:/Picture/lost/" + fname2).start();
                //System.out.println(issuccess);
            }
            catch(IOException e)
            {
                System.out.println(e);
            }
        }else{
            System.out.println("失败");
        }
	}

}
