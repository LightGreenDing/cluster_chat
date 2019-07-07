package com.wt.wtchat.dao;

import com.wt.wtchat.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 房间Dao
 *
 * @author Zed
 */
@Repository
public interface RoomDao extends JpaRepository<Room, Integer> {
}
