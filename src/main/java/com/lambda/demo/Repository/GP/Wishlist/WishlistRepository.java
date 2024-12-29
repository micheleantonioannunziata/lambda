package com.lambda.demo.Repository.GP.Wishlist;


import com.lambda.demo.Entity.GP.Wishlist.WishlistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<WishlistEntity, Integer> {
}
