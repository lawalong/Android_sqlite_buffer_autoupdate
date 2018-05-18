import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lawalong on 11/05/2015.
 */
public class DBManager {
    private DataBaseHelper helper;
    public DBManager(Context context){
        helper = new DataBaseHelper(context);
    }

    public String GetDbVersion(){
        String version="";
        try{
            SQLiteDatabase database = helper.getWritableDatabase();

            Cursor cursor = database.rawQuery("select max(version) from version_control",null);
            while (cursor.moveToNext()){
               version = cursor.getString(cursor.getColumnIndex("max(version)"));
            }

        }catch(Exception e){}
        return version;
    }

    public boolean updateBySQL(String sql,Object[] bindArgs){
        boolean flag = false;
        try{
            SQLiteDatabase database = helper.getReadableDatabase();
            database.execSQL(sql,bindArgs);
            flag = true;
        }catch(Exception e){System.out.println("***********Update**Exception***********"+e);}
        return flag;
    }

    public void close(){
        helper.close();
    }

}
