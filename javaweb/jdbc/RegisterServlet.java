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
		// ��ȡ�������un,pw,tel,addr
		String un = request.getParameter("un");
		String pw = request.getParameter("pw");
		String tel = request.getParameter("tel");
		String addr = request.getParameter("addr");
		if (un == null || pw == null || tel == null || addr == null || un.equals("") || pw.equals("") || tel.equals("") || addr.equals("")) {
			// �û�ע����Ϣ��ȫ��������ʾ��Ϣ
			out.println("������������<a href=pages/homepage.html>ע��</a>��Ϣ��");
		} else {
			// ע������Ľ��
			int r = 0;
			// �������ݿ��������
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				// 1.������������
				Class.forName("com.mysql.cj.jdbc.Driver");
				// 2.�����������ݿ�ĵ�ַ
				String url = "jdbc:mysql://127.0.0.1:3306/345javaweb?serverTimezone=GMT%2B8";
				// 3.���������ݿ������
				con = DriverManager.getConnection(url, "root", "220103");
				// 4.����SQL��䣬��ѯ�û����Ƿ���á�
				String sql = "select * from user where username=?";
				// 5.����������
				pstmt = con.prepareStatement(sql);
				// 6.��SQL��ռλ��?����ֵ��ִ��SQL���
				pstmt.setString(1, un);
				rs = pstmt.executeQuery();
				// 7.�Խ�������д���
				if (rs.next()) {
					//�û���������
					out.println("�û��������ã�");
					out.println("<a href=pages/homepage.html>����</a>");
					return;
				} else {
					pstmt.close();
					// �û�������
					//����SQL��䣬��ע����Ϣ���뵽���ݿ�
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
				// 8.�ر�����
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
			//	���ݽ��������ʾ��Ϣ
			if(r == 1) {
				//ע��ɹ�
				out.println("ע��ɹ���");
			}else {
				//ע��ʧ��
				out.println("ע��ʧ�ܣ�");
			}
			out.println("��<a href=pages/homepage.jsp>����</a>");
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
