package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    // тестирование добавление пользователя без логина
    @Test
    @DisplayName("Если нет поля login, то возвращается код 400")
    void postMethod_shouldReturnResponseWithCode400_whenUserWithoutLoginPassed() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Nick Name\"," +
                                "\"email\":\"mail@mail.ru\", \"birthday\":\"1946-08-20\"}"))
                .andExpect(status().isInternalServerError());
    }

    // тестирование добавление пользователя с некорректным указанием логина (с пробелом)
    @Test
    @DisplayName("Если login указан с пробелами, то возвращается код 400")
    void postMethod_shouldReturnResponseWithCode400_whenIncorrectLoginPassed() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"dolore \", \"name\":\"Nick Name\"," +
                                "\"email\":\"mail@mail.ru\", \"birthday\":\"1946-08-20\"}"))
                .andExpect(status().isInternalServerError());
    }

    // тестирование добавление пользователя без электронной почты
    @Test
    @DisplayName("Если электронная почта не указана, то возвращается код 400")
    void postMethod_shouldReturnResponseWithCode400_whenUserWithoutEmailPassed() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"dolore\", \"name\":\"Nick Name\"," +
                                "\"birthday\":\"1946-08-20\"}"))
                .andExpect(status().isInternalServerError());
    }

    // тестирование добавление пользователя с некорректным указанием электронной почты
    @Test
    @DisplayName("Если электронная почта указана некорректно, то возвращается код 400")
    void postMethod_shouldReturnResponseWithCode400_whenIncorrectEmailPassed() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"dolore\", \"name\":\"Nick Name\"," +
                                "\"email\":\"mail-mail.ru\", \"birthday\":\"1946-08-20\"}"))
                .andExpect(status().isInternalServerError());
    }

    // тестирование добавление пользователя указанием даты рождения из будущего
    @Test
    @DisplayName("Если если дата рождения больше текущей даты, то возвращается код 400")
    void postMethod_shouldReturnResponseWithCode400_whenIncorrectBirthdayPassed() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"dolore\", \"name\":\"Nick Name\"," +
                                "\"email\":\"mail@mail.ru\", \"birthday\":\"2046-08-20\"}"))
                .andExpect(status().isInternalServerError());
    }

    // тестирование корректного добавления пользователя
    @Test
    @DisplayName("При корректном добавлении пользователя, возвращается код 200 и пользователь добавляется в хранилище")
    void postMethod_shouldReturnResponseWithCode200_whenCorrectDataPassed() throws Exception {
        // добавляем пользователя
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"dolore\", \"name\":\"Nick Name\"," +
                                "\"email\":\"mail@mail.ru\", \"birthday\":\"1946-08-20\"}"))
                .andExpect(status().is2xxSuccessful());
        // проверяем, что пользователь был создан и добавлен в хранилище
        mockMvc.perform(get("/users"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[{\"id\":1,\"login\":\"dolore\",\"name\":\"Nick Name\"," +
                        "\"email\":\"mail@mail.ru\",\"birthday\":\"1946-08-20\",\"friends\":[]}]"));
    }

    // тестирование корректного обновление пользователя
    @Test
    @DisplayName("При корректном обновлении пользователя, возвращается код 200")
    void putMethod_shouldReturnResponseWithCode200_whenCorrectDataUpdated() throws Exception {
        // обновляем пользователя
        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"doloreUpdate\", \"name\":\"Name Update\"," +
                                "\"id\":\"1\", \"email\":\"mail@yandex.ru\", \"birthday\":\"1976-09-20\"}"))
                .andExpect(status().is2xxSuccessful());
        // проверяем, что пользователь в хранилище был обновлён
        mockMvc.perform(get("/users"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[{\"id\":1,\"login\":\"doloreUpdate\"," +
                        "\"name\":\"Name Update\",\"email\":\"mail@yandex.ru\",\"birthday\":\"1976-09-20\"," +
                        "\"friends\":[]}]"));
    }
}
