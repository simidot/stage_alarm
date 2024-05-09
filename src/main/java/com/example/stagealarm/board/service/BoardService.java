package com.example.stagealarm.board.service;

import com.example.stagealarm.awsS3.S3FileService;
import com.example.stagealarm.board.dto.BoardDto;
import com.example.stagealarm.board.dto.ContentSearchParams;
import com.example.stagealarm.board.dto.TitleSearchParams;
import com.example.stagealarm.board.entity.ActivateEnum;
import com.example.stagealarm.board.entity.Board;
import com.example.stagealarm.board.entity.Category;
import com.example.stagealarm.board.repo.BoardRepository;
import com.example.stagealarm.board.repo.CategoryRepository;
import com.example.stagealarm.board.repo.QBoardRepo;
import com.example.stagealarm.facade.AuthenticationFacade;
import com.example.stagealarm.image.entity.Image;
import com.example.stagealarm.image.repo.ImageRepository;
import com.example.stagealarm.user.entity.UserEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
  private final BoardRepository boardRepository;
  private final AuthenticationFacade auth;
  private final CategoryRepository categoryRepository;
  private final QBoardRepo qBoardRepo;
  private final S3FileService s3FileService;
  private final ImageRepository imageRepository;

  // Create
  @Transactional
  public BoardDto createBoard(
      List<MultipartFile> files,
      BoardDto dto
  ) {
    try {
      // 유저 정보 가져오기
      UserEntity targetUser = auth.getUserEntity();

      // 카테고리 정보 가져오기
      Category targetCategory = categoryRepository.findById(dto.getCategoryId())
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

      // 생성
      Board newBoard = Board.customBuilder()
          .title(dto.getTitle())
          .content(dto.getContent())
          .activate(ActivateEnum.ACTIVATE)
          .views(0L)
          .userEntity(targetUser)
          .category(targetCategory)
          .build();

      // 이미지가 있다면
      if (files != null) {
        List<String> uploadedUrls = s3FileService.uploadIntoS3("/boardImg", files);

        // Image Entity 생성 및 임시 저장을 위한 리스트
        List<Image> imageToSave = new ArrayList<>();

        // Image Entity 생성 및 Board Entity와 연결
        for (String url : uploadedUrls) {
          Image targetImage = Image.builder()
              .imgUrl(url)
              .build();

          // addImage 메서드를 사용하여 Board Entity에 Image Entity 연결
          newBoard.addImage(targetImage);

          // 임시 저장 리스트에 추가
          imageToSave.add(targetImage);
        }
        // Entity 저장
        imageRepository.saveAll(imageToSave);
      }
      // 저장
      return BoardDto.fromEntity(boardRepository.save(newBoard));
    } catch (Exception e) {
      log.error("err: {}", e.getMessage());
      if (e instanceof ResponseStatusException) throw (ResponseStatusException) e;
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "createBoard");
    }
  }

  // Read
  // read One
  public BoardDto readOne(Long boardId) {
    Board targetBoard = boardRepository.findById(boardId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    // 조회 수 올리기
    Long views = targetBoard.getViews() + 1L;
    targetBoard.setViews(views);

    // 반환
    return BoardDto.fromEntity(boardRepository.save(targetBoard));
  }

  // Update
  @Transactional
  public BoardDto reWriteBoard(
      List<MultipartFile> files,
      List<String> existingImages,
      Long boardId,
      BoardDto dto
  ) {
    BoardDto result = updateBoard(boardId, dto);
    Boolean imageResult = updateImage(boardId, files, existingImages);
    log.info("S3 uploaded: {}", imageResult);

    return result;
  }

  // Delete
  @Transactional
  public void deleteBoard(Long boardId) {
    try {
      // 권한 확인하고 targetBoard 가져오기
      Board targetBoard = checkAuthority(boardId);

      // 이미지가 있다면 삭제 진행
      if (!targetBoard.getImageList().isEmpty()) {
        // Image 찾기
        List<Image> targetImages = imageRepository.findAllByBoard_Id(boardId);

        for (Image image : targetImages) {
          String filename = image.getImgUrl().substring(image.getImgUrl().lastIndexOf("/") + 1);

          s3FileService.deleteFile("/boardImg", filename);
        }
        imageRepository.deleteAll(targetImages);
      }

      // 삭제
      boardRepository.deleteById(boardId);
    } catch (Exception e) {
      log.error("err: {}", e.getMessage());
      if (e instanceof ResponseStatusException) throw (ResponseStatusException) e;
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "deleteBoard");
    }
  }

  // Search
  // title
  public Page<BoardDto> searchTitle(
      TitleSearchParams params,
      Pageable pageable
  ) {
    if (params.getTitle() == null && params.getCategoryId() == null)
      return boardRepository.findAll(pageable).map(BoardDto::fromEntity);

    return qBoardRepo.searchTitle(params, pageable)
        .map(BoardDto::fromEntity);
  }

  // content
  public Page<BoardDto> searchContent(
      ContentSearchParams params,
      Pageable pageable
  ) {
    if (params.getContent() == null && params.getCategoryId() == null)
      return boardRepository.findAll(pageable).map(BoardDto::fromEntity);

    return qBoardRepo.searchContent(params, pageable)
        .map(BoardDto::fromEntity);
  }

  // Update - Board만 수정
  public BoardDto updateBoard(
      Long boardId,
      BoardDto dto
  ) {
    try {
      // 권한 확인하고 targetBoard 가져오기
      Board targetBoard = checkAuthority(boardId);

      // 카테고리 정보 가져오기
      Category targetCategory = categoryRepository.findById(dto.getCategoryId())
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

      // 수정
      targetBoard.setTitle(dto.getTitle());
      targetBoard.setContent(dto.getContent());
      targetBoard.setCategory(targetCategory);

      // 저장 및 반환
      return BoardDto.fromEntity(boardRepository.save(targetBoard));
    } catch (Exception e) {
      log.error("err: {}", e.getMessage());
      if (e instanceof ResponseStatusException) throw (ResponseStatusException) e;
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "update board");
    }
  }

  // Update - Image만 수정
  public Boolean updateImage(
      Long boardId,
      List<MultipartFile> files,
      List<String> existingImages
  ) {
    try {
      // 권한 확인하고 targetBoard 가져오기
      Board targetBoard = checkAuthority(boardId);

      List<Image> targetImages = imageRepository.findAllByBoard_Id(boardId);

      // 기존 이미지 중 유지되어야 할 이미지 처리
      if (existingImages != null && !existingImages.isEmpty()) {
        List<Image> imagesToKeep = targetImages.stream()
            .filter(image -> existingImages.contains(image.getImgUrl().substring(image.getImgUrl().lastIndexOf("/") + 1)))
            .toList();

        targetImages.removeAll(imagesToKeep); // 유지 대상이 아닌 이미지들 추출
      }

      // 삭제 대상 이미지 처리 (유지되어야 할 이미지를 제외한 나머지)
      if (!targetImages.isEmpty()) {
        for (Image image : targetImages) {
          String filename = image.getImgUrl().substring(image.getImgUrl().lastIndexOf("/") + 1);
          s3FileService.deleteFile("/boardImg", filename); // S3에서 이미지 파일 삭제
          imageRepository.delete(image); // 데이터베이스에서 이미지 항목 삭제
        }
      }

      // 새로운 파일들 업로드 후 데이터베이스에 저장
      if (files != null && !files.isEmpty()) {
        List<String> uploadedUrls = s3FileService.uploadIntoS3("/boardImg", files);

        List<Image> imageToSave = new ArrayList<>();
        for (String url : uploadedUrls) {
          Image newImage = Image.builder()
              .imgUrl(url)
              .build();
          // Board와 Image 연결
          targetBoard.addImage(newImage);
          imageToSave.add(newImage);
        }

        // 데이터베이스에 이미지 정보 저장
        imageRepository.saveAll(imageToSave);
      }

      return true;
    } catch (Exception e) {
      log.error("err: {}", e.getMessage());
      if (e instanceof ResponseStatusException) throw (ResponseStatusException) e;
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "update image");
    }
  }

  // boardId로 categoryId를 반환
  public Long findCategoryId(Long boardId) {
    try {
      Board targetBoard = boardRepository.findById(boardId)
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

      return targetBoard.getCategory().getId();
    } catch (Exception e) {
      log.error("err: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "find categoryId");
    }
  }

  public Board checkAuthority(Long boardId) {
    // 해당 board 가져오기
    Board targetBoard = boardRepository.findById(boardId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    // 수정 요청자 정보 가져오기
    UserEntity targetUser = auth.getUserEntity();

    // 권한 확인
    if (!targetUser.getId().equals(targetBoard.getUserEntity().getId()))
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

    return targetBoard;
  }
}
