package com.lambda.demo.Repository.GP.Wishlist;

import com.lambda.demo.Entity.GP.Wishlist.FormazioneWihslist.FormazioneWishlistEntity;
import com.lambda.demo.Entity.GP.Wishlist.FormazioneWihslist.FormazioneWishlistEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormazioneWishlistRepository extends JpaRepository<FormazioneWishlistEntity, FormazioneWishlistEntityId> {
}
