package com.itsm.dranswer.board;

import com.itsm.dranswer.config.LoginUser;
import com.itsm.dranswer.config.LoginUserInfo;
import com.itsm.dranswer.ncp.storage.CustomObjectStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    private final CustomObjectStorage customObjectStorage;

    @Autowired
    public BoardRestCtrl(BoardService boardService, CustomObjectStorage customObjectStorage) {
        this.boardService = boardService;
        this.customObjectStorage = customObjectStorage;
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


    @GetMapping(value = "/board/faq/list")
    public ApiResult<Page<FaqDto>> getFaqList(
            @RequestParam(required = false) QuestionType questionType,
            @RequestParam(required = false) String keyword,
            Pageable pageable){

        Page<FaqDto> pageFaqs =
                boardService.pageFaqs(questionType, keyword, pageable);

        return success(pageFaqs);
    }

    @GetMapping(value = "/board/faq/get/{faqSeq:.+(?<!\\.js)$}")
    public ApiResult<FaqDto> getFaq(
            @PathVariable Long faqSeq){

        return success(boardService.faq(faqSeq));
    }

    @PostMapping(value = "/board/faq/delete/{faqSeq:.+(?<!\\.js)$}")
    public ApiResult<FaqDto> deleteFaq(
            @PathVariable Long faqSeq){

        boardService.deleteFaq(faqSeq);

        return success(null);
    }

    @PostMapping(path = "/board/faq/save")
    public ApiResult<FaqDto> saveFaq(
            @RequestBody
                    FaqDto request){

        return success(boardService.saveFaq(request));
    }

    @GetMapping(value = "/board/inquiry/list")
    public ApiResult<Page<InquiryDto>> getInquiryList(
            @RequestParam(required = false) String keyword,
            Pageable pageable){

        Page<InquiryDto> pageInquiries =
                boardService.pageInquiries(keyword, pageable);

        return success(pageInquiries);
    }

    @GetMapping(value = "/board/inquiry/get/{inquirySeq:.+(?<!\\.js)$}")
    public ApiResult<InquiryDto> getInquiry(
            @PathVariable Long inquirySeq){

        return success(boardService.inquiry(inquirySeq));
    }

    @PostMapping(value = "/board/inquiry/delete/{inquirySeq:.+(?<!\\.js)$}")
    public ApiResult<InquiryDto> deleteInquiry(
            @PathVariable Long inquirySeq){

        boardService.deleteInquiry(inquirySeq);

        return success(null);
    }

    @PostMapping(path = "/board/inquiry/save")
    public ApiResult<InquiryDto> saveInquiry(
            @RequestBody
                    InquiryDto request){

        return success(boardService.saveInquiry(request));
    }

    @PostMapping(path = "/board/inquiry/file/upload")
    public ApiResult<InquiryDto> inquiryFileUpload(
            @LoginUser LoginUserInfo loginUserInfo,
            MultipartHttpServletRequest request
    ) throws InterruptedException {

        String sInquirySeq = request.getParameter("inquirySeq");
        List<MultipartFile> multipartFiles = request.getFiles("multipartFile");

        return success(boardService.saveInquiryFile(sInquirySeq, multipartFiles, loginUserInfo));
    }

    @Value("${ncp.laif.end-point}")
    private String endPoint;
    @Value("${ncp.laif.region-name}")
    private String regionName;
    @Value("${ncp.laif.access-key}")
    private String laifAccessKey;
    @Value("${ncp.laif.secret-key}")
    private String laifSecretKey;
    @Value("${ncp.laif.server-access-key}")
    private String laifServerAccessKey;
    @Value("${ncp.laif.server-secret-key}")
    private String laifServerSecretKey;

    @GetMapping(path = "/board/test")
    public ApiResult<String> test(){

        String bucketName = "dranswer-upload-files";

//        customObjectStorage.putBucketCORS(endPoint, regionName, laifServerAccessKey, laifServerSecretKey, bucketName);
//        customObjectStorage.getBucketCORS(endPoint, regionName, laifServerAccessKey, laifServerSecretKey, bucketName);

        return success("OK");
    }
}
