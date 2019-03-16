package devsunset.simple.random.chat.modules.dataservice;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(version = 1, entities = {AppTalkMain.class, AppTalkThread.class})
@TypeConverters({Converters.class})
public abstract class AppDataBase extends RoomDatabase {

    // AppTalkMainDao is a class annotated with @Dao.
    abstract public AppTalkMainDao AppTalkMainDao();

    // AppTalkThreadDao is a class annotated with @Dao.
    abstract public AppTalkThreadDao AppTalkThreadDao();
}