package com.traveler.board.dto;

import com.traveler.board.entity.BoardDocument;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardDocumentDto {
    private List<BoardDocument> result;
    private Long totalHits;
}
