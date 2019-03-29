/*
 * @(#)AppDataBase.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat.modules.dataservice;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

/**
 * <PRE>
 * SimpleRandomChat Converters
 * </PRE>
 *
 * @author devsunset
 * @version 1.0
 * @since SimpleRandomChat 1.0
 */

@Database(version = 1, exportSchema = false, entities = {AppTalkMain.class, AppTalkThread.class})
@TypeConverters({Converters.class})
public abstract class AppDataBase extends RoomDatabase {

    // AppTalkMainDao is a class annotated with @Dao.
    abstract public AppTalkMainDao AppTalkMainDao();

    // AppTalkThreadDao is a class annotated with @Dao.
    abstract public AppTalkThreadDao AppTalkThreadDao();
}