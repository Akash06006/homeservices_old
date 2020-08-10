package com.example.services.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "job_details")
public class JobDetails {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    public var user_id : String? = null
    public var job_id : String? = null
    public var complete_status : String? = null
    public var start_date : String? = null
    public var end_date : String? = null
    public var status : String? = null

}