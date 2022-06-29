package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    // тестирование добавление пользователя без логина
    @Test
    @DisplayName("Если нет поля login, то возвращается код 400")
    void AddUserWithoutLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Nick Name\"," +
                                "\"email\":\"mail@mail.ru\", \"birthday\":\"1946-08-20\"}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // тестирование добавление пользователя с некорректным указанием логина (с пробелом)
    @Test
    @DisplayName("Если нет поля login, то возвращается код 400")
    void AddUserWithWrongLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"dolore \", \"name\":\"Nick Name\"," +
                                "\"email\":\"mail@mail.ru\", \"birthday\":\"1946-08-20\"}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // тестирование добавление пользователя без электронной почты
    @Test
    @DisplayName("Если электронная почта не указана, то возвращается код 400")
    void AddUserWithoutEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"dolore\", \"name\":\"Nick Name\"," +
                                "\"birthday\":\"1946-08-20\"}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // тестирование добавление пользователя с некорректным указанием электронной почты
    @Test
    @DisplayName("Если электронная почта указана некорректно, то возвращается код 400")
    void AddUserWithWrongEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"dolore\", \"name\":\"Nick Name\"," +
                                "\"email\":\"mail-mail.ru\", \"birthday\":\"1946-08-20\"}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // тестирование добавление пользователя указанием даты рождения из будущего
    @Test
    @DisplayName("Если если дата рождения больше текущей даты, то возвращается код 400")
    void AddUserWithWrongBirthday () throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"dolore\", \"name\":\"Nick Name\"," +
                                "\"email\":\"mail@mail.ru\", \"birthday\":\"2046-08-20\"}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // тестирование корректно добавление пользователя
    @Test
    @DisplayName("При корректном добавлении пользователя, возвращается код 200")
    void AddUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"dolore\", \"name\":\"Nick Name\"," +
                                "\"email\":\"mail@mail.ru\", \"birthday\":\"1946-08-20\"}"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    // тестирование корректного обновление пользователя
    @Test
    @DisplayName("При корректном обновлении пользователя, возвращается код 200")
    void PutUser() throws Exception {
        // обновляем пользователя
        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"doloreUpdate\", \"name\":\"est adipisicing\"," +
                                "\"id\":\"1\", \"email\":\"mail@yandex.ru\", \"birthday\":\"1976-09-20\"}"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }
}
