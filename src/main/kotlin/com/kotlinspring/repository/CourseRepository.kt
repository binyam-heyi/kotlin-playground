package com.kotlinspring.repository

import com.kotlinspring.entity.Course
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface CourseRepository : CrudRepository<Course, Int>{
    fun findByNameContaining(coursName:String):List<Course>
    @Query(value="SELECT * FROM COURSES WHERE name LIKE %?1%", nativeQuery=true)
    fun findCourseByName(courseName: String):List<Course>
}