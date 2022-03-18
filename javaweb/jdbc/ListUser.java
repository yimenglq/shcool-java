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
		// ��ȡģ����ѯ����
		String s_un = request.getParameter("s_un");
		// ����ҵ���߼�������û���Ϣ��ѯ
		UserService us = new UserService();
		List<Map<String, String>> users = us.getUsers(s_un);
		// �ڱ������ʾ�û���Ϣ�б�
		out.println("<div align=center>");
		// ��ʾ��ѯ�û�����
		out.println("<form action=list_user method=post>");
		out.println("<input type=text name=s_un placeholder=�������û��� value=" 
						+ (s_un == null ? "" : s_un)
						+ ">");
		out.println("<input type=submit value=����>");
		out.println("</form>");
		// �����ʾ�û���Ϣ�б�
		out.println("<table border>");
		out.println("<tr><th>���</th><th>�û���</th>"
				+ "<th>��ɫ</th><th>�绰</th>"
				+ "<th>��ַ</th><th>����</th></tr>");
		int num = 0;
		for(Map<String,String> u : users) {
			num++;
			out.println("<tr>");
			out.println("<td>" + num + "</td>");
			out.println("<td>" + u.get("username") + "</td>");
			out.println("<td>" + (u.get("ident").equals("1") ? "����Ա" : "��ͨ�û�") + "</td>");
			out.println("<td>" + u.get("telephone") + "</td>");
			out.println("<td>" + u.get("address") + "</td>");
			out.println("<td><a href=del_user?id="
					+ u.get("id") + " onclick=\"return confirm('ȷ��Ҫɾ����')\""
					+">ɾ��</a></td>");
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
