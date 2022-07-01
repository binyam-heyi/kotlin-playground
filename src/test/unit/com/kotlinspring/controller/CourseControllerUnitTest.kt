package com.kotlinspring.controller

import com.kotlinspring.service.CourseService
import com.ninjasquad.springmockk.MockkBean
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

}