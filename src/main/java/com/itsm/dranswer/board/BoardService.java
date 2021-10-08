package com.itsm.dranswer.board;

/*
 * @package : com.itsm.dranswer.board
 * @name : BoardService.java
 * @date : 2021-10-08 오후 1:43
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.config.LoginUserInfo;
import com.itsm.dranswer.errors.NotFoundException;
import com.itsm.dranswer.etc.FileUploadResponse;
import com.itsm.dranswer.storage.StorageService;
import com.itsm.dranswer.users.IsYn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service("boardService")
public class BoardService {

    private final NoticeRepo noticeRepo;

    private final NoticeRepoSupport noticeRepoSupport;

    private final NoticeFileRepo noticeFileRepo;

    private final FaqRepo faqRepo;

    private final FaqRepoSupport faqRepoSupport;

    private final InquiryRepo inquiryRepo;

    private final InquiryRepoSupport inquiryRepoSupport;

    private final InquiryFileRepo inquiryFileRepo;

    private final StorageService storageService;

    @Autowired
    public BoardService(NoticeRepo noticeRepo, NoticeRepoSupport noticeRepoSupport, NoticeFileRepo noticeFileRepo, FaqRepo faqRepo, FaqRepoSupport faqRepoSupport, InquiryRepo inquiryRepo, InquiryRepoSupport inquiryRepoSupport, InquiryFileRepo inquiryFileRepo, StorageService storageService) {
        this.noticeRepo = noticeRepo;
        this.noticeRepoSupport = noticeRepoSupport;
        this.noticeFileRepo = noticeFileRepo;
        this.faqRepo = faqRepo;
        this.faqRepoSupport = faqRepoSupport;
        this.inquiryRepo = inquiryRepo;
        this.inquiryRepoSupport = inquiryRepoSupport;
        this.inquiryFileRepo = inquiryFileRepo;
        this.storageService = storageService;
    }

    /**
     * 공지사항 페이지 조회
     * @methodName : pageNotices
     * @date : 2021-10-08 오후 1:44
     * @author : xeroman.k
     * @param keyword
     * @param pageable
     * @return : org.springframework.data.domain.Page<com.itsm.dranswer.board.NoticeDto>
     * @throws
     * @modifyed :
     *
    **/
    public Page<NoticeDto> pageNotices(String keyword, Pageable pageable) {
        keyword = keyword==null?"":keyword;
        Page<Notice> pageNotices = noticeRepo.findByTitleContainsOrderByImportantYnDescCreatedDateDesc(keyword, pageable);
        return pageNotices.map(NoticeDto::new);
    }

    /**
     * 공지사항 저장
     * @methodName : saveNotice
     * @date : 2021-10-08 오후 1:44
     * @author : xeroman.k
     * @param noticeDto
     * @return : com.itsm.dranswer.board.NoticeDto
     * @throws
     * @modifyed :
     *
    **/
    public NoticeDto saveNotice(NoticeDto noticeDto) {

        Notice notice = null;

        List<NoticeFileDto> noticeFileDtos = noticeDto.getNoticeFiles();

        if(noticeDto.getNoticeSeq() == null){
            notice = new Notice(noticeDto);
            notice = noticeRepo.save(notice);
        }else{
            notice = noticeRepo.findById(noticeDto.getNoticeSeq())
                    .orElseThrow(() -> new NotFoundException("존재하지 않는 정보 입니다."));
            notice.update(noticeDto);

            List<NoticeFile> noticeFiles = notice.getNoticeFiles();
            for(NoticeFile noticeFile: noticeFiles){

                if(!noticeFileDtos.stream().anyMatch(e->e.getFileSeq().equals(noticeFile.getFileSeq()))){
                    noticeFileRepo.deleteById(noticeFile.getFileSeq());
                }
            }
        }
        return notice.convertDto();
    }

    /**
     * 공지사항 상세 조회
     * @methodName : notice
     * @date : 2021-10-08 오후 1:44
     * @author : xeroman.k
     * @param noticeSeq
     * @return : com.itsm.dranswer.board.NoticeDto
     * @throws
     * @modifyed :
     *
    **/
    public NoticeDto notice(Long noticeSeq) {

        Notice notice = noticeRepo.findById(noticeSeq)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 정보 입니다."));

        notice.incCnt();

        NoticeDto prev = noticeRepoSupport.prev(noticeSeq);
        NoticeDto next = noticeRepoSupport.next(noticeSeq);

        NoticeDto noticeDto = notice.convertDto();

        noticeDto.setPrevAndNext(prev, next);

        return noticeDto;

    }

    /**
     * 공지사항 삭제
     * @methodName : deleteNotice
     * @date : 2021-10-08 오후 1:44
     * @author : xeroman.k
     * @param noticeSeq
     * @return : void
     * @throws
     * @modifyed :
     *
    **/
    public void deleteNotice(Long noticeSeq) {
        noticeFileRepo.deleteByNoticeSeq(noticeSeq);
        noticeRepo.deleteById(noticeSeq);
    }

    /**
     * 공지사항 파일 저장
     * @methodName : saveNoticeFile
     * @date : 2021-10-08 오후 1:44
     * @author : xeroman.k
     * @param sNoticeSeq
     * @param multipartFiles
     * @param loginUserInfo
     * @return : com.itsm.dranswer.board.NoticeDto
     * @throws
     * @modifyed :
     *
    **/
    public NoticeDto saveNoticeFile(String sNoticeSeq, List<MultipartFile> multipartFiles, LoginUserInfo loginUserInfo)
            throws InterruptedException {

        Long noticeSeq = null;

        try{
            noticeSeq = Long.parseLong(sNoticeSeq);
        }catch (NumberFormatException e){
            throw new NumberFormatException("공지사항 번호의 형식이 올바르지 않습니다.");
        }

        SimpleDateFormat fm = new SimpleDateFormat("yyyyMMdd");
        String yyyyMMdd = fm.format(new Date());
        String folderName = "notice/" + yyyyMMdd + "/" + noticeSeq + "/" + loginUserInfo.getUserSeq() + "/";

        FileUploadResponse response = storageService.uploadBoardFile(folderName, multipartFiles);

        for(FileUploadResponse.FileObject object : response.getListObject()){

            NoticeFile noticeFile = new NoticeFile(noticeSeq, object);
            noticeFileRepo.save(noticeFile);

        }

        Notice notice = noticeRepo.findById(noticeSeq).orElseThrow(()-> new NotFoundException("존재하지 않는 정보 입니다."));

        return notice.convertDto();
    }

    /**
     * 자주묻는질문 페이지 조회
     * @methodName : pageFaqs
     * @date : 2021-10-08 오후 1:44
     * @author : xeroman.k
     * @param questionType
     * @param keyword
     * @param pageable
     * @return : org.springframework.data.domain.Page<com.itsm.dranswer.board.FaqDto>
     * @throws
     * @modifyed :
     *
    **/
    public Page<FaqDto> pageFaqs(QuestionType questionType, String keyword, Pageable pageable) {

        return faqRepoSupport.searchAll(questionType, keyword, pageable);
    }

    /**
     * 자주묻는질문 상세조회
     * @methodName : faq
     * @date : 2021-10-08 오후 1:44
     * @author : xeroman.k
     * @param faqSeq
     * @return : com.itsm.dranswer.board.FaqDto
     * @throws
     * @modifyed :
     *
    **/
    public FaqDto faq(Long faqSeq) {

        FaqDto faqDto = faqRepo.findById(faqSeq).orElseThrow(()->new NotFoundException("존재하지 않는 정보 입니다")).convertDto();

        FaqDto prev = faqRepoSupport.prev(faqSeq);
        FaqDto next = faqRepoSupport.next(faqSeq);

        faqDto.setPrevAndNext(prev, next);

        return faqDto;
    }

    /**
     * 자주묻는질문 삭제
     * @methodName : deleteFaq
     * @date : 2021-10-08 오후 1:45
     * @author : xeroman.k
     * @param faqSeq
     * @return : void
     * @throws
     * @modifyed :
     *
    **/
    public void deleteFaq(Long faqSeq) {
        faqRepo.deleteById(faqSeq);
    }

    /**
     * 자주묻는질문 저장
     * @methodName : saveFaq
     * @date : 2021-10-08 오후 1:45
     * @author : xeroman.k
     * @param faqDto
     * @return : com.itsm.dranswer.board.FaqDto
     * @throws
     * @modifyed :
     *
    **/
    public FaqDto saveFaq(FaqDto faqDto) {

        Faq faq = null;
        if(faqDto.getFaqSeq() == null){
            faq = new Faq(faqDto);
            faq = faqRepo.save(faq);
        }else{
            faq = faqRepo.findById(faqDto.getFaqSeq()).orElseThrow(()->new NotFoundException("존재하지 않는 정보 입니다."));
            faq.update(faqDto);
        }


        return faq.convertDto();
    }

    /**
     * 질문과답변 페이지 조회
     * @methodName : pageInquiries
     * @date : 2021-10-08 오후 1:45
     * @author : xeroman.k
     * @param keyword
     * @param pageable
     * @return : org.springframework.data.domain.Page<com.itsm.dranswer.board.InquiryDto>
     * @throws
     * @modifyed :
     *
    **/
    public Page<InquiryDto> pageInquiries(String keyword, Pageable pageable) {
        return inquiryRepoSupport.searchAll(keyword, pageable);
    }

    /**
     * 질문과답변 상세조회
     * @methodName : inquiry
     * @date : 2021-10-08 오후 1:45
     * @author : xeroman.k
     * @param loginUserInfo
     * @param inquirySeq
     * @return : com.itsm.dranswer.board.InquiryDto
     * @throws
     * @modifyed :
     *
    **/
    public InquiryDto inquiry(LoginUserInfo loginUserInfo, Long inquirySeq) {

        InquiryDto inquiryDto = inquiryRepoSupport.findOne(inquirySeq);

        boolean isAdmin = loginUserInfo.isAdmin();

        if(inquiryDto.getPublicYn() == IsYn.N && !isAdmin){
            if(inquiryDto.getCreatedBy() != loginUserInfo.getUserSeq()){
                throw new IllegalArgumentException("열람하실 수 없는 글 입니다.");
            }
        }

        List<InquiryFile> inquiryFiles = inquiryFileRepo.findByInquirySeq(inquirySeq);
        List<InquiryFileDto>  inquiryFileDto = inquiryFiles.stream().map(InquiryFileDto::new).collect(Collectors.toList());
        inquiryDto.setInquiryFiles(inquiryFileDto);

        List<Inquiry> children = inquiryRepo.findByOrgInquirySeqOrderByCreatedDateDesc(inquirySeq);
        inquiryDto.setChildren(children.stream().map(InquiryDto::new).collect(Collectors.toList()));

        return inquiryDto;
    }

    /**
     * 질문과답변 삭제
     * @methodName : deleteInquiry
     * @date : 2021-10-08 오후 1:45
     * @author : xeroman.k
     * @param inquirySeq
     * @return : void
     * @throws
     * @modifyed :
     *
    **/
    public void deleteInquiry(Long inquirySeq) {
        Inquiry inquiry = inquiryRepo.findById(inquirySeq).orElseThrow(()->new NotFoundException("존재하지 않는 정보 입니다."));

        List<Inquiry> children = inquiry.getChildren();
        for(Inquiry child : children){
            inquiryFileRepo.deleteByInquirySeq(child.getInquirySeq());
        }

        inquiryRepo.deleteAll(children);
        inquiryFileRepo.deleteByInquirySeq(inquirySeq);
        inquiryRepo.deleteById(inquirySeq);
    }

    /**
     * 질문과답변 저장
     * @methodName : saveInquiry
     * @date : 2021-10-08 오후 1:45
     * @author : xeroman.k
     * @param inquiryDto
     * @return : com.itsm.dranswer.board.InquiryDto
     * @throws
     * @modifyed :
     *
    **/
    public InquiryDto saveInquiry(InquiryDto inquiryDto) {
        Inquiry inquiry = null;

        List<InquiryFileDto> inquiryFileDtos = inquiryDto.getInquiryFiles();

        if(inquiryDto.getInquirySeq() == null){
            inquiry = new Inquiry(inquiryDto);
            inquiry = inquiryRepo.save(inquiry);

            if(inquiry.getOrgInquirySeq() != null){

            }

        }else{
            inquiry = inquiryRepo.findById(inquiryDto.getInquirySeq())
                    .orElseThrow(() -> new NotFoundException("존재하지 않는 정보 입니다."));
            inquiry.update(inquiryDto);

            List<InquiryFile> inquiryFiles = inquiry.getInquiryFiles();
            for(InquiryFile inquiryFile: inquiryFiles){

                if(!inquiryFileDtos.stream().anyMatch(e->e.getFileSeq().equals(inquiryFile.getFileSeq()))){
                    inquiryFileRepo.deleteById(inquiryFile.getFileSeq());
                }
            }
        }
        return inquiry.convertDto();
    }

    /**
     * 질문과답변 파일 저장
     * @methodName : saveInquiryFile
     * @date : 2021-10-08 오후 1:46
     * @author : xeroman.k
     * @param sInquirySeq
     * @param multipartFiles
     * @param loginUserInfo
     * @return : com.itsm.dranswer.board.InquiryDto
     * @throws
     * @modifyed :
     *
    **/
    public InquiryDto saveInquiryFile(String sInquirySeq, List<MultipartFile> multipartFiles, LoginUserInfo loginUserInfo) throws InterruptedException {

        Long inquirySeq = null;

        try{
            inquirySeq = Long.parseLong(sInquirySeq);
        }catch (NumberFormatException e){
            throw new NumberFormatException("문의하기 번호의 형식이 올바르지 않습니다.");
        }

        SimpleDateFormat fm = new SimpleDateFormat("yyyyMMdd");
        String yyyyMMdd = fm.format(new Date());
        String folderName = "inquiry/" + yyyyMMdd + "/" + inquirySeq + "/" + loginUserInfo.getUserSeq() + "/";

        FileUploadResponse response = storageService.uploadBoardFile(folderName, multipartFiles);

        for(FileUploadResponse.FileObject object : response.getListObject()){

            InquiryFile inquiryFile = new InquiryFile(inquirySeq, object);
            inquiryFileRepo.save(inquiryFile);

        }

        Inquiry inquiry = inquiryRepo.findById(inquirySeq).orElseThrow(()-> new NotFoundException("존재하지 않는 정보 입니다."));

        return inquiry.convertDto();

    }
}
