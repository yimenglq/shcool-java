package dao;

public interface FoodDao {
    int foodDelete(int id);
    int updateFood(String sql, String[] params);

}
