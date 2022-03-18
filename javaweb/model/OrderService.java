package model;

import util.DBUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderService {
    private DBUtil db;

    public OrderService() {
        db = new DBUtil();
    }

    public void Order(String userid , String[] shuliang , String[] ids){
       String sql;
       int i = 0;
       String id;
        int moeny = 0;
       if (ids != null || ids.length > 0 ){

           for (String food : ids){
               sql = "select * from food where id = ?";
               Map<String,String> fd = db.getMap(sql,new String[]{food});
               moeny = Integer.parseInt(shuliang[i])*Integer.parseInt(fd.get("price"));
               i++;
           }
           i = 0;
           sql = "insert into orders(userid,money) values(?,?)";
           if(db.update(sql,new String[]{userid, String.valueOf(moeny)})>0){
               sql = "select * from orders where item = 0 and userid = ?";
               Map<String,String> order = db.getMap(sql,new String[]{userid});
               id = order.get("id");
               if (id!=null){
                   for (String food : ids ){
                       sql = "insert into orderitem(orderid,foodid,shuliang) values(?,?,?)";
                       db.update(sql,new String[]{id,food,shuliang[i]});
                       i++;
                   }
                   sql = "select * from orderitem where orderid = ?";
                   List<Map<String,String>> list = db.getList(sql,new String[]{id});
                   if(list.size() == ids.length){
                       sql = "update orders set item = 1 where id = ? and userid = ?";
                       db.update(sql,new String[]{id,userid});
                   }
               }
           }

       }

    }

    public List<Map<String,String>> getorder(String userid) {
       String sql = "SELECT * FROM orderitem,orders,food WHERE orderitem.foodid = food.id and orderitem.orderid = orders.id AND userid = ?";

       return db.getList(sql,new String[]{userid});
    }
    public Map<String,List<Map<String,String>>> showOrders(String userid){
        Map<String,List<Map<String,String>>> dcs = new HashMap<String,List<Map<String,String>>>();
        //读取系统所有点餐用户的id和name
        String sql = "SELECT * FROM orders WHERE userid = ?";
        List<Map<String,String>> orders = db.getList(sql,new String[]{userid});
        for(Map<String,String> m : orders) {
            dcs.put(m.get("id"), getorder(userid));
        }
        return dcs;
    }
}
