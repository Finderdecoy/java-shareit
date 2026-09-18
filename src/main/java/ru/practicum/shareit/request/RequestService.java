package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestOnCreate;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestService {

    private final RequestRepo requestRepo;
    private final UserRepository userRepository;

    public ItemRequestDto save(Long userId, ItemRequestOnCreate dtoCreate) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не зарегистрирован"));
        ItemRequest itemRequest = RequestMapper.toCreate(dtoCreate, user);
        log.info("Идет сохранение в бд объект: {}", itemRequest);
        return RequestMapper.toDto(requestRepo.save(itemRequest));
    }

    public List<ItemRequestDto> getMyRequests(Long userId) {
        return requestRepo.findByOwnerRequestId(userId).stream().map(RequestMapper::toDto).toList();
    }

    public List<ItemRequestDto> getAllRequests() {
        return requestRepo.findAll().stream().map(RequestMapper::toDto).toList();
    }

    public ItemRequestDto getRequestById(Long requestId) {
        ItemRequest request = requestRepo.findById(requestId).orElseThrow(
                () -> new NotFoundException("Запрос с id - " + requestId + " не найден"));
        log.info("Обьект из бд по запросу : {}", request);
        return RequestMapper.toDto(request);
    }
}
