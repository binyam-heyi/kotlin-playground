package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.service.CourseService
import com.ninjasquad.springmockk.MockkBean
import com.springkotlin.util.courseDTO
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(controllers = [CourseController::class])
@AutoConfigureWebTestClient
class CourseControllerUnitTest {
    @Autowired
    lateinit var webTestClient: WebTestClient
    @MockkBean
    lateinit var courseService: CourseService

    @Test
    fun addCourse() {
        val courseDto = CourseDTO(
            null,
            "Build RestFul APis using Spring Boot and Kotlin",
            "Development"
        )

        every {
            courseService.addCourse(any()) } returns courseDTO(id=1)

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
    fun retrieveCourses() {
        every { courseService.retrieveAllCourses(any()) }.returnsMany (
                listOf(courseDTO(id=1), courseDTO(id=2), courseDTO(id=3))
                )


        val courseDTOs=webTestClient.get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody
        println("couseDTOs, $courseDTOs")
        Assertions.assertEquals(3, courseDTOs!!.size)
    }

    @Test
    fun updateCourse() {

        val courseDTO=   CourseDTO(101,
            "Build RestFul APis using SpringBoot and Kotlin for Test", "Development")
            every { courseService.updateCourse(any(), any()) } returns courseDTO
        val updatedCourseDTO = webTestClient
            .put()
            .uri("/v1/courses/{courseId}", courseDTO.id)
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody
        Assertions.assertEquals("Build RestFul APis using SpringBoot and Kotlin for Test", updatedCourseDTO!!.name)
    }
    @Test
    fun deleteteCourse() {
       every { courseService.deleteCourse(any()) } just runs
        val updatedCourseDTO = webTestClient
            .delete()
            .uri("/v1/courses/{courseId}", 1119)
            .exchange()
            .expectStatus().isNoContent


    }
    @Test
    fun addCourse_validation() {
        val courseDto = CourseDTO(
            null,
            " ",
            " "
        )

        every {
            courseService.addCourse(any()) } returns courseDTO(id=1)

        val response = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDto)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals("courseDTO.category must not be blank, courseDTO.name must not be blank", response)

    }
    @Test
    fun addCourse_allException() {
        val courseDto = CourseDTO(
            null,
            "Test",
            "Test Category "
        )

        every {
            courseService.addCourse(any()) } throws  RuntimeException("Unexpected Error Happens")

        val response = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDto)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody



    }

}