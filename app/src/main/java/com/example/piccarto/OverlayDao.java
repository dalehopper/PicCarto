package com.example.piccarto;

import android.arch.lifecycle.LiveData;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface OverlayDao {
    @Query("SELECT * FROM overlays")
    List<Overlay> getAll();

    @Query("SELECT * FROM overlays WHERE owner LIKE :owner")
    List<Overlay> loadUserOverlays(String owner);

    @Query("SELECT * FROM overlays  where overlayID is :id")
    Overlay findById(int id);

    @Query("SELECT COUNT(overlayID) FROM overlays")
    int getCount();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Overlay overlay);

    @Delete
    void delete(Overlay overlay);
}
