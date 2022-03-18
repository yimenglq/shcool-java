package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtil {
	// ���ݿ���������
	private String driver;
	// ���ݿ�������Ϣ
	private String url;
	// �������ݿ���û���
	private String username;
	// �������ݿ������
	private String password;
	// ���Ӷ���
	protected Connection con;
	// Ԥ����������
	protected PreparedStatement pstmt;
	// ��ѯ�����ҳʱ��ÿҳ��ʾ��¼����
	public static final long PAGE_REC_NUM = 8;

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public DBUtil() {
		driver = "com.mysql.cj.jdbc.Driver";
		url = "jdbc:mysql://localhost:3306/ysx?serverTimezone=GMT%2B8";//serverTimezone=UTC&useSSL=false
		username = "root";
		password = "220103";
	}

	// ��ʼ�������������������򣬻�����ݿ�����Ӷ���
	protected void init() {
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// ���ݿ��������Ĺر�
	protected void close() {
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

	// ΪԤ�����SQL����е�ռλ��?����ֵ��ֵ�������ַ�������params�С�
	private void setParams(String[] params) {
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				try {
					pstmt.setString(i + 1, params[i]);
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}

		}
	}

	// ִ�и����ࣨ��ɾ�ģ���SQL��䣬SQL����к���ռλ����
	public int update(String sql, String[] params) {
		int result = 0;
		init();
		try {
			pstmt = con.prepareStatement(sql);
			setParams(params);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	}

	// ִ�и����ࣨ��ɾ�ģ���SQL��䣬SQL����в�����ռλ����
	public int update(String sql) {
		return update(sql, null);
	}

	// ִ�з��ض�����¼�Ĳ�ѯ�������������װ��List�С�
	public List<Map<String, String>> getList(String sql, String[] params) {
		List<Map<String, String>> list = null;
		init();
		try {
			pstmt = con.prepareStatement(sql);
			setParams(params);
			System.out.println(pstmt.toString());
			ResultSet rs = pstmt.executeQuery();
			list = getListFromRS(rs);
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return list;
	}

	public List<Map<String, String>> getList(String sql) {
		return getList(sql, null);
	}

	// ִ�з�������һ����¼�Ĳ�ѯ�������������װ��Map�С�
	public Map<String, String> getMap(String sql, String[] params) {
		Map<String, String> m = null;
		List<Map<String, String>> l = getList(sql, params);
		if (l != null && l.size() != 0) {
			m = (Map<String, String>) (l.get(0));
		}
		return m;
	}

	public Map<String, String> getMap(String sql) {
		return getMap(sql, null);
	}

	// ��������е����ݷ�װ��List��
	private List<Map<String, String>> getListFromRS(ResultSet rs) throws SQLException {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		ResultSetMetaData rsmd = rs.getMetaData();
		while (rs.next()) {
			Map<String, String> m = new HashMap<String, String>();
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				String colName = rsmd.getColumnLabel(i);
				String s = rs.getString(colName);
				if (s != null) {
					m.put(colName, s);
				}
			}
			list.add(m);
		}
		return list;
	}

	// ��ѯ�����ҳʱ�����ط�ҳ��Ϣ��Map��������ҳ����ÿҳ��¼���͵�ǰҳ�еļ�¼��
	public Map<String, Object> getPage(String sql, String[] params, String curPage) {
		Map<String, Object> page = new HashMap<String, Object>();
		String newSql = sql + " limit " + (Long.parseLong(curPage) - 1) * PAGE_REC_NUM + "," + PAGE_REC_NUM;
		List<Map<String, String>> pageList = getList(newSql, params);
		sql = sql.toLowerCase();
		String countSql = "";
		if (sql.indexOf("group") >= 0) {
			countSql = "select count(*) as tempNum from (" + sql + ") as temp";
		} else {
			countSql = "select count(*) as tempNum " + sql.substring(sql.indexOf("from"));
		}
		String count_s = (String) getMap(countSql, params).get("tempNum");
		long count = Long.parseLong(count_s);
		long totalPage = 0;
		if (count % PAGE_REC_NUM == 0)
			totalPage = count / PAGE_REC_NUM;
		else
			totalPage = count / PAGE_REC_NUM + 1;
		page.put("list", pageList);
		page.put("totalPage", totalPage);
		page.put("recNum", PAGE_REC_NUM);
		return page;
	}
	public int checkIdent(String sql, String username) {
		int result = 0;
		init();
		//String sql=
		try {
			pstmt = con.prepareStatement(sql);
			try {
				//foodname,feature, material,price,type,picture,comment
				pstmt.setString( 1, username);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()){
				result=rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	}

	public Map<String, Object> getPage(String sql, String curPage) {
		return getPage(sql, null, curPage);
	}
}