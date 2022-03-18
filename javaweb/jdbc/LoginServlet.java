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
		// ��ȡ�������un��pw
		String un = request.getParameter("un");
		String pw = request.getParameter("pw");
		if (un == null || pw == null || un.equals("") || pw.equals("")) {
			// �û�δ��¼��������ʾ��Ϣ
			out.println("����<a href=pages/homepage.jsp>��¼</a>��");
		} else {
			// �������ݿ��������
			Connection con = null;
			Statement stmt = null;
			ResultSet rs = null;
			// �������ݿ���е�¼��֤
			try {
				// 1.������������
				Class.forName("com.mysql.cj.jdbc.Driver"); 
				// 2.�����������ݿ�ĵ�ַ
				String url = "jdbc:mysql://127.0.0.1:3306/345javaweb?serverTimezone=GMT%2B8";
				// 3.���������ݿ������
				con = DriverManager.getConnection(url, "root", "220103");
				out.println("�����������ݿ�");
				// 4.����SQL���
				String sql = "select * from user where username='" + un + "' and password='" + pw + "'";
				// 5.����������
				stmt = con.createStatement();
				// 6.ִ��SQL���
				rs = stmt.executeQuery(sql);
				// 7.�Խ�������д���
				if (rs.next()) {
					// ��¼�ɹ�
					String ident = rs.getString("ident");
					// ��session�����û��ĵ�¼��Ϣ
					session.setAttribute("loginID", rs.getString("id"));
					session.setAttribute("loginName", un);
					session.setAttribute("ident", ident);
					if (ident.equals("1")) {
						// ����Ա
						out.println("����Ա��¼�ɹ���");
						out.println("<a href=pages/homepage.jsp>����</a>");
					} else {
						// ��ͨ�û�
						out.println("��ͨ�û���¼�ɹ���");
						out.println("<a href=pages/homepage.jsp>����</a>");
					}
				} else {
					// ��¼ʧ��
					out.println("�û������������");
					out.println("<a href=pages/homepage.jsp>����</a>");
				}

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				// 8.�ر�����
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
