package com.itsm.dranswer.board;

import com.itsm.dranswer.config.LoginUser;
import com.itsm.dranswer.config.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

import static com.itsm.dranswer.utils.ApiUtils.ApiResult;
import static com.itsm.dranswer.utils.ApiUtils.success;

@RestController
public class BoardRestCtrl {

    private final BoardService boardService;

    @Autowired
    public BoardRestCtrl(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping(value = "/board/notice/list")
    public ApiResult<Page<NoticeDto>> getNoticeList(
            @RequestParam(required = false) String keyword,
            Pageable pageable){

        Page<NoticeDto> pageNotices =
                boardService.pageNotices(keyword, pageable);

        return success(pageNotices);
    }

    @GetMapping(value = "/board/notice/get/{noticeSeq:.+(?<!\\.js)$}")
    public ApiResult<NoticeDto> getNotice(
            @PathVariable Long noticeSeq){

        return success(boardService.notice(noticeSeq));
    }

    @PostMapping(value = "/board/notice/delete/{noticeSeq:.+(?<!\\.js)$}")
    public ApiResult<NoticeDto> deleteNotice(
            @PathVariable Long noticeSeq){

        boardService.deleteNotice(noticeSeq);

        return success(null);
    }

    @PostMapping(path = "/board/notice/save")
    public ApiResult<NoticeDto> saveNotice(
            @RequestBody
                    NoticeDto request){

        return success(boardService.saveNotice(request));
    }

    @PostMapping(path = "/board/notice/file/upload")
    public ApiResult<NoticeDto> noticeFileUpload(
            @LoginUser LoginUserInfo loginUserInfo,
            MultipartHttpServletRequest request
    ) throws InterruptedException {

        String sNoticeSeq = request.getParameter("noticeSeq");
        List<MultipartFile> multipartFiles = request.getFiles("multipartFile");

        return success(boardService.saveNoticeFile(sNoticeSeq, multipartFiles, loginUserInfo));
    }
}
