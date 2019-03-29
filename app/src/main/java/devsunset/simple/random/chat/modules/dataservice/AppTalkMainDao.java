/*
 * @(#)AppTalkMainDao.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat.modules.dataservice;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * <PRE>
 * SimpleRandomChat AppTalkMainDao
 * </PRE>
 *
 * @author devsunset
 * @version 1.0
 * @since SimpleRandomChat 1.0
 */

@Dao
public interface AppTalkMainDao {

    @Query("SELECT * FROM APP_TALK_MAIN WHERE ATX_STATUS NOT IN ('F','H') ORDER BY ATX_LOCAL_TIME DESC")
    List<AppTalkMain> getAll();

    @Query("SELECT * FROM APP_TALK_MAIN WHERE ATX_ID = :atxId")
    List<AppTalkMain> findByAtxId(String atxId);

    @Insert
    void insert(AppTalkMain appTalkMain);

    @Delete
    void delete(AppTalkMain appTalkMain);

    @Query("UPDATE APP_TALK_MAIN SET ATX_STATUS = :atxStatus, ATX_LOCAL_TIME= :atxLocalTime  WHERE ATX_ID =:atxId")
    void updateStatus(String atxStatus, String atxLocalTime, String atxId);

    @Query("UPDATE APP_TALK_MAIN SET ATX_STATUS = :atxStatus, ATX_LOCAL_TIME= :atxLocalTime " +
            ", TALK_APP_ID =:talkAppId ,TALK_TEXT= :talkText ,TALK_TYPE= :talkType WHERE ATX_ID =:atxId")
    void updateReplySend(String atxStatus, String atxLocalTime, String talkAppId, String talkText,String talkType, String atxId);

}