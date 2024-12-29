package com.lambda.demo.Repository.GP.Newsletter;


import com.lambda.demo.Entity.GP.IscrizioneNewsletter.IscrizioneNewsletterEntity;
import com.lambda.demo.Entity.GP.IscrizioneNewsletter.IscrizioneNewsletterEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IscrizioneNewsletterRepository extends JpaRepository<IscrizioneNewsletterEntity, IscrizioneNewsletterEntityId> {
}
