package com.example.demo.repository;

import com.example.demo.model.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Integer> {
    List<Friend> findAllByUserId(int id);

    @Query(value = "select friend from Friend friend where user_id = :id or  friend_id = :id")
    List<Friend> serchAllFriends(@Param(value = "id") int id);

    Friend findByFriendIdAndUserId(int friendId, int userId);

    Friend findByUserIdAndFriendId(int userId, int friendId);

    @Query(value = "select * from friend where (friend_id=:fId and user_id=:uId) or (friend_id=:uId and user_id=:fId)", nativeQuery = true)
    List<Friend> customGetFriend(@Param(value = "fId") int friendId, @Param(value = "uId") int userId);

}
