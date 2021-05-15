package com.smilegatemegaport.coupon.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Coupon implements Persistable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long sequence;
    private String phoneNumber;
    private String couponNumber;
    private LocalDateTime createTime;

    @Override
    @Transient
    @JsonIgnore
    public String getId() {
        return this.phoneNumber;
    }

    @Override
    @Transient
    @JsonIgnore
    public boolean isNew() {
        return null == getId();
    }

    @PrePersist
    protected void onUpdate() {
        createTime = LocalDateTime.now();
    }
}
