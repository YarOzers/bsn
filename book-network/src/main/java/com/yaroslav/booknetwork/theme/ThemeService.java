package com.yaroslav.booknetwork.theme;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ThemeService {
    private final ThemeRepository themeRepository;

    @Autowired
    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<Theme> getAllThemes() {
        return themeRepository.findAll();
    }

    public Optional<Theme> getThemeById(Long id) {
        return themeRepository.findById(id);
    }

    public Theme saveTheme(Theme theme) {
        return themeRepository.save(theme);
    }

    public void deleteTheme(Long id) {
        themeRepository.deleteById(id);
    }

    public Theme editTheme(Theme editedTheme) {
        Optional<Theme> existingThemeOptional = themeRepository.findById(editedTheme.getId());
        if (existingThemeOptional.isPresent()) {
            Theme existingTheme = existingThemeOptional.get();
            existingTheme.setName(editedTheme.getName());
            // Сохраняем обновленную тему и возвращаем ее
            return themeRepository.save(existingTheme);
        } else {
            throw new IllegalArgumentException("Theme with ID " + editedTheme.getId() + " not found");
        }
    }
}
