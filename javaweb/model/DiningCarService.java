package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import util.DBUtil;

public class DiningCarService {

	private DBUtil db;

	public DiningCarService(){
		db = new DBUtil();
	}

	//加入点餐车
	public int addToDC(String userid, String[] ids, String[] shuliang  ){
		int r = 0,i=0;
		String sql;
		if(ids != null){

				for (String foodid:ids) {
					sql = "select * from diningcar where userid = ? and foodid = ?";
					System.out.println("sql遍历的foodid"+foodid);
					List<Map<String, String>> list = db.getList(sql, new String[]{userid, foodid});
					if( list == null || list.size() == 0){
						sql = "insert into diningcar(id,userid,foodid,shuliang) values(null,?,?,?)";
						r+=db.update(sql, new String[]{userid,foodid,shuliang[i]});
						i++;
					}else {
						String id = list.get(0).get("id");
						System.out.println(id);
//						String shulian = list.get("shuliang");
						sql = "update diningcar set shuliang =shuliang + ?  where id = ?";
						r+=db.update(sql,new String[]{shuliang[i],id});
						i++;
					}
				}



//			for( i=0;i<ids.length;i++){


			sql = "update food set hits=hits+1 where id=?";
			for(String id : ids){
				db.update(sql, new String[]{id});
			}
		}
		return r;
	}
	public int addToDCOne(String userid, String[] ids){
		int r = 0;
		if(ids != null){
			String sql = "insert into diningcar values(null,?,?,?)";
			for(String foodid : ids){
				r=db.update(sql, new String[]{userid,ids[0],ids[1]});
			}
			sql = "update food set hits=hits+1 where id=?";
			{
				db.update(sql, new String[]{ids[0]});
			}
		}
		return r;
	}
	//读取用户点餐信息
	public List<Map<String,String>> showDC(String userid){
		String sql = "select f.*,ft.typename,dc.id as dcid,dc.shuliang from food f join foodtype ft on f.type = ft.id join diningcar dc on f.id = dc.foodid where dc.userid = ? order by dcid desc";
		return db.getList(sql, new String[]{userid});
	}

	//从点餐车删除
	public int delFromDC(String[] ids){
		int r =0;
		if(ids != null){
			String sql="update food set hits = hits-1 where id =(select foodid from diningcar where id=?)";
			for(String dcid:ids){
				//更新点餐率
				db.update(sql, new String[]{dcid});
			}
			sql="delete from diningcar where id=?";
			for(String id:ids){
				//从点餐车删除
				r+=db.update(sql, new String[]{id});
			}
		}
		return r;
	}

	//读取所有用户点餐信息
	public Map<String,List<Map<String,String>>> showAllDC(){
		Map<String,List<Map<String,String>>>
				dcs = new HashMap<String,List<Map<String,String>>>();
		//读取系统所有点餐用户的id和name
		String sql = "select distinct u.id,u.username from user u join diningcar dc on u.id = dc.userid";
		List<Map<String,String>> users = db.getList(sql);
		for(Map<String,String> m : users) {
			dcs.put(m.get("username"), showDC(m.get("id")));
		}
		return dcs;
	}
}
