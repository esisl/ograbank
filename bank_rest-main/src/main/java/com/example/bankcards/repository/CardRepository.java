package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {
    // Pageable автоматически трансформируется в LIMIT/OFFSET + сортировку
    Page<Card> findByUserId(UUID userId, Pageable pageable);
}