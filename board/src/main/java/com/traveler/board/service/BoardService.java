package com.traveler.board.service;


import com.traveler.board.dto.BoardDocumentDto;
import com.traveler.board.dto.BoardDto;
import com.traveler.board.dto.BoardListDto;
import com.traveler.board.dto.SearchResultDto;
import com.traveler.board.entity.Board;
import com.traveler.board.entity.Image;
import com.traveler.board.repository.BoardRepository;
import com.traveler.board.repository.ImageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final ImageRepository imageRepository;
    @Qualifier("${image.storage.service}")
    private final ImageStorageService imageStorageService;
    private final TravelPlaceService travelPlaceService;
    private final SearchService searchService;
    private final KafkaProducerService kafkaProducerService;

    public SearchResultDto listArticlesBySearch(String keyword, String category, String region, String sort, String direction, int page) throws DataAccessException{
        BoardDocumentDto searchResult = searchService.search(keyword, category, region, sort, direction, page);

        List<BoardListDto> result = searchResult.getResult().stream().map(board -> BoardListDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .memberId(board.getMemberId())
                .modifiedDate(board.getModifiedDate())
                .category(board.getCategory())
                .region(board.getRegion())
                .viewCount(board.getViewCount())
                .ratingAvg(board.getRatingAvg())
                .build()
        ).toList();
        Long totalHits = searchResult.getTotalHits();
        return new SearchResultDto(result, totalHits);
    }



    public List<BoardListDto> listArticles() throws DataAccessException{
        List<Board> boardList = boardRepository.findAll();
        return boardList.stream().map(board -> BoardListDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .memberId(board.getMemberId())
                .modifiedDate(board.getModifiedDate())
                .category(board.getTravelPlace().getCategory().getName())
                .region(board.getTravelPlace().getRegion().getName())
                .build()
        ).collect(Collectors.toList());
    }

    @Transactional
    public void addArticle(BoardDto data, List<MultipartFile> images)throws DataAccessException{

        Board board = new Board();
        board.setTitle(data.getTitle());
        board.setContent(data.getContent());
        board.setMemberId(data.getMemberId());
        board.setTravelPlace(travelPlaceService.addAndGetTravelPlace(data.getCategory(), data.getRegion(), data.getTravelPlace(), data.getAddress()));
        Board savedBoard = boardRepository.save(board);
        uploadImages(images, savedBoard);
        searchService.saveToES(savedBoard);
    }

    public BoardDto viewArticle(long no){
        Board board = boardRepository.findById(no).orElseThrow(() -> new CustomBoardException("존재하지 않는 게시글입니다."));
        List<Image> images = imageRepository.findByBoard(board);
        List<String> imagePaths = images.stream()
                .map(Image::getPath)
                .collect(Collectors.toList());
        BoardDto dto = new BoardDto();
        dto.setNo(board.getId().toString());
        dto.setTitle(board.getTitle());
        dto.setContent(board.getContent());
        dto.setMemberId(board.getMemberId());
        dto.setTravelPlace(board.getTravelPlace().getName());
        dto.setAddress(board.getTravelPlace().getAddress());
        dto.setRegion(board.getTravelPlace().getRegion().getName());
        dto.setCategory(board.getTravelPlace().getCategory().getName());
        dto.setRatingAvg(board.getRatingAvg());
        dto.setFavoriteCount(board.getFavoriteCount());
        dto.setCommentCount(board.getCommentCount());
        dto.setViewCount(board.getViewCount());
        dto.setImagePaths(imagePaths);
        return dto;
    }

    @Transactional
    public void editArticle(BoardDto data, List<MultipartFile> images){
        Board board = boardRepository.findById(Long.parseLong(data.getNo())).orElseThrow(RuntimeException::new);
        board.setId(Long.parseLong(data.getNo()));
        board.setTitle(data.getTitle());
        board.setContent(data.getContent());
        board.setMemberId(data.getMemberId());
        board.setTravelPlace(travelPlaceService.editAndGetTravelPlace(board.getTravelPlace().getId(), data.getCategory(), data.getRegion(), data.getTravelPlace(), data.getAddress()));

        List<Image> existingImages = imageRepository.findByBoard(board);
        for (Image img : existingImages) {
            imageRepository.delete(img);
            try {
                imageStorageService.delete(img.getPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        // 2. 새 이미지 저장
        if (images != null && !images.isEmpty()) {
            uploadImages(images, board);
        }
        searchService.saveToES(board);
//        Board savedBoard = boardRepository.save(board);
//        searchService.saveToES(savedBoard);
    }
    public void removeArticle(Long no){
        Board board = boardRepository.findById(no).orElseThrow(() -> new CustomBoardException("존재하지 않는 게시글입니다."));
        List<Image> existingImages = imageRepository.findByBoard(board);
        for (Image img : existingImages) {
            imageRepository.delete(img);
            try {
                imageStorageService.delete(img.getPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        boardRepository.delete(board);
        searchService.deleteById(no);
        kafkaProducerService.deleteBoard(no);

    }

    public void uploadImages(List<MultipartFile> files, Board board){
        if (files.isEmpty()) {
            throw new CustomBoardException("이미지 파일이 없습니다.");
        }
        try {
            for (MultipartFile file : files) {
                String path = imageStorageService.store(file);

                Image image = new Image();
                image.setBoard(board);
                image.setName(file.getOriginalFilename());
                image.setPath(path);
                imageRepository.save(image);
            }
        } catch (Exception e) {
            throw new CustomBoardException("이미지 업로드 실패");
        }
    }

    public List<BoardListDto> getListPart(List<Long> boardIDs){
        List<Board> boards = boardRepository.findByIdIn(boardIDs);
        return boards.stream().map(board -> BoardListDto.builder()
                        .id(board.getId())
                        .title(board.getTitle())
                        .modifiedDate(board.getModifiedDate())
                        .memberId(board.getMemberId())
                        .category(board.getTravelPlace().getCategory().getName())
                        .region(board.getTravelPlace().getRegion().getName())
                        .build())
                .collect(Collectors.toList());

    }
    public List<BoardListDto> getListByMember(Long memberId){
        List<Board> boards = boardRepository.findAllByMemberId(memberId);
        return boards.stream().map(board -> BoardListDto.builder()
                        .id(board.getId())
                        .title(board.getTitle())
                        .modifiedDate(board.getModifiedDate())
                        .memberId(board.getMemberId())
                        .category(board.getTravelPlace().getCategory().getName())
                        .region(board.getTravelPlace().getRegion().getName())
                        .build())
                .collect(Collectors.toList());

    }
    public void migrateAll(){
        List<Board> boardList = boardRepository.findAll();
        searchService.migrateAllBoardsToES(boardList);
    }

    @Transactional
    public void updateRatingAvg(Long boardId, Integer rating, boolean isAdd) {
        Board board = boardRepository.findById(boardId).orElse(null);
        if (board == null) return;

        long commentCount = board.getCommentCount() == null ? 0L : board.getCommentCount();
        double ratingAvg = board.getRatingAvg() == null ? 0.0 : board.getRatingAvg();
        if(isAdd){
            long newCommentCount = commentCount + 1;
            double newRatingAvg = (ratingAvg * commentCount + rating) / newCommentCount;

            board.setCommentCount(newCommentCount);
            board.setRatingAvg(newRatingAvg);
        }
        else{
            long newCommentCount = commentCount - 1;
            double newRatingAvg = (ratingAvg * commentCount - rating) / newCommentCount;

            board.setCommentCount(newCommentCount);
            board.setRatingAvg(newRatingAvg);
        }
    }

    @Transactional
    public void updateFavoriteCount(Long boardId, boolean isAdd) {
        Board board = boardRepository.findById(boardId).orElse(null);
        if (board == null) return;

        long favoriteCount = board.getFavoriteCount() == null ? 0L : board.getFavoriteCount();
        board.setFavoriteCount(isAdd ? favoriteCount + 1 : favoriteCount - 1);
    }

    @Transactional
    public void updateViewCount(Long boardId){
        boardRepository.increaseViewCount(boardId);
    }

}
