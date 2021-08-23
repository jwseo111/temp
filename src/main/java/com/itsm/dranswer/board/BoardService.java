package com.itsm.dranswer.board;

import com.itsm.dranswer.config.LoginUserInfo;
import com.itsm.dranswer.errors.NotFoundException;
import com.itsm.dranswer.etc.FileUploadResponse;
import com.itsm.dranswer.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    public Page<NoticeDto> pageNotices(String keyword, Pageable pageable) {
        keyword = keyword==null?"":keyword;
        Page<Notice> pageNotices = noticeRepo.findByTitleContainsOrderByCreatedDateDesc(keyword, pageable);
        return pageNotices.map(NoticeDto::new);
    }

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

    public void deleteNotice(Long noticeSeq) {
        noticeFileRepo.deleteByNoticeSeq(noticeSeq);
        noticeRepo.deleteById(noticeSeq);
    }

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

    public Page<FaqDto> pageFaqs(QuestionType questionType, String keyword, Pageable pageable) {

        return faqRepoSupport.searchAll(questionType, keyword, pageable);
    }

    public FaqDto faq(Long faqSeq) {

        FaqDto faqDto = faqRepo.findById(faqSeq).orElseThrow(()->new NotFoundException("존재하지 않는 정보 입니다")).convertDto();

        FaqDto prev = faqRepoSupport.prev(faqSeq);
        FaqDto next = faqRepoSupport.next(faqSeq);

        faqDto.setPrevAndNext(prev, next);

        return faqDto;
    }

    public void deleteFaq(Long faqSeq) {
        faqRepo.deleteById(faqSeq);
    }

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

    public Page<InquiryDto> pageInquiries(String keyword, Pageable pageable) {
        return inquiryRepoSupport.searchAll(keyword, pageable);
    }

    public InquiryDto inquiry(Long inquirySeq) {

        return inquiryRepoSupport.findOne(inquirySeq);
    }

    public void deleteInquiry(Long inquirySeq) {
        inquiryFileRepo.deleteByInquirySeq(inquirySeq);
        inquiryRepo.deleteById(inquirySeq);
    }

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
