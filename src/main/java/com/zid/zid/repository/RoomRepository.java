package com.zid.zid.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.zid.zid.entity.Room;

@Repository
public  interface RoomRepository extends MongoRepository<Room, String> {
    
}
