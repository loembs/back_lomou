package ism.atelier.atelier.services.impl;

import ism.atelier.atelier.data.models.Category;
import ism.atelier.atelier.data.models.Plate;
import ism.atelier.atelier.data.repository.CategoryRepository;
import ism.atelier.atelier.data.repository.PlateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ism.atelier.atelier.services.MenuService;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private PlateRepository plateRepo;
    @Autowired
    private CategoryRepository categoryRepo;

    public List<Category> getMenu() {
        return categoryRepo.findAllWithPlates();
    }

    public Plate savePlate(Plate plate) {
        return plateRepo.save(plate);
    }
} 