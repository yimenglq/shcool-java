package dao;

import java.sql.SQLException;
import util.DBUtil;

public class FoodDaoImp extends  DBUtil implements FoodDao {


    @Override
    public int foodDelete(int id) {
        // TODO 自动生成的方法存根
        return 0;
    }

    @Override
    public int updateFood(String sql, String[] params) {
        int result = 0;
        init();
        try {
            pstmt = con.prepareStatement(sql);
            try {
                //foodname,feature, material,price,type,picture,comment
                pstmt.setString( 1, params[0]);
                pstmt.setString( 2, params[1]);
                pstmt.setString( 3, params[2]);
                pstmt.setInt( 4, Integer.parseInt(params[3]));
                pstmt.setInt( 5, Integer.parseInt(params[4]));
                pstmt.setString( 6, params[5]);
                pstmt.setString( 7, params[6]);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return result;
    }

}
