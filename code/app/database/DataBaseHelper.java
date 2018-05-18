import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Created by lawalong on 11/05/2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    private static String DB_PATH = "your path"; // e.g /data/data/com.xxx.xxx/databases/

    private static String DB_NAME = "xxx.db";

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    private static final String DICTIONARY_TABLE_CREATE =

            "CREATE TABLE product (pid integer primary key autoincrement,name varchar(64),address varchar(64)) ";

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    private static int DBVERSION = 1;

    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, DBVERSION);
        this.myContext = context;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
            System.out.println("==DB==>>exist");
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("==DB==>>Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time user open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }
        System.out.println("==DB==>>Checkexist==>"+checkDB);
        return checkDB != null ? true : false;
    }

    /**
     * Copies database from local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase()throws IOException{
        try {
            //Open your local db as the input stream
            InputStream myInput = myContext.getAssets().open(DB_NAME);

            // Path to the just created empty db
            String outFileName = DB_PATH + DB_NAME;

            //Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);

            //transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            //Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
            System.out.println("==DB==>>Copy success");
        }
        catch(IOException e){System.out.println("==DB==>>Copy faild" + e);}

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        System.out.println("==DB==>>DB opened");

    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();
        System.out.println("==DB==>>DB Closed");
        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.

}