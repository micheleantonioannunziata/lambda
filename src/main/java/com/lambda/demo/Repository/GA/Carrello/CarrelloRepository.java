package com.lambda.demo.Repository.GA.Carrello;

import com.lambda.demo.Entity.GA.Carrello.CarrelloEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarrelloRepository extends JpaRepository<CarrelloEntity, Integer> {
}
