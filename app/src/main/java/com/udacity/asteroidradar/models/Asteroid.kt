package com.udacity.asteroidradar.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "asteroid")
data class Asteroid(
        //TODO: rinomina il table name e le column info con una costante

        @PrimaryKey
        val id: Long,

        @ColumnInfo(name= "codename")
        val codename: String,

        @ColumnInfo(name= "close_approach_date")
        val closeApproachDate: String,

        @ColumnInfo(name= "absolute_magnitude")
        val absoluteMagnitude: Double,

        @ColumnInfo(name= "estimated_diameter")
        val estimatedDiameter: Double,

        @ColumnInfo(name= "relative_velocity")
        val relativeVelocity: Double,

        @ColumnInfo(name= "distance_from_earth")
        val distanceFromEarth: Double,

        @ColumnInfo(name= "is_potentially_hazardous")
        val isPotentiallyHazardous: Boolean

) : Parcelable