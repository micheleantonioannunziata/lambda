package com.lambda.demo.Repository.GA.Carrello;

import com.lambda.demo.Entity.GA.Carrello.FormazioneCarrello.FormazioneCarrelloEntity;
import com.lambda.demo.Entity.GA.Carrello.FormazioneCarrello.FormazioneCarrelloEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormazioneCarrelloRepository extends JpaRepository<FormazioneCarrelloEntity, FormazioneCarrelloEntityId> {
}
