package com.itsm.dranswer.board;

/*
 * @package : com.itsm.dranswer.board
 * @name : BoardRestCtrl.java
 * @date : 2021-10-08 오후 1:23
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.config.LoginUser;
import com.itsm.dranswer.config.LoginUserInfo;
import com.itsm.dranswer.ncp.storage.CustomObjectStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

import static com.itsm.dranswer.utils.ApiUtils.ApiResult;
import static com.itsm.dranswer.utils.ApiUtils.success;

@RestController
public class BoardRestCtrl {

    private final BoardService boardService;

    private final CustomObjectStorage customObjectStorage;

    @Autowired
    public BoardRestCtrl(BoardService boardService, CustomObjectStorage customObjectStorage) {
        this.boardService = boardService;
        this.customObjectStorage = customObjectStorage;
    }

    /**
     * 공지사항 목록 조회
     * @methodName : getNoticeList
     * @date : 2021-10-08 오후 1:39
     * @author : xeroman.k
     * @param keyword
     * @param pageable
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<org.springframework.data.domain.Page<com.itsm.dranswer.board.NoticeDto>>
     * @throws
     * @modifyed :
     *
    **/
    @GetMapping(value = "/board/notice/list")
    public ApiResult<Page<NoticeDto>> getNoticeList(
            @RequestParam(required = false) String keyword,
            Pageable pageable){

        Page<NoticeDto> pageNotices =
                boardService.pageNotices(keyword, pageable);

        return success(pageNotices);
    }

    /**
     * 공지사항 상세 조회
     * @methodName : getNotice
     * @date : 2021-10-08 오후 1:39
     * @author : xeroman.k
     * @param noticeSeq
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.board.NoticeDto>
     * @throws
     * @modifyed :
     *
    **/
    @GetMapping(value = "/board/notice/get/{noticeSeq:.+(?<!\\.js)$}")
    public ApiResult<NoticeDto> getNotice(
            @PathVariable Long noticeSeq){

        return success(boardService.notice(noticeSeq));
    }

    /**
     * 공지사항 삭제
     * @methodName : deleteNotice
     * @date : 2021-10-08 오후 1:39
     * @author : xeroman.k
     * @param noticeSeq
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.board.NoticeDto>
     * @throws
     * @modifyed :
     *
    **/
    @Secured(value = {"ROLE_ADMIN"})
    @PostMapping(value = "/board/notice/delete/{noticeSeq:.+(?<!\\.js)$}")
    public ApiResult<NoticeDto> deleteNotice(
            @PathVariable Long noticeSeq){

        boardService.deleteNotice(noticeSeq);

        return success(null);
    }

    /**
     * 공지사항 저장
     * @methodName : saveNotice
     * @date : 2021-10-08 오후 1:39
     * @author : xeroman.k
     * @param request
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.board.NoticeDto>
     * @throws
     * @modifyed :
     *
    **/
    @Secured(value = {"ROLE_ADMIN"})
    @PostMapping(path = "/board/notice/save")
    public ApiResult<NoticeDto> saveNotice(
            @RequestBody
                    NoticeDto request){

        return success(boardService.saveNotice(request));
    }

    /**
     * 공지사항 파일 업로드
     * @methodName : noticeFileUpload
     * @date : 2021-10-08 오후 1:39
     * @author : xeroman.k
     * @param loginUserInfo
     * @param request
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.board.NoticeDto>
     * @throws
     * @modifyed :
     *
    **/
    @PostMapping(path = "/board/notice/file/upload")
    public ApiResult<NoticeDto> noticeFileUpload(
            @LoginUser LoginUserInfo loginUserInfo,
            MultipartHttpServletRequest request
    ) throws InterruptedException {

        String sNoticeSeq = request.getParameter("noticeSeq");
        List<MultipartFile> multipartFiles = request.getFiles("multipartFile");

        return success(boardService.saveNoticeFile(sNoticeSeq, multipartFiles, loginUserInfo));
    }

    /**
     * 자주묻는질문 목록 조회
     * @methodName : getFaqList
     * @date : 2021-10-08 오후 1:39
     * @author : xeroman.k
     * @param questionType
     * @param keyword
     * @param pageable
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<org.springframework.data.domain.Page<com.itsm.dranswer.board.FaqDto>>
     * @throws
     * @modifyed :
     *
    **/
    @GetMapping(value = "/board/faq/list")
    public ApiResult<Page<FaqDto>> getFaqList(
            @RequestParam(required = false) QuestionType questionType,
            @RequestParam(required = false) String keyword,
            Pageable pageable){

        Page<FaqDto> pageFaqs =
                boardService.pageFaqs(questionType, keyword, pageable);

        return success(pageFaqs);
    }

    /**
     * 자주묻는질문 상세 조회
     * @methodName : getFaq
     * @date : 2021-10-08 오후 1:40
     * @author : xeroman.k
     * @param faqSeq
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.board.FaqDto>
     * @throws
     * @modifyed :
     *
    **/
    @GetMapping(value = "/board/faq/get/{faqSeq:.+(?<!\\.js)$}")
    public ApiResult<FaqDto> getFaq(
            @PathVariable Long faqSeq){

        return success(boardService.faq(faqSeq));
    }

    /**
     * 자주묻는질문 삭제
     * @methodName : deleteFaq
     * @date : 2021-10-08 오후 1:40
     * @author : xeroman.k
     * @param faqSeq
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.board.FaqDto>
     * @throws
     * @modifyed :
     *
    **/
    @Secured(value = {"ROLE_ADMIN"})
    @PostMapping(value = "/board/faq/delete/{faqSeq:.+(?<!\\.js)$}")
    public ApiResult<FaqDto> deleteFaq(
            @PathVariable Long faqSeq){

        boardService.deleteFaq(faqSeq);

        return success(null);
    }

    /**
     * 자주묻는질문 저장
     * @methodName : saveFaq
     * @date : 2021-10-08 오후 1:40
     * @author : xeroman.k
     * @param request
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.board.FaqDto>
     * @throws
     * @modifyed :
     *
    **/
    @Secured(value = {"ROLE_ADMIN"})
    @PostMapping(path = "/board/faq/save")
    public ApiResult<FaqDto> saveFaq(
            @RequestBody
                    FaqDto request){

        return success(boardService.saveFaq(request));
    }

    /**
     * 질문과답변 목록 조회
     * @methodName : getInquiryList
     * @date : 2021-10-08 오후 1:41
     * @author : xeroman.k
     * @param keyword
     * @param pageable
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<org.springframework.data.domain.Page<com.itsm.dranswer.board.InquiryDto>>
     * @throws
     * @modifyed :
     *
    **/
    @GetMapping(value = "/board/inquiry/list")
    public ApiResult<Page<InquiryDto>> getInquiryList(
            @RequestParam(required = false) String keyword,
            Pageable pageable){

        Page<InquiryDto> pageInquiries =
                boardService.pageInquiries(keyword, pageable);

        return success(pageInquiries);
    }

    /**
     * 질문과답변 상세 조회
     * @methodName : getInquiry
     * @date : 2021-10-08 오후 1:42
     * @author : xeroman.k
     * @param loginUserInfo
     * @param inquirySeq
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.board.InquiryDto>
     * @throws
     * @modifyed :
     *
    **/
    @GetMapping(value = "/board/inquiry/get/{inquirySeq:.+(?<!\\.js)$}")
    public ApiResult<InquiryDto> getInquiry(
            @LoginUser LoginUserInfo loginUserInfo,
            @PathVariable Long inquirySeq){

        return success(boardService.inquiry(loginUserInfo, inquirySeq));
    }

    /**
     * 질문과답변 삭제
     * @methodName : deleteInquiry
     * @date : 2021-10-08 오후 1:42
     * @author : xeroman.k
     * @param inquirySeq
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.board.InquiryDto>
     * @throws
     * @modifyed :
     *
    **/
    @PostMapping(value = "/board/inquiry/delete/{inquirySeq:.+(?<!\\.js)$}")
    public ApiResult<InquiryDto> deleteInquiry(
            @PathVariable Long inquirySeq){

        boardService.deleteInquiry(inquirySeq);

        return success(null);
    }

    /**
     * 질문과답변 저장
     * @methodName : saveInquiry
     * @date : 2021-10-08 오후 1:42
     * @author : xeroman.k
     * @param request
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.board.InquiryDto>
     * @throws
     * @modifyed :
     *
    **/
    @PostMapping(path = "/board/inquiry/save")
    public ApiResult<InquiryDto> saveInquiry(
            @RequestBody
                    InquiryDto request){

        return success(boardService.saveInquiry(request));
    }

    /**
     * 질문과답변 파일 업로드
     * @methodName : inquiryFileUpload
     * @date : 2021-10-08 오후 1:43
     * @author : xeroman.k
     * @param loginUserInfo
     * @param request
     * @return : com.itsm.dranswer.utils.ApiUtils.ApiResult<com.itsm.dranswer.board.InquiryDto>
     * @throws
     * @modifyed :
     *
    **/
    @PostMapping(path = "/board/inquiry/file/upload")
    public ApiResult<InquiryDto> inquiryFileUpload(
            @LoginUser LoginUserInfo loginUserInfo,
            MultipartHttpServletRequest request
    ) throws InterruptedException {

        String sInquirySeq = request.getParameter("inquirySeq");
        List<MultipartFile> multipartFiles = request.getFiles("multipartFile");

        return success(boardService.saveInquiryFile(sInquirySeq, multipartFiles, loginUserInfo));
    }

}
