package com.bold.Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bold.Dao.LostInfoDao;
import com.bold.Model.LostInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class LostInfoServlet
 */
@WebServlet("/GetLostInfoServlet")
public class GetLostInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetLostInfoServlet() {
        super();
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
	    List<LostInfo> list =LostInfoDao.getAllInfo();    // 创建返回的list,所有的失物招领信息    
        // 处理返回前端的json数据
        JSONObject s=new JSONObject();    // 声明一个json对象
        JSONArray json = JSONArray.fromObject(list);    // 将list对象转为json格式
        s.put("count", list.size());    // count为信息条数
        s.put("datas", json);    // datas为信息内容
        // 输出json格式的数据到前端
        response.setContentType("text/html;charset=utf-8");    // 防止乱码
        PrintWriter out = response.getWriter();
        out.write(s.toString());
        //System.out.println(s);
	}

}
