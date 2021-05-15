package com.smilegatemegaport.coupon.exception;

import com.smilegatemegaport.coupon.domain.dto.CouponResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public abstract class CouponException extends HttpStatusCodeException {
    protected CouponException(HttpStatus statusCode) {
        super(statusCode);
    }

    abstract public CouponResponse toCouponResponse();
}
