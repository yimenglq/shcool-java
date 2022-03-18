package jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
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
		// 读取请求参数un,pw,tel,addr
		String un = request.getParameter("un");
		String pw = request.getParameter("pw");
		String tel = request.getParameter("tel");
		String addr = request.getParameter("addr");
		if (un == null || pw == null || tel == null || addr == null || un.equals("") || pw.equals("") || tel.equals("") || addr.equals("")) {
			// 用户注册信息不全，给出提示信息
			out.println("请输入完整的<a href=pages/homepage.html>注册</a>信息！");
		} else {
			// 注册操作的结果
			int r = 0;
			// 声明数据库操作对象
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				// 1.加载驱动程序
				Class.forName("com.mysql.cj.jdbc.Driver");
				// 2.定义连接数据库的地址
				String url = "jdbc:mysql://127.0.0.1:3306/345javaweb?serverTimezone=GMT%2B8";
				// 3.建立与数据库的连接
				con = DriverManager.getConnection(url, "root", "220103");
				// 4.声明SQL语句，查询用户名是否可用。
				String sql = "select * from user where username=?";
				// 5.建立语句对象
				pstmt = con.prepareStatement(sql);
				// 6.给SQL的占位符?设置值，执行SQL语句
				pstmt.setString(1, un);
				rs = pstmt.executeQuery();
				// 7.对结果集进行处理
				if (rs.next()) {
					//用户名不可用
					out.println("用户名不可用！");
					out.println("<a href=pages/homepage.html>返回</a>");
					return;
				} else {
					pstmt.close();
					// 用户名可用
					//声明SQL语句，将注册信息插入到数据库
					sql = "insert into user values(null,?,?,0,?,?)";
					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, un);
					pstmt.setString(2, pw);
					pstmt.setString(3, tel);
					pstmt.setString(4, addr);
					r = pstmt.executeUpdate();
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
				if (pstmt != null) {
					try {
						pstmt.close();
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
			//	根据结果给出提示信息
			if(r == 1) {
				//注册成功
				out.println("注册成功！");
			}else {
				//注册失败
				out.println("注册失败！");
			}
			out.println("请<a href=pages/homepage.jsp>返回</a>");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
