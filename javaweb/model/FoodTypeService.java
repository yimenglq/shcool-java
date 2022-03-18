package model;

import java.util.List;
import java.util.Map;
import util.DBUtil;

public class FoodTypeService {

	private DBUtil db;
	
	public FoodTypeService(){
		db = new DBUtil();
	}
	
	public List<Map<String,String>> getAllTypes(){
		 String sql ="select * from foodtype";
		 return db.getList(sql);
	}
	public List<Map<String,String>> getfoodTypef(String tyupe ){
		if (tyupe != null){
			String sql1 = "insert into foodtype(typename) value(?)";
			db.update(sql1,new String[]{tyupe});
		}
		return null;
	}
	public List<Map<String,String>> getfoodType(){
		String sql = "SELECT * FROM foodtype ";


		return db.getList(sql);


	}
	public int updata(String id,String name){

		if (name !=null){
		String sql = "update foodtype set typename = ? where id = ?";
		return db.update(sql,new String[]{name,id});
		}
		return 0;
	}
	public int deltype(String id){
		String[] params = { id };
		String sql = "delete from foodtype where id=?";

		return db.update(sql,params);

	}
}
