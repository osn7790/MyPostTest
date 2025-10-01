package com.example.mypost.post;

import com.example.mypost._core.errors.exception.Exception403;
import com.example.mypost._core.errors.exception.Exception404;
import com.example.mypost.user.LoginUser;
import com.example.mypost.user.User;
import com.example.mypost.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PostService {

    private final PostJpaRepository postJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Transactional
    public Post save(PostRequest.SaveDTO saveDTO, LoginUser loginUser) {
        log.info("게시글 저장 서비스 처리 시작 - 제목 {} , 작성자 {}",
                saveDTO.getTitle(), loginUser.getUsername());
        User user = userJpaRepository.findById(loginUser.getId())
                .orElseThrow(() -> new Exception404("로그인 유저를 찾을 수 없습니다."));

        Post post = saveDTO.toEntity(user);
        postJpaRepository.save(post);

        log.info("게시글 저장 완료 - ID {}, 제목 {}", post.getTitle());

        return post;
    }

    public List<Post> findAll() {

        log.info("게시글 조회 서비스 처리 시작 - 제목 {}, 작성자 {}");
        List<Post> postList = postJpaRepository.findAllJoinUser();
        log.info("게시글 목록 조회 완료 - 총 {} 개", postList.size());
        return postList;

    }

    public Post findById(Long id) {
        log.info("게시글 상세 조회 서비스 시작 - ID {}", id);
        Post post = postJpaRepository.findByIdJoinUser(id).orElseThrow(() -> {
            log.warn("게시글 조회 실패 - ID {}", id);
            return new Exception404("게시글을 찾을 수 없습니다");
        });

        log.info("게시글 상세 조회 완료 - 제목 {}", post.getTitle());
        return post;
    }

    @Transactional
    public  Post updateById(Long id, PostRequest.UpdateDTO updateDTO,
                            LoginUser loginUser) {

        Post post = postJpaRepository.findById(id).orElseThrow(() -> {
           log.warn("게시글 조회 실패 - ID {}", id);
           return new Exception404("해당 게시글이 존재하지 않습니다.");
        });

        User user = userJpaRepository.findById(loginUser.getId())
                .orElseThrow(() -> new Exception404("로그인 유저를 찾을 수 없습니다."));

        if(!post.isOwner(loginUser.getId())) {
            throw new Exception403("본인이 작성한 게시글만 수정할 수 잆습니다");
        }

        updateDTO.validate();

        post.setTitle(updateDTO.getTitle());
        post.setContent(updateDTO.getContent());

        return post;
    }

    @Transactional
    public void deletebyId(Long id, LoginUser loginUser){
        Post post = postJpaRepository.findById(id).orElseThrow(() -> {
            throw new Exception404("삭제할 게시글이 없습니다");
        });
        if(! post.isOwner(loginUser.getId())) {
            throw new Exception403("본인이 작성한 게시글만 삭제 가능합니다");
        }
        postJpaRepository.deleteById(id);
        log.info("게시글 삭제 완료 - 게시글 ID {}", id);
    }

    public void checkPostOwner(Long postId, Long userId) {
        Post post = findById(postId);
        if(!post.isOwner(userId)) {
            throw new Exception403("본인 게시글만 수정할 수 있습니다");
        }
    }


}
