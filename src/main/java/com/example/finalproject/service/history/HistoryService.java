package com.example.finalproject.service.history;

import com.example.finalproject.domain.dto.request.HistoryRequest;
import com.example.finalproject.domain.dto.response.BaseResponse;
import com.example.finalproject.domain.dto.response.HistoryResponse;
import com.example.finalproject.domain.entity.history.HistoryEntity;
import com.example.finalproject.repository.history.HistoryRepository;
import com.example.finalproject.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoryService implements BaseService<BaseResponse<HistoryResponse>, HistoryRequest> {
    private final HistoryRepository historyRepository;
    private final ModelMapper mapper;
    public List<HistoryResponse> getUserHistories(Long chatId) {
        return historyRepository.getUserHistories(chatId).stream()
                .map((history) -> mapper.map(history, HistoryResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public BaseResponse<HistoryResponse> create(HistoryRequest historyRequest) {
        historyRepository.save(mapper.map(historyRequest, HistoryEntity.class));
        return BaseResponse.<HistoryResponse>builder()
                .message("successfully saved")
                .status(200)
                .build();
    }

    @Override
    public BaseResponse<HistoryResponse> update(HistoryRequest historyRequest, UUID id) {
        historyRepository.update(historyRequest.getAmount(), historyRequest.getTotalPrice(), id);
        return BaseResponse.<HistoryResponse>builder()
                .message("success")
                .status(200)
                .build();
    }

    @Override
    public Boolean delete(UUID id) {
        Optional<HistoryEntity> byId = historyRepository.findById(id);

        if (byId.isPresent()) {
            historyRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public BaseResponse<HistoryResponse> getById(UUID id) {
        Optional<HistoryEntity> byId = historyRepository.findById(id);

        if (byId.isPresent()) {
            return BaseResponse.<HistoryResponse>builder()
                    .message("success")
                    .status(200)
                    .data(mapper.map(byId.get(), HistoryResponse.class))
                    .build();
        }
        return BaseResponse.<HistoryResponse>builder()
                .message("fail")
                .status(400)
                .build();
    }
}

