package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FilmController.class)
public class FilmControllerTest {

    @Autowired
    MockMvc mockMvc;

    // тестирование добавление фильма без имени
    @Test
    @DisplayName("Если нет поля name, то возвращается код 400")
    void postMethod_shouldReturnResponseWithCode400_whenIncorrectNamePassed() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"Description\"," +
                                "\"releaseDate\":\"1967-03-25\", \"duration\":\"100\"}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // тестирование добавление фильма с некорректным указанием описанием (больше 200 символов)
    @Test
    @DisplayName("Если описание больше 200 символов, то возвращается код 400")
    void postMethod_shouldReturnResponseWithCode400_whenIncorrectLoginPassed() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Film name\", \"description\":\"Пятеро друзей ( комик-группа «Шарло»)," +
                                " приезжают в город Бризуль. Здесь они хотят разыскать господина Огюста Куглова," +
                                " который задолжал им деньги, а именно 20 миллионов. о Куглов," +
                                " который за время «своего отсутствия», стал кандидатом Коломбани.\"," +
                                "\"releaseDate\":\"1900-03-25\", \"duration\":\"200\"}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // тестирование добавление фильма с некорректным указанием продолжительности (не положительная)
    @Test
    @DisplayName("Если продолжительность не положительная, то возвращается код 400")
    void postMethod_shouldReturnResponseWithCode400_whenIncorrectDurationPassed() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Name\", \"description\":\"Description\"," +
                                "\"releaseDate\":\"1980-03-25\", \"duration\":\"-200\"}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // тестирование корректного добавления фильма
    @Test
    @DisplayName("При корректном добавлении фильма, возвращается код 200 и фильм добавляется в хранилище")
    void postMethod_shouldReturnResponseWithCode200_whenCorrectDataPassed() throws Exception {
        // добавляем фильм
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Name\", \"description\":\"Description\"," +
                                "\"releaseDate\":\"1980-03-25\", \"duration\":\"200\"}"))
                .andExpect(status().is2xxSuccessful());
        // проверяем, что фильм был создан и добавлен в хранилище
        mockMvc.perform(get("/films"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[{\"id\":1,\"name\":\"Name\",\"description\":\"Description\"," +
                        "\"releaseDate\":\"1980-03-25\",\"duration\":200}]"));
    }

    // тестирование корректного обновление фильма
    @Test
    @DisplayName("При корректном обновлении пользователя, возвращается код 200")
    void putMethod_shouldReturnResponseWithCode200_whenCorrectDataUpdated() throws Exception {
        // обновляем фильм
        mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"NameUpdate\", \"description\":\"Description Update\"," +
                                "\"id\":\"1\", \"releaseDate\":\"1980-03-25\", \"duration\":\"150\"}"))
                .andExpect(status().is2xxSuccessful());
        // проверяем, что фильм в хранилище был обновлён
        mockMvc.perform(get("/films"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[{\"id\":1,\"name\":\"NameUpdate\"," +
                        "\"description\":\"Description Update\",\"releaseDate\":\"1980-03-25\",\"duration\":150}]"));
    }

}
