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
//            //ͨ�����Ӵ�����statement
//            CallableStatement call = con.prepareCall(sql);
//            //����in��������ֵ
//            call.setInt(1, p.get(0));  // (�ڼ����ʺ�,Ҫ����ֵ)
//            call.setInt(2, p.get(1));  // (�ڼ����ʺ�,Ҫ����ֵ)
//            //��out����������
//            call.registerOutParameter(3, Types.VARCHAR);
//
//            //ִ�е���
//            call.execute();
//
//            //ȡ�����
//            String userName = call.getString(3);
//
//
//            System.out.println("�û�������"+userName);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally{
//            //�ر����ӣ��ͷ���Դ
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