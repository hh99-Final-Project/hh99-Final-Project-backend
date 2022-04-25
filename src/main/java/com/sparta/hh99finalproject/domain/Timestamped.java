package com.sparta.hh99finalproject.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass // Entity가 자동으로 컬럼으로 인식합니다.
// 변화하는 것을 지켜보다가 변화 있을 때 자동으로 업데이트
@EntityListeners(AuditingEntityListener.class) // 생성/변경 시간을 자동으로 업데이트합니다.
public abstract class Timestamped { // abstract -> 직접 new로 못쓰고 다른데서 상속 되어야만 빵틀 만들수 있는 클래스

    @CreatedDate // 생성시간
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;
}