package com.example.services.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "job_loccations")
public class JobLocationsDetails {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    public var job_lat : String? = null
    public var job_long : String? = null
    public var job_id : String? = null
    public var status : String? = null

}