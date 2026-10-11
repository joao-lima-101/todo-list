package com.todolist.todoserver.controller;

import com.todolist.todoserver.dto.ApiResponseDTO;
import com.todolist.todoserver.dto.ListRequestDTO;
import com.todolist.todoserver.dto.ListResponseDTO;
import com.todolist.todoserver.entity.User;
import com.todolist.todoserver.service.ListService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/list")
public class ListController {

    private final ListService listService;

    public ListController(ListService listService) {
        this.listService = listService;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO> create(
            @Valid @RequestBody ListRequestDTO dto,
            @AuthenticationPrincipal User user
    ) {
        listService.create(user.getId(), dto.name(), dto.description());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO("Nova lista criada com sucesso!"));
    }

    @GetMapping("/{idList}")
    public ResponseEntity<ListResponseDTO> getList(@PathVariable Long idList) {
        ListResponseDTO dto = listService.getList(idList);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{idList}")
    public ResponseEntity<ApiResponseDTO> updateList(@PathVariable Long idList, @RequestBody ListRequestDTO dto) {
        listService.update(idList, dto.name(), dto.description());
        return ResponseEntity.ok(new ApiResponseDTO("Lista atualizada com sucesso!"));
    }

    @DeleteMapping("/{idList}")
    public ResponseEntity<Void> delete(@PathVariable Long idList) {
        listService.delete(idList);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ListResponseDTO>> getLists( @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(listService.getLists(user.getId()));
    }
}
