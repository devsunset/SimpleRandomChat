/*
 * @(#)AppTalkThreadDao.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat.modules.dataservice;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * <PRE>
 * SimpleRandomChat AppTalkThreadDao
 * </PRE>
 *
 * @author devsunset
 * @version 1.0
 * @since SimpleRandomChat 1.0
 */
@Dao
public interface AppTalkThreadDao {

    @Query("SELECT * FROM APP_TALK_THREAD")
    List<AppTalkThread> getAll();

    @Query("SELECT * FROM APP_TALK_THREAD WHERE ATX_ID = :atxId")
    List<AppTalkThread> findByAtxId(String atxId);

    @Query("SELECT * FROM APP_TALK_THREAD WHERE TALK_ID = :talkId")
    List<AppTalkThread> findByTalkId(String talkId);

    @Insert
    void insert(AppTalkThread appTalkThread);

    @Query("DELETE FROM APP_TALK_THREAD WHERE ATX_ID =:atxId")
    void deleteByAtxId(String atxId);

    @Query("DELETE fROM APP_TALK_THREAD WHERE ATX_ID IN ( SELECT ATX_ID FROM APP_TALK_MAIN WHERE ATX_STATUS = :atxStatus AND ATX_LOCAL_TIME  < :cttm )")
    void deleteByCttm(String atxStatus , String cttm);
}