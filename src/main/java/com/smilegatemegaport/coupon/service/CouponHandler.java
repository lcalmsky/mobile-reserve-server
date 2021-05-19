package com.smilegatemegaport.coupon.service;

import com.smilegatemegaport.coupon.domain.CouponRepository;
import com.smilegatemegaport.coupon.domain.entity.Coupon;
import com.smilegatemegaport.coupon.exception.CouponAlreadyIssuedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponHandler implements CouponService {

    private static final List<String> COMBINATION_OF_LETTERS = Arrays.asList(
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
    );

    private final CouponRepository couponRepository;

    @Override
    public void issueCoupon(String phoneNumber) {
        Coupon coupon = couponRepository.findByPhoneNumber(phoneNumber);
        if (coupon != null) throw CouponAlreadyIssuedException.thrown(coupon);
        coupon = Coupon.builder()
                .couponNumber(generateCouponNumber())
                .phoneNumber(phoneNumber)
                .build();
        couponRepository.save(coupon);
    }

    private String generateCouponNumber() {
        Collections.shuffle(COMBINATION_OF_LETTERS);
        String couponNumber = String.join("", COMBINATION_OF_LETTERS.subList(0, 11));
        return String.format("%s-%s-%s", couponNumber.substring(0, 4), couponNumber.substring(4, 8), couponNumber.substring(8));
    }

    @Override
    public Page<Coupon> getCoupons(Pageable pageable) {
        return couponRepository.findAll(pageable);
    }
}
