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

    @Query("SELECT * FROM APP_TALK_MAIN ORDER BY ATX_LOCAL_TIME DESC")
    List<AppTalkMain> getAll();

    @Query("SELECT * FROM APP_TALK_MAIN WHERE ATX_ID = :atxId")
    List<AppTalkMain> findByAtxId(String atxId);

    @Insert
    void insert(AppTalkMain appTalkMain);

    @Delete
    void delete(AppTalkMain appTalkMain);

    // Example

    //@Query("SELECT * FROM APP_TALK_MAIN WHERE ATX_ID IN (:atxIds)")
    //List<AppTalkMain> findByAtxIds(int[] atxIds);

    //@Query("SELECT * FROM APP_TALK_MAIN WHERE first_name LIKE :first AND " + "last_name LIKE :last LIMIT 1")
    //User findByName(String first, String last);

    //@Query("SELECT * FROM book " + "INNER JOIN loan ON loan.book_id = book.id " + "INNER JOIN user ON user.id = loan.user_id " + "WHERE user.name LIKE :userName")
    //public List<Book> findBooksBorrowedByNameSync(String userName);

}