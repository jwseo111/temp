package com.itsm.dranswer.board;

/*
 * @package : com.itsm.dranswer.etc
 * @name : BoardPage.java
 * @date : 2021-08-12 오후 3:30
 * @author : lsj
 * @version : 1.0.0
 * @modifyed :
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardPage {

    private final Logger log = LoggerFactory.getLogger(getClass());

    // 공지사항 리스트
    @GetMapping(value = "/board/notice/main")
    public String boardList() {

        return "pages/board/noticeList";
    }
    // 공지사항 상세
    @GetMapping(value = "/board/notice/view")
    public String boardView() {

        return "pages/board/noticeView";
    }
    // 공지사항 등록
    @GetMapping(value = "/board/notice/reg")
    public String boardReg() {

        return "pages/board/noticeReg";
    }
    // 문의하기 리스트
    @GetMapping(value = "/board/qna/main")
    public String qnaList() {

        return "pages/board/qnaList";
    }
    // 문의하기 상세
    @GetMapping(value = "/board/qna/view")
    public String qnaView() {

        return "pages/board/qnaView";
    }
    // 문의하기 등록
    @GetMapping(value = "/board/qna/reg")
    public String qnaReg() {

        return "pages/board/qnaReg";
    }
    // FAQ 리스트
    @GetMapping(value = "/board/faq/main")
    public String faqList() {

        return "pages/board/faqList";
    }
    // FAQ 상세
    @GetMapping(value = "/board/faq/view")
    public String faqView() {

        return "pages/board/faqView";
    }
    // FAQ 등록
    @GetMapping(value = "/board/faq/reg")
    public String faqReg() {

        return "pages/board/faqReg";
    }
}
