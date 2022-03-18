//package dao;
//
//import org.junit.Test;
//import util.DBUtil;
//
//import java.sql.CallableStatement;
//import java.sql.Types;
//import java.util.ArrayList;
//public class CallPRO extends DBUtil{
//
//    /**
//     * CREATE DEFINER=`root`@`localhost` PROCEDURE `selectFood`(fid int,ftype int,OUT fname varchar(20) )
//     BEGIN
//     set fname=(select foodname from food where id=fid and type=ftype);
//
//     END
//     */
//    public CallPRO(){
//        super();
//        init();
//    }
//
//
//
//    public void testProcedure( String sql ,ArrayList<Integer> p){
//
//        try {
//
//            //通过连接创建出statement
//            CallableStatement call = con.prepareCall(sql);
//            //对于in参数，赋值
//            call.setInt(1, p.get(0));  // (第几个问号,要赋的值)
//            call.setInt(2, p.get(1));  // (第几个问号,要赋的值)
//            //对out参数，声明
//            call.registerOutParameter(3, Types.VARCHAR);
//
//            //执行调用
//            call.execute();
//
//            //取出结果
//            String userName = call.getString(3);
//
//
//            System.out.println("用户姓名："+userName);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally{
//            //关闭连接，释放资源
//            close();
//        }
//
//    }
//
//    @Test
//    public void test(){
//        CallPRO test=new CallPRO();
//        ArrayList<Integer> p=new ArrayList<Integer>();
//        p.add(0, 1);p.add(1,3);
//        String sql = "{call selectFood(?,?,?)}";
//        test.testProcedure(sql,p);
//
//
//    }
//
//}