package jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		// 读取请求参数un和pw
		String un = request.getParameter("un");
		String pw = request.getParameter("pw");
		if (un == null || pw == null || un.equals("") || pw.equals("")) {
			// 用户未登录，给出提示信息
			out.println("请先<a href=pages/homepage.jsp>登录</a>！");
		} else {
			// 声明数据库操作对象
			Connection con = null;
			Statement stmt = null;
			ResultSet rs = null;
			// 连接数据库进行登录验证
			try {
				// 1.加载驱动程序
				Class.forName("com.mysql.cj.jdbc.Driver"); 
				// 2.定义连接数据库的地址
				String url = "jdbc:mysql://127.0.0.1:3306/345javaweb?serverTimezone=GMT%2B8";
				// 3.建立与数据库的连接
				con = DriverManager.getConnection(url, "root", "220103");
				out.println("正在连接数据库");
				// 4.声明SQL语句
				String sql = "select * from user where username='" + un + "' and password='" + pw + "'";
				// 5.建立语句对象
				stmt = con.createStatement();
				// 6.执行SQL语句
				rs = stmt.executeQuery(sql);
				// 7.对结果集进行处理
				if (rs.next()) {
					// 登录成功
					String ident = rs.getString("ident");
					// 用session保存用户的登录信息
					session.setAttribute("loginID", rs.getString("id"));
					session.setAttribute("loginName", un);
					session.setAttribute("ident", ident);
					if (ident.equals("1")) {
						// 管理员
						out.println("管理员登录成功！");
						out.println("<a href=pages/homepage.jsp>返回</a>");
					} else {
						// 普通用户
						out.println("普通用户登录成功！");
						out.println("<a href=pages/homepage.jsp>返回</a>");
					}
				} else {
					// 登录失败
					out.println("用户名或密码错误！");
					out.println("<a href=pages/homepage.jsp>返回</a>");
				}

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				// 8.关闭连接
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (con != null) {
					try {
						con.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
