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

    private final StorageService storageService;

    @Autowired
    public BoardService(NoticeRepo noticeRepo, NoticeRepoSupport noticeRepoSupport, NoticeFileRepo noticeFileRepo, StorageService storageService) {
        this.noticeRepo = noticeRepo;
        this.noticeRepoSupport = noticeRepoSupport;
        this.noticeFileRepo = noticeFileRepo;
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

        Long noticeSeq = Long.parseLong(sNoticeSeq);

        SimpleDateFormat fm = new SimpleDateFormat("yyyyMMdd");
        String yyyyMMdd = fm.format(new Date());
        String folderName = "notice/" + yyyyMMdd + "/" + loginUserInfo.getUserSeq();

        FileUploadResponse response = storageService.uploadBoardFile(folderName, multipartFiles);

        for(FileUploadResponse.FileObject object : response.getListObject()){

            NoticeFile noticeFile = new NoticeFile(noticeSeq, object);
            noticeFileRepo.save(noticeFile);

        }

        Notice notice = noticeRepo.findById(noticeSeq).orElseThrow(()-> new NotFoundException("존재하지 않는 정보 입니다."));

        return notice.convertDto();
    }
}
