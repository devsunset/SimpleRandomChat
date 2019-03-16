package devsunset.simple.random.chat.modules.dataservice;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;


@Dao
public interface AppTalkMainDao {

    @Query("SELECT * FROM APP_TALK_MAIN")
    List<AppTalkMain> getAll();

    @Query("SELECT * FROM APP_TALK_MAIN WHERE ATX_ID IN (:atxIds)")
    List<AppTalkMain> findByAtxIds(int[] atxIds);

    @Insert
    void insert(AppTalkMain appTalkMain);

    @Delete
    void delete(AppTalkMain appTalkMain);


    // Example

    //@Query("SELECT * FROM APP_TALK_MAIN WHERE first_name LIKE :first AND " + "last_name LIKE :last LIMIT 1")
    //User findByName(String first, String last);

    //@Query("SELECT * FROM book " + "INNER JOIN loan ON loan.book_id = book.id " + "INNER JOIN user ON user.id = loan.user_id " + "WHERE user.name LIKE :userName")
    //public List<Book> findBooksBorrowedByNameSync(String userName);

}