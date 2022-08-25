/*
 * @(#)DatabaseClient.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat.modules.dataservice;

import android.arch.persistence.room.Room;
import android.content.Context;

/**
 * <PRE>
 * SimpleRandomChat DatabaseClient
 * </PRE>
 *
 * @author devsunset
 * @version 1.0
 * @since SimpleRandomChat 1.0
 */
public class DatabaseClient {

    private static DatabaseClient mInstance;
    private final Context mCtx;
    //our app database object
    private final AppDataBase appDataBase;

    private DatabaseClient(Context mCtx) {
        this.mCtx = mCtx;
        //creating the app database with Room database builder
        //MyToDos is the name of the database
        appDataBase = Room.databaseBuilder(mCtx, AppDataBase.class, "SRC_DB").build();
    }

    public static synchronized DatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }

    public AppDataBase getAppDataBase() {
        return appDataBase;
    }
}

/*
    ### Example Migration ###

    Room.databaseBuilder(getApplicationContext(), AppDataBase.class, "database-name") .addMigrations(MIGRATION_1_2, MIGRATION_2_3).build();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
	@Override
	public void migrate(SupportSQLiteDatabase database) {
		database.execSQL("CREATE TABLE `Fruit` (`id` INTEGER, " + "`name` TEXT, PRIMARY KEY(`id`))");
		}
	};

	static final Migration MIGRATION_2_3 = new Migration(2, 3) {
	@Override
	public void migrate(SupportSQLiteDatabase database) {
		database.execSQL("ALTER TABLE Fruit " + " ADD COLUMN pub_year INTEGER");
	} };

 */