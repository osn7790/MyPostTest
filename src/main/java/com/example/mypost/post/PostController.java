package com.example.mypost.post;

import com.example.mypost._core.utils.Define;
import com.example.mypost.user.LoginUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;


    @GetMapping("/post/{id}/post-update")
    public String updateForm(@PathVariable(name = "id") Long postId, HttpServletRequest res,
                             @RequestAttribute(Define.LOGIN_USER) LoginUser loginUser) {
        postService.checkPostOwner(postId, loginUser.getId());
        return "post/update-form";
    }

    @PostMapping("/post/{id}/update-form")
    public String update(@PathVariable(name = "id") Long postId, PostRequest.UpdateDTO reqDTO,
                         @RequestAttribute(Define.LOGIN_USER) LoginUser loginUser) {

        log.info("게시글 수정 기능 요청 - boardId : {}, 새 제목 {} ", postId, reqDTO.getTitle());

        reqDTO.validate();

        postService.updateById(postId, reqDTO, loginUser);

        return "redrect:/post/" + postId;
    }



    @PostMapping("/post/{id}/delete")
    public String delete(@PathVariable(name = "id") long id,  @RequestAttribute(Define.LOGIN_USER) LoginUser loginUser) {

        log.info("게시글 삭제 요청 - ID : {}", id);



        postService.deletebyId(id,loginUser); // Ctrl + space 하면 나옴

        return "redirect:/";
    }


    @GetMapping("/post/save-form")
    public String saveForm(HttpSession session) {

        log.info("게시글 저장 요청 ");
        // 권한 체크 -> 로그인된 사용자만 이동
        return "board/save-form";
    }

    @PostMapping("/post/save")
    public String save(PostRequest.SaveDTO reqDTO, @RequestAttribute(Define.LOGIN_USER) LoginUser loginUser) {

        log.info("게시글 작성 기능 요청 ");

        reqDTO.validate();
        postService.save(reqDTO, loginUser);

        return "redirect:/";
    }


    @GetMapping("/post")
    public String index(Model model) {
        log.info("메인 페이지 요청");

        List<Post> postList = postService.findAll();
        model.addAttribute("postList", postList);
        return "index";
    }



}
