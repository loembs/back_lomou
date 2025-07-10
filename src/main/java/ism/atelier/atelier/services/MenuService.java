package ism.atelier.atelier.services;

import ism.atelier.atelier.data.models.Category;
import ism.atelier.atelier.data.models.Plate;
import ism.atelier.atelier.data.repository.CategoryRepository;
import ism.atelier.atelier.data.repository.PlateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MenuService {

    public List<Category> getMenu();

    public Plate savePlate(Plate plate);
} 