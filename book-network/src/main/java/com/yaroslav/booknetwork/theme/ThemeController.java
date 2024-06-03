package com.yaroslav.booknetwork.theme;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    @Autowired
    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<Theme>> getAllThemes() {
        List<Theme> themes = themeService.getAllThemes();
        return new ResponseEntity<>(themes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Theme> getThemeById(@PathVariable Long id) {
        Optional<Theme> theme = themeService.getThemeById(id);
        return theme.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Theme> createTheme(@RequestBody Theme theme) {
        Theme savedTheme = themeService.saveTheme(theme);
        return new ResponseEntity<>(savedTheme, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Theme> editTheme(@PathVariable Long id, @RequestBody Theme editedTheme) {
        editedTheme.setId(id); // Устанавливаем ID темы из пути запроса
        Theme updatedTheme = themeService.editTheme(editedTheme);
        return new ResponseEntity<>(updatedTheme, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteTheme(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
