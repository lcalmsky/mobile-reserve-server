package com.smilegatemegaport.coupon.service;

import com.smilegatemegaport.coupon.domain.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponService {
    void issueCoupon(String phoneNumber);

    Page<Coupon> getCoupons(Pageable pageable);
}
