package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Adiacenza;
import it.polito.tdp.food.model.Condiment;

public class FoodDAO {
	
	Map<Integer, Condiment> condimenti = new HashMap<>();

	public List<Food> listAllFood(){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Food(res.getInt("food_id"),
							res.getInt("food_code"),
							res.getString("display_name"), 
							res.getInt("portion_default"), 
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"),
							res.getDouble("calories")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Condiment> listAllCondiment(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					Condiment c = new Condiment(res.getInt("condiment_id"),
							res.getInt("food_code"),
							res.getString("display_name"), 
							res.getString("condiment_portion_size"), 
							res.getDouble("condiment_calories")
							);
					list.add(c);
					condimenti.put(res.getInt("condiment_id"), c);
					
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Condiment> vertici(double calorie){
		this.listAllCondiment();
		String sql = "SELECT distinct condiment_id from condiment where condiment_calories<?" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setDouble(1, calorie);
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(condimenti.get(res.getInt("condiment_id")));
							
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			Collections.sort(list);
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Adiacenza> archi(double calorie){
		this.listAllCondiment();
		String sql = "SELECT distinct c1.condiment_id as con1, c2.condiment_id as con2, count(distinct f1.food_code) as cnt from food_condiment as f1, food_condiment as f2, condiment as c1, condiment as c2 where f1.food_code=f2.food_code and c1.food_code=f1.condiment_food_code and c2.food_code=f2.condiment_food_code and c1.condiment_id>c2.condiment_id and c1.condiment_calories<? and c2.condiment_calories<? group by c1.condiment_id, c2.condiment_id " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setDouble(1, calorie);
			st.setDouble(2, calorie);
			
			List<Adiacenza> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Adiacenza(condimenti.get(res.getInt("con1")), condimenti.get(res.getInt("con2")), res.getInt("cnt")));
							
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}

	}
	
	public int tot(double calorie, int id){
		int x = 0;
		String sql = "SELECT distinct c.condiment_id, count(distinct f.food_code) as cnt from condiment as c, food_condiment as f where c.condiment_calories<? and c.condiment_id=? and c.food_code=f.condiment_food_code group by c.condiment_id" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setDouble(1, calorie);
			st.setInt(2, id);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					x = res.getInt("cnt");
							
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return x ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1 ;
		}

	}

}

