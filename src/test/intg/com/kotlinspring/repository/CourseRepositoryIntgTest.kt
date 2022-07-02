package com.kotlinspring.repository

import com.springkotlin.util.courseEntityList
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import java.util.stream.Stream

@DataJpaTest
@ActiveProfiles("test")
class CourseRepositoryIntgTest {
    @Autowired
    lateinit var courseRepository: CourseRepository
    @BeforeEach
   fun setUp() {
       courseRepository.deleteAll()
       courseRepository.saveAll(courseEntityList())
    }

    @Test
    fun findByNameContaining() {
        val courses = courseRepository.findByNameContaining("SpringBoot")
        println("courses: $courses")
        Assertions.assertEquals(2,courses.size)

    }
    @Test
    fun findByNameByCourseData() {
        val courses = courseRepository.findCourseByName("SpringBoot")
        println("courses: $courses")
        Assertions.assertEquals(2,courses.size)

    }
    @ParameterizedTest
    @MethodSource("courseAndSize")
    fun findByName_ParameterizedTest(name:String, expectedSize:Int) {
        val courses = courseRepository.findCourseByName(name)
        println("courses: $courses")
        Assertions.assertEquals(expectedSize,courses.size)

    }

    companion object {
        @JvmStatic
        fun courseAndSize(): Stream<Arguments> {
        return Stream.of(Arguments.arguments("SpringBoot",2), Arguments.arguments("Wiremock",1))
        }
    }


}