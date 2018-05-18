import android.app.Activity;

import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;


import com.android.volley.RequestQueue;

/**
 * Created by lawalong on 11/05/2015.
 */
 
/** Welcome Page */
public class MainActivity extends Activity {

    private RequestQueue requestQueue;
	private REQUEST_UPDATE_URL = getResources().getString(R.string.PASS_UPDATE);
        
	@Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        // Full screen show welcome page
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.start);
			
        /**
         * init DataBase
         */
        CopyDB();

        /**
         * Get update data from server
         */
		UpdateDB()
		
        /**
         * Your other stuff
         */

    }
		
	public void UpdateDB(){
			requestQueue = Volley.newRequestQueue(getApplicationContext());
            System.out.println("*************Step 1**************");
            Thread t = new Thread(){
                public void run(){

                    final DBManager manager = new DBManager(getApplicationContext());
                    String version = manager.GetDbVersion();
                    String requeseUpdateUrl = REQUEST_UPDATE_URL; // your update server API url
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("current_version",version);
                    System.out.println("*************Step 2**************"+version);
                    CustomRequest jsonObjectRequest = new CustomRequest(Request.Method.POST, requeseUpdateUrl,map, new Response.Listener<JSONObject>(){
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("*************Step 3**************"+response);
                            try{
                                JSONArray updates = response.getJSONArray("u");
                                int lv = response.getInt("lv");
                                ArrayList<Bean> newProducts = new ArrayList<Bean>();// Bean is your data model
                                for(int i=0;i<updates.length();i++){
                                    JSONObject update = updates.getJSONObject(i);
                                    String DB_COLUMN_1 = update.getString("DB_COLUMN_1");
                                    String DB_COLUMN_2 = update.getString("DB_COLUMN_2");
                                    String DB_COLUMN_3 = update.getString("DB_COLUMN_3");
									// ...

                                    newProducts.add(new Bean(DB_COLUMN_1, DB_COLUMN_2, DB_COLUMN_3)); 
                                    /**
                                     * update local database (two tables)
                                     * product + version_control
                                     */
                                    String SQL_INSET_OR_UPDATE_ITEM ="INSERT OR IGNORE INTO product VALUES (?,?,?)"; // example
                                    String SQL_UPDATE_PRICE ="UPDATE product SET price = ? WHERE id_product LIKE ?"; // example
                                    String SQL_UPDATE_ACTICITY ="UPDATE product SET activity = ? WHERE id_product LIKE ?"; // example

                                    Object[] bindArgs1 = {id_product,productcode,name,price,imgUrl,activity};
                                    Object[] bindArgs2 = {price,id_product};
                                    Object[] bindArgs3 = {activity,id_product};
                                    System.out.println("*************Step 9**************" + manager.updateBySQL(SQL_INSET_OR_UPDATE_ITEM,bindArgs1));
                                    System.out.println("*************Step 10**************" + manager.updateBySQL(SQL_UPDATE_PRICE,bindArgs2));
                                    System.out.println("*************Step 10**************" + manager.updateBySQL(SQL_UPDATE_ACTICITY,bindArgs3));
                                }
								
                                String SQL_INSERT_VERSION = "insert into version_control (version) values (?)";
                                Object[] bindArgs3 = {lv + ""};
                                System.out.println("*************Step 11**************" + manager.updateBySQL(SQL_INSERT_VERSION,bindArgs3));

                                System.out.println("*************Step 4**************" + newProducts.get(0).getId());
                            }catch (JSONException e){e.printStackTrace();System.out.println("*************5**************");}
                        }
                    },new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("*************Step 6**************" + error);
                        }
                    });
                    requestQueue.add(jsonObjectRequest);
                    System.out.println("*************Step 7**************");
                }
            };
            t.start();
            System.out.println("*************Step 8**************");
	}
		
	public void CopyDB(){
		DataBaseHelper db = new DataBaseHelper(getApplicationContext());

		try {
			db.createDataBase();
		}catch(Exception e){throw new Error("Unable to create database");}
	}
}