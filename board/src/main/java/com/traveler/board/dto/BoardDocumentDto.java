package com.traveler.board.dto;

import com.traveler.board.entity.BoardDocument;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BoardDocumentDto {
    private List<BoardDocument> result;
    private Long totalHits;
}
