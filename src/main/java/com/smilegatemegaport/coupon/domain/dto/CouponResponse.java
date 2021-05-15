package com.smilegatemegaport.coupon.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CouponResponse {
    private String couponNumber;
    private LocalDateTime issuedDate;
}
