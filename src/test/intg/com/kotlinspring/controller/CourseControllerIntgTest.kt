package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.entity.Course
import com.kotlinspring.repository.CourseRepository
import com.kotlinspring.repository.InstructorRepository
import com.springkotlin.util.courseEntityList
import com.springkotlin.util.instructorEntity
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriComponentsBuilder

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class CourseControllerIntgTest {
    @Autowired
    lateinit var webTestClient: WebTestClient
    @Autowired
    lateinit var courseRepository: CourseRepository
    @Autowired
    lateinit var instructorRepository: InstructorRepository

    @BeforeEach
    internal fun setUp() {
        courseRepository.deleteAll()
        var instructor = instructorEntity()
        instructorRepository.save(instructor)
        var courses = courseEntityList(instructor)
        courseRepository.saveAll(courses)
    }

    @Test
    fun addCourse() {
        val courseDto = CourseDTO(
            null,
            "Test Course",
            "Test Class",
            instructorRepository.findAll().first().id
                )
        val savedCourseDTO = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDto)
            .exchange()
            .expectStatus().isCreated
            .expectBody(courseDto::class.java)
            .returnResult()
            .responseBody

        Assertions.assertTrue {
            savedCourseDTO!!.id != null
        }

    }

    @Test
     fun retrieveAllCourses() {
        val courseDTOs=webTestClient.get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody
        println("couseDTOs, $courseDTOs")
        assertEquals(3,courseDTOs!!.size)
    }
    @Test
    fun retrieveAllCoursesByName() {
        val uri= UriComponentsBuilder.fromUriString("/v1/courses")
            .queryParam("course_name","SpringBoot")
            .toUriString();
        val courseDTOs=webTestClient.get()
            .uri(uri)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody
        println("couseDTOs, $courseDTOs")
        assertEquals(2,courseDTOs!!.size)
    }

    @Test
    fun updateCourse() {
       val course= Course(null,
            "Build RestFul APis using SpringBoot and Kotlin", "Development",instructorRepository.findAll().first())
        courseRepository.save(course)
        val courseDTO=   CourseDTO(null,
            "Build RestFul APis using SpringBoot and Kotlin for Test", "Development",instructorRepository.findAll().first().id)
        val updatedCourseDTO = webTestClient
            .put()
            .uri("/v1/courses/{courseId}", course.id)
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody
        assertEquals("Build RestFul APis using SpringBoot and Kotlin for Test",updatedCourseDTO!!.name)
    }
    @Test
    fun deleteteCourse() {
        val course= Course(null,
            "Build RestFul APis using SpringBoot and Kotlin", "Development", instructorRepository.findAll().first())
        courseRepository.save(course)
        val courseDTO=   CourseDTO(null,
            "Build RestFul APis using SpringBoot and Kotlin for Test", "Development")
        val updatedCourseDTO = webTestClient
            .delete()
            .uri("/v1/courses/{courseId}", course.id)
            .exchange()
            .expectStatus().isNoContent


    }
}