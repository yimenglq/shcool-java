package jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.UserService;

/**
 * Servlet implementation class UserList
 */
@WebServlet("/list_user")
public class ListUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListUser() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		// 读取模糊查询参数
		String s_un = request.getParameter("s_un");
		// 调用业务逻辑类完成用户信息查询
		UserService us = new UserService();
		List<Map<String, String>> users = us.getUsers(s_un);
		// 在表格中显示用户信息列表
		out.println("<div align=center>");
		// 显示查询用户名表单
		out.println("<form action=list_user method=post>");
		out.println("<input type=text name=s_un placeholder=请输入用户名 value=" 
						+ (s_un == null ? "" : s_un)
						+ ">");
		out.println("<input type=submit value=搜索>");
		out.println("</form>");
		// 表格显示用户信息列表
		out.println("<table border>");
		out.println("<tr><th>序号</th><th>用户名</th>"
				+ "<th>角色</th><th>电话</th>"
				+ "<th>地址</th><th>操作</th></tr>");
		int num = 0;
		for(Map<String,String> u : users) {
			num++;
			out.println("<tr>");
			out.println("<td>" + num + "</td>");
			out.println("<td>" + u.get("username") + "</td>");
			out.println("<td>" + (u.get("ident").equals("1") ? "管理员" : "普通用户") + "</td>");
			out.println("<td>" + u.get("telephone") + "</td>");
			out.println("<td>" + u.get("address") + "</td>");
			out.println("<td><a href=del_user?id="
					+ u.get("id") + " onclick=\"return confirm('确定要删除吗？')\""
					+">删除</a></td>");
			out.println("</tr>");
		}
		out.println("</table>");
		out.println("</div>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
