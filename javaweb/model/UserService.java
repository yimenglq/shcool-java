package model;

import java.util.List;
import java.util.Map;
import util.DBUtil;

public class UserService {

	private DBUtil db;

	public UserService() {
		db = new DBUtil();
	}

	public Map<String, String> login(String username, String password) {
		String sql = "select * from user where username=? and password=?";
		return db.getMap(sql, new String[] { username, password });
	}
	public int register(String username,String password,String telephone,String address){
		String sql = "insert into user(username,password,telephone,address) value(?,?,?,?)";
		return db.update(sql,new String[]{username,password,telephone,address});
	}
	public Map<String,String> registerName(String username){
		String sql ="select * from user where username=?";
		return db.getMap(sql,new String[]{username});
	}
	public List<Map<String,String>> getUsers( String username){
		String sql = "select * from user";
		String[] params = null;
		if (username != null) {
			sql = sql + " where username like?";
			params = new String[]{"%"+username+"%"};
		}
		return db.getList(sql,params);
	}
	public int deluser(String id){
		String sql = "delete from user where id = ?";
		return db.update(sql,new String[]{id});
	}
	public int delUser(String id){
		String[] params = { id };
		String sql= "delete from diningcar where user id=?";
		db.update(sql,params);
		sql = "delete from user where id=?";
		return db.update(sql,params);
	}
	public List<Map<String,String>> getUsers(){
		String sql = "select * from user";

		return db.getList(sql);
	}

    public int checkIdent(String un) {
		String sql = "select ident from user where username = ?";
		return db.checkIdent(sql,un);
    }
	public int laike(String userid,String foodid){
		String sqlc = "select * from laike where userid = ? and foodid = ?";

		if(db.getMap(sqlc,new String[]{userid,foodid}) == null) {
			String sql = "insert into laike(userid,foodid) value(?,?)";
			return db.update(sql,new String[]{userid,foodid});
		}else{
			return 0;
		}

	}

	public List<Map<String, String>> kViewlaike(String userid) {
		String sql ="select * from View_laike where userid = ?";
		return db.getList(sql,new String[]{userid});
	}
}
