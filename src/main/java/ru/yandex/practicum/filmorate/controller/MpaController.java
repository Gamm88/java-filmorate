package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

/**
 * Контроллер MPA (возрастных ограничений)
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;

    @GetMapping
    public List<Mpa> getAllMpa() {
        return mpaService.getAllMpa();
    }

    @GetMapping("/{mpaId}")
    public Mpa getMpaById(@PathVariable("mpaId") Long mpaId) {
        return mpaService.getMpaById(mpaId);
    }
}