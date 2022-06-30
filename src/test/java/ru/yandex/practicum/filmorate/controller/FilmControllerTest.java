package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = FilmController.class)
public class FilmControllerTest {

    @Autowired
    MockMvc mockMvc;

    // тестирование добавление фильма без имени
    @Test
    @DisplayName("Если нет поля name, то возвращается код 400")
    void AddFilmWithoutName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"adipisicing\"," +
                                "\"releaseDate\":\"1967-03-25\", \"duration\":\"100\"}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // тестирование добавление фильма с некорректным указанием описанием (больше 200 символов)
    @Test
    @DisplayName("Если описание больше 200 символов, то возвращается код 400")
    void AddFilmWithWrongLogin() throws Exception {
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
    void AddFilmWithWrongDuration() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Name\", \"description\":\"Descrition\"," +
                                "\"releaseDate\":\"1980-03-25\", \"duration\":\"-200\"}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}
