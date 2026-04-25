package com.traveler.search.domain.post.repository;

import com.traveler.search.domain.post.document.PostDocument;
import com.traveler.search.domain.post.dto.message.PostSearchMessage;
import com.traveler.search.domain.post.dto.request.PostSearchRequest;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostDocumentRepositoryCustom {
    Page<PostDocument> search(PostSearchRequest.SearchDTO request, Pageable pageable);

    List<String> findAutocompleteTitles(String keyword);

    void updatePartial(PostSearchMessage.UpdatedDTO dto);

    void updateStatistics(PostSearchMessage.StatUpdateDoc doc);
}
