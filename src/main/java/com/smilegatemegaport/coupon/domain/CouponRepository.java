package com.smilegatemegaport.coupon.domain;

import com.smilegatemegaport.coupon.domain.entity.Coupon;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends PagingAndSortingRepository<Coupon, String> {
}