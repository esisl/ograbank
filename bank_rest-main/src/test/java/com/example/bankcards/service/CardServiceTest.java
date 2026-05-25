package com.example.bankcards.service;

import com.example.bankcards.dto.CardCreateDto;
import com.example.bankcards.dto.CardResponseDto;
import com.example.bankcards.dto.TransferRequestDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.UserDetailsImpl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock private CardRepository cardRepository;
    @Mock private UserRepository userRepository;
    @InjectMocks private CardServiceImpl cardService;

    private UUID currentUserId;
    private UUID otherUserId;
    private UUID cardId1;
    private UUID cardId2;
    private User currentUser;
    private User otherUser;
    private Card card1;
    private Card card2;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        currentUserId = UUID.randomUUID();
        otherUserId = UUID.randomUUID();
        cardId1 = UUID.randomUUID();
        cardId2 = UUID.randomUUID();

        currentUser = new User();
        currentUser.setId(currentUserId);
        currentUser.setEmail("test@bank.local");
        currentUser.setPasswordHash("$2a$10$hash");
        currentUser.setRole("USER");

        otherUser = new User();
        otherUser.setId(otherUserId);
        otherUser.setEmail("other@bank.local");
        otherUser.setPasswordHash("$2a$10$hash");
        otherUser.setRole("USER");

        card1 = new Card();
        card1.setId(cardId1);
        card1.setUser(currentUser);
        card1.setEncryptedNumber("ENC:4111111111111111");
        card1.setOwnerName("Test User");
        card1.setExpiryDate(LocalDate.of(2030, 12, 31));
        card1.setStatus("ACTIVE");
        card1.setBalance(BigDecimal.valueOf(100.00));
        card1.setCreatedAt(LocalDateTime.now());

        card2 = new Card();
        card2.setId(cardId2);
        card2.setUser(currentUser);
        card2.setEncryptedNumber("ENC:5555555555554444");
        card2.setOwnerName("Test User");
        card2.setExpiryDate(LocalDate.of(2030, 12, 31));
        card2.setStatus("ACTIVE");
        card2.setBalance(BigDecimal.valueOf(50.00));
        card2.setCreatedAt(LocalDateTime.now());

        // Настройка аутентификации
        UserDetailsImpl userDetails = new UserDetailsImpl(currentUser);
        authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("createCard: успешное создание карты")
    void createCard_success() {
        // Arrange
        CardCreateDto dto = new CardCreateDto();
        dto.setUserId(currentUserId);
        dto.setCardNumber("4222222222222222");
        dto.setOwnerName("New Card");
        dto.setExpiryDate(LocalDate.of(2031, 1, 1));

        when(userRepository.findById(currentUserId)).thenReturn(Optional.of(currentUser));
        when(cardRepository.save(any(Card.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        CardResponseDto result = cardService.createCard(dto);

        // Assert
        assertNotNull(result);
        assertEquals("**** **** **** 2222", result.getMaskedNumber());
        assertEquals("New Card", result.getOwnerName());
        assertEquals(BigDecimal.ZERO, result.getBalance());
        verify(cardRepository).save(any(Card.class));
    }

    @Test
    @DisplayName("createCard: пользователь не найден")
    void createCard_userNotFound() {
        // Arrange
        CardCreateDto dto = new CardCreateDto();
        
        // ✅ FIX: Используем currentUserId, чтобы проверка владения прошла успешно
        // Тогда код дойдёт до поиска в БД, где мы замокаем пустой результат
        dto.setUserId(currentUserId);
        
        dto.setCardNumber("4222222222222222");
        dto.setOwnerName("New Card");
        dto.setExpiryDate(LocalDate.of(2031, 1, 1));

        // Мокаем репозиторий: поиск по currentUserId вернёт пустой Optional
        when(userRepository.findById(currentUserId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                jakarta.persistence.EntityNotFoundException.class,
                () -> cardService.createCard(dto)
        );
        
        // Опционально: проверяем, что репозиторий действительно был вызван
        verify(userRepository).findById(currentUserId);
    }

    @Test
    @DisplayName("transfer: успешный перевод между своими картами")
    void transfer_success() {
        // Arrange
        TransferRequestDto dto = new TransferRequestDto();
        dto.setFromCardId(cardId1);
        dto.setToCardId(cardId2);
        dto.setAmount(BigDecimal.valueOf(25.00));

        when(cardRepository.findById(cardId1)).thenReturn(Optional.of(card1));
        when(cardRepository.findById(cardId2)).thenReturn(Optional.of(card2));

        // Act
        cardService.transfer(dto);

        // Assert
        assertEquals(BigDecimal.valueOf(75.00), card1.getBalance());
        assertEquals(BigDecimal.valueOf(75.00), card2.getBalance());
        verify(cardRepository).save(card1);
        verify(cardRepository).save(card2);
    }

    @Test
    @DisplayName("transfer: перевод на ту же карту")
    void transfer_sameCard() {
        // Arrange
        TransferRequestDto dto = new TransferRequestDto();
        dto.setFromCardId(cardId1);
        dto.setToCardId(cardId1); // same card
        dto.setAmount(BigDecimal.valueOf(10.00));

        when(cardRepository.findById(cardId1)).thenReturn(Optional.of(card1));

        // Act & Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> cardService.transfer(dto)
        );
        assertEquals("Cannot transfer to the same card", ex.getMessage());
        verify(cardRepository, never()).save(any());
    }

    @Test
    @DisplayName("transfer: недостаточно средств")
    void transfer_insufficientFunds() {
        // Arrange
        TransferRequestDto dto = new TransferRequestDto();
        dto.setFromCardId(cardId1);
        dto.setToCardId(cardId2);
        dto.setAmount(BigDecimal.valueOf(999.99)); // больше баланса

        when(cardRepository.findById(cardId1)).thenReturn(Optional.of(card1));
        when(cardRepository.findById(cardId2)).thenReturn(Optional.of(card2));

        // Act & Assert
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> cardService.transfer(dto)
        );
        assertEquals("Insufficient funds", ex.getMessage());
        verify(cardRepository, never()).save(any());
    }

    @Test
    @DisplayName("transfer: попытка перевода с чужой карты")
    void transfer_notOwner_sourceCard() {
        // Arrange
        Card otherCard = new Card();
        otherCard.setId(UUID.randomUUID());
        otherCard.setUser(otherUser); // другая карта
        otherCard.setBalance(BigDecimal.valueOf(100.00));

        TransferRequestDto dto = new TransferRequestDto();
        dto.setFromCardId(otherCard.getId());
        dto.setToCardId(cardId2);
        dto.setAmount(BigDecimal.valueOf(10.00));

        when(cardRepository.findById(otherCard.getId())).thenReturn(Optional.of(otherCard));
        when(cardRepository.findById(cardId2)).thenReturn(Optional.of(card2));

        // Act & Assert
        AccessDeniedException ex = assertThrows(
                AccessDeniedException.class,
                () -> cardService.transfer(dto)
        );
        assertEquals("You can only transfer between your own cards", ex.getMessage());
    }

    @Test
    @DisplayName("transfer: попытка перевода на чужую карту")
    void transfer_notOwner_targetCard() {
        // Arrange
        Card otherCard = new Card();
        otherCard.setId(UUID.randomUUID());
        otherCard.setUser(otherUser);
        otherCard.setBalance(BigDecimal.valueOf(100.00));

        TransferRequestDto dto = new TransferRequestDto();
        dto.setFromCardId(cardId1);
        dto.setToCardId(otherCard.getId()); // чужая карта
        dto.setAmount(BigDecimal.valueOf(10.00));

        when(cardRepository.findById(cardId1)).thenReturn(Optional.of(card1));
        when(cardRepository.findById(otherCard.getId())).thenReturn(Optional.of(otherCard));

        // Act & Assert
        AccessDeniedException ex = assertThrows(
                AccessDeniedException.class,
                () -> cardService.transfer(dto)
        );
        assertEquals("You can only transfer between your own cards", ex.getMessage());
    }

    @Test
    @DisplayName("getCardsByUserId: пагинация своих карт")
    void getCardsByUserId_pagination() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Card> cardPage = new PageImpl<>(List.of(card1, card2), pageable, 2);

        when(cardRepository.findByUserId(currentUserId, pageable)).thenReturn(cardPage);

        // Act
        Page<CardResponseDto> result = cardService.getCardsByUserId(currentUserId, pageable);

        // Assert
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals("**** **** **** 1111", result.getContent().get(0).getMaskedNumber());
    }

    @Test
    @DisplayName("getCardsByUserId: доступ к чужим картам запрещён")
    void getCardsByUserId_accessDenied() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        UUID foreignUserId = UUID.randomUUID();

        // Act & Assert
        AccessDeniedException ex = assertThrows(
                AccessDeniedException.class,
                () -> cardService.getCardsByUserId(foreignUserId, pageable)
        );
        assertEquals("Cannot access cards of another user", ex.getMessage());
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }
}