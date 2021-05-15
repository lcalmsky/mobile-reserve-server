package com.smilegatemegaport.coupon.service;

import com.smilegatemegaport.coupon.domain.CouponRepository;
import com.smilegatemegaport.coupon.domain.entity.Coupon;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponHandler implements CouponService {

    private static final List<String> COMBINATION_OF_LETTERS;

    static {
        COMBINATION_OF_LETTERS = new ArrayList<>();
        IntStream.rangeClosed(0, 9)
                .boxed()
                .map(String::valueOf)
                .forEach(COMBINATION_OF_LETTERS::add);
        IntStream.rangeClosed(0, 26)
                .mapToObj(i -> ((char) (i + 'a')))
                .map(String::valueOf)
                .forEach(COMBINATION_OF_LETTERS::add);
        IntStream.rangeClosed(0, 26)
                .mapToObj(i -> ((char) (i + 'A')))
                .map(String::valueOf)
                .forEach(COMBINATION_OF_LETTERS::add);
    }

    private final CouponRepository couponRepository;

    @Override
    public void issueCoupon(String phoneNumber) {
        Coupon coupon = Coupon.builder()
                .couponNumber(generateCouponNumber())
                .phoneNumber(phoneNumber)
                .build();
        couponRepository.save(coupon);
    }

    private String generateCouponNumber() {
        Collections.shuffle(COMBINATION_OF_LETTERS);
        String couponNumber = String.join("", COMBINATION_OF_LETTERS.subList(0, 11));
        log.info(couponNumber);
        return String.format("%s-%s-%s", couponNumber.substring(0, 4), couponNumber.substring(4, 8), couponNumber.substring(8));
    }

    @Override
    public Page<Coupon> getCoupons(Pageable pageable) {
        return couponRepository.findAll(pageable);
    }
}