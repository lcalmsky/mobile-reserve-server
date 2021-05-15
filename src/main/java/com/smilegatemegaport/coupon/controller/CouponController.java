package com.smilegatemegaport.coupon.controller;

import com.smilegatemegaport.coupon.domain.dto.CouponRequest;
import com.smilegatemegaport.coupon.domain.entity.Coupon;
import com.smilegatemegaport.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void issueCoupon(@RequestBody CouponRequest couponRequest) {
        couponService.issueCoupon(couponRequest.getPhoneNumber());
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Page<Coupon> getCoupons(Pageable pageable) {
        return couponService.getCoupons(pageable);
    }
}
