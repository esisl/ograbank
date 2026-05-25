package com.example.bankcards.service;

import com.example.bankcards.dto.CardCreateDto;
import com.example.bankcards.dto.CardResponseDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.UserDetailsImpl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.AccessDeniedException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.bankcards.dto.TransferRequestDto;
import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional(readOnly = true) // По умолчанию все методы только для чтения
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    // Конструкторная инъекция. Стандарт Spring. @Autowired на полях — антипаттерн.
    public CardServiceImpl(CardRepository cardRepository, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public CardResponseDto createCard(CardCreateDto dto) {
        // 1. Получаем текущего аутентифицированного пользователя
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            throw new AccessDeniedException("Not authenticated");
        }
        
        UUID currentUserId = userDetails.getId();
        
        // 2. Проверяем права: ADMIN может всё, USER — только свои карты
        boolean isAdmin = userDetails.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        if (!isAdmin && !currentUserId.equals(dto.getUserId())) {
            throw new AccessDeniedException("Cannot create card for another user");
        }
        
        // 3. Основная логика создания карты
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Card card = new Card();
        card.setUser(user);
        card.setEncryptedNumber("ENC:" + dto.getCardNumber());
        card.setOwnerName(dto.getOwnerName());
        card.setExpiryDate(dto.getExpiryDate());
        card.setStatus("ACTIVE");
        card.setCreatedAt(LocalDateTime.now());

        cardRepository.save(card);

        return toResponseDto(card);
    }

    private CardResponseDto toResponseDto(Card card) {
        // Маскирование делается здесь. Контроллер не должен знать о бизнес-правилах форматирования.
        String encrypted = card.getEncryptedNumber();
        String last4 = encrypted.length() > 4 ? encrypted.substring(encrypted.length() - 4) : "0000";
        String masked = "**** **** **** " + last4;
        
        return new CardResponseDto(
                card.getId(), masked, card.getOwnerName(),
                card.getExpiryDate(), card.getStatus(),
                card.getBalance(), card.getCreatedAt()
        );
    }

    @Override
    public Page<CardResponseDto> getCardsByUserId(UUID userId, Pageable pageable) {
        // Проверка владения (аналогично createCard)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            throw new AccessDeniedException("Not authenticated");
        }
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && !userDetails.getId().equals(userId)) {
            throw new AccessDeniedException("Cannot access cards of another user");
        }

        Page<Card> cardPage = cardRepository.findByUserId(userId, pageable);
        // map сохраняет totalElements, totalPages, number и т.д.
        return cardPage.map(this::toResponseDto);
    }

    @Override
    @Transactional
    public void transfer(TransferRequestDto dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof UserDetailsImpl user)) {
            throw new AccessDeniedException("Not authenticated");
        }
        UUID currentUserId = user.getId();

        Card fromCard = cardRepository.findById(dto.getFromCardId())
                .orElseThrow(() -> new EntityNotFoundException("Source card not found"));
        Card toCard = cardRepository.findById(dto.getToCardId())
                .orElseThrow(() -> new EntityNotFoundException("Target card not found"));

        // 1. Проверка владения: обе карты должны принадлежать текущему пользователю
        if (!fromCard.getUser().getId().equals(currentUserId) || 
            !toCard.getUser().getId().equals(currentUserId)) {
            throw new AccessDeniedException("You can only transfer between your own cards");
        }

        // 2. Защита от перевода на ту же карту
        if (fromCard.getId().equals(toCard.getId())) {
            throw new IllegalArgumentException("Cannot transfer to the same card");
        }

        // 3. Проверка баланса
        if (fromCard.getBalance().compareTo(dto.getAmount()) < 0) {
            throw new IllegalStateException("Insufficient funds");
        }

        // 4. Атомарное обновление
        fromCard.setBalance(fromCard.getBalance().subtract(dto.getAmount()));
        toCard.setBalance(toCard.getBalance().add(dto.getAmount()));

        cardRepository.save(fromCard);
        cardRepository.save(toCard);
    }
}