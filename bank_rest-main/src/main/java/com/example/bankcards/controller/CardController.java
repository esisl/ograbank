package com.example.bankcards.controller;

import com.example.bankcards.dto.CardCreateDto;
import com.example.bankcards.dto.CardResponseDto;
import com.example.bankcards.dto.TransferRequestDto;
import com.example.bankcards.service.CardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.web.PageableDefault;
// import org.springframework.data.domain.Sort;
// import org.springframework.data.domain.PageRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    // POST: ADMIN может создавать карты для любого пользователя, USER — только для себя
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public CardResponseDto createCard(@Valid @RequestBody CardCreateDto dto) {
        return cardService.createCard(dto);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Page<CardResponseDto> getCardsByUser(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") org.springframework.data.domain.Sort.Direction direction) {
                
        if (size > 100) size = 100;
        if (page < 0) page = 0;

        // direction уже имеет тип Sort.Direction, конвертация выполнена Spring
        org.springframework.data.domain.Sort sort = org.springframework.data.domain.Sort.by(direction, sortBy);
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, sort);
        
        return cardService.getCardsByUserId(userId, pageable);
    }

    @PostMapping("/transfer")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Перевод между картами", description = "Атомарный перевод средств между двумя картами одного владельца")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Перевод выполнен успешно"),
        @ApiResponse(responseCode = "400", description = "Невалидный запрос / недостаточно средств"),
        @ApiResponse(responseCode = "403", description = "Доступ запрещён (чужая карта)")
    })
    @ResponseStatus(HttpStatus.OK)
    public void transferCards(@Valid @RequestBody TransferRequestDto dto) {
        cardService.transfer(dto);
    }
}