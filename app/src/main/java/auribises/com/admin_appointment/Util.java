package auribises.com.admin_appointment;

import android.net.Uri;

public class Util {

    // Information for my Database
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "adminappointment.db";

    // Information for my Table
    public static final String TAB_NAME = "adminappointment";
    public static final String COL_ID = "_ID";
    public static final String COL_NAME = "NAME";
    public static final String COL_PHONE = "PHONE";
    public static final String COL_EMAIL = "EMAIL";
    public static final String COL_GENDER = "GENDER";
    public static final String COL_PURPOSE = "PURPOSE";
    public static final String COL_DATE = "DATE";
    public static final String COL_TIME = "TIME";
    public static final String COL_ROOM = "ROOM";

    public static final String CREATE_TAB_QUERY = "create table adminappointment(" +
            "_ID integer primary key autoincrement," +
            "NAME varchar(256)," +
            "PHONE varchar(256)," +
            "EMAIL varchar(256)," +
            "GENDER varchar(256)," +
            "PURPOSE varchar(256)," +
            "DATE varchar(256)," +
            "TIME varchar(256)," +
            "ROOM varchar(256)" +
            ")";


    public static final String PREFS_NAME = "visitorbook";
    public static final String KEY_NAME = "keyName";
    public static final String KEY_PHONE = "keyPhone";
    public static final String KEY_EMAIL = "keyEmail";
    public static final String KEY_GENDER = "keyGender";
    public static final String KEY_PURPOSE = "keyPurpose";
    public static final String KEY_DATE = "keydate";
    public static final String KEY_TIME = "keytime";
    public static final String KEY_room = "keyroom";

    // URI
    public static final Uri ADMIN_APPOINTMENT_URI = Uri.parse("content://com.auribises.adminappointment.teacherprovider/"+TAB_NAME);


    final static String URI = "http://tajinderj.esy.es/admin2017/";
    // URL
    public static final String INSERT_ADMIN_APPOINTMENT_TPHP = "http://tajinderj.esy.es/admin2017/insert.php";

    public static final String RETRIEVE_ADMIN_APPOINTMENT_PHP = "http://tajinderj.esy.es/admin2017/retrieve.php";

    public static final String DELETE_ADMIN_APPOINTMENT_PHP = "http://tajinderj.esy.es/admin2017/delete.php";

    public static final String UPDATE_ADMIN_APPOINTMENT_PHP = "http://tajinderj.esy.es/admin2017/update.php";
}
