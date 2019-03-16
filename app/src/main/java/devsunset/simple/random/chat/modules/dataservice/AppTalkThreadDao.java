package devsunset.simple.random.chat.modules.dataservice;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface AppTalkThreadDao {

    @Query("SELECT * FROM APP_TALK_THREAD")
    List<AppTalkThread> getAll();

    @Query("SELECT * FROM APP_TALK_THREAD WHERE ATX_ID IN (:atxIds)")
    List<AppTalkThread> findByAtxIds(int[] atxIds);

    @Insert
    void insert(AppTalkThread appTalkThread);

    @Delete
    void delete(AppTalkThread appTalkThread);

}