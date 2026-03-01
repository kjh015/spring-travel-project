package com.traveler.board.repository.querydsl;

import static com.traveler.board.entity.QBoard.board;
import static com.traveler.board.entity.QCategory.category;
import static com.traveler.board.entity.QRegion.region;
import static com.traveler.board.entity.QTravelPlace.travelPlace;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.traveler.board.dto.BoardListDto;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BoardListDto> findBoardList() {
        return queryFactory
                .select(Projections.constructor(
                        BoardListDto.class,
                        board.id,
                        board.title,
                        board.memberId,
                        board.modifiedDate,
                        travelPlace.category.name,
                        travelPlace.region.name,
                        board.viewCount,
                        board.ratingAvg))
                .from(board)
                .join(board.travelPlace, travelPlace)
                .join(travelPlace.category, category)
                .join(travelPlace.region, region)
                .orderBy(board.id.desc())
                .fetch();
    }
}
