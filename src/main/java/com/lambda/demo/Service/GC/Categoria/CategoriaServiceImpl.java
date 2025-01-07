package com.lambda.demo.Service.GC.Categoria;

import com.lambda.demo.Entity.GC.CategoriaEntity;
import com.lambda.demo.Repository.GC.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService{
    @Autowired
    CategoriaRepository categoriaRepository;

    @Override
    public List<CategoriaEntity> getAllCategories() {
        return categoriaRepository.findAll();
    }
}
