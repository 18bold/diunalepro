package com.bold.Servlet;

import java.io.IOException;
import java.io.PrintWriter;
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

import com.bold.Dao.PictureSurfDao;
import com.bold.Model.LostInfo;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class PicSearch
 */
@WebServlet("/PicSearchLostServlet")
@MultipartConfig(location = "D:/Picture/search")
public class PicSearchLostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PicSearchLostServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    long startTime=System.currentTimeMillis();   //获取开始时间
	    request.setCharacterEncoding("utf-8"); 
	    Part p =request.getPart("upfile");
	    LostInfo result = null;
	    if(p.getContentType().contains("image")){
            try
            {
                ApplicationPart ap = (ApplicationPart) p;
                String fname1 = ap.getSubmittedFileName();
                DateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
                String time=format.format(new Date());
                String fname2 = time + "." + fname1.split("\\.")[1];
                p.write(fname2);
                System.out.println("成功");
                PictureSurfDao psd = new PictureSurfDao("D:/Picture/search/" + fname2);
                result = psd.calpiclist();
            }
            catch(IOException e)
            {
                System.out.println(e);
            }
        }else{
            System.out.println("失败");
        }
	    JSONObject s=new JSONObject();    // 声明一个json对象
        s.put("message", "success");    // datas为信息内容
        s.put("result", result);
        // 输出json格式的数据到前端
        response.setContentType("text/html;charset=utf-8");    // 防止乱码
        PrintWriter out = response.getWriter();
        out.write(s.toString());
        long endTime=System.currentTimeMillis(); //获取结束时间
        System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
	}

}
