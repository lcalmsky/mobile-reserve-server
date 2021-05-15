package com.smilegatemegaport.coupon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smilegatemegaport.coupon.domain.dto.CouponRequest;
import com.smilegatemegaport.coupon.service.CouponService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureRestDocs
@WebMvcTest(CouponController.class)
class CouponControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CouponService couponService;

    @Test
    @DisplayName("쿠폰 발급 테스트")
    public void issueCouponTest() throws Exception {
        CouponRequest couponRequest = new CouponRequest();
        couponRequest.setPhoneNumber("010-1234-5678");

        ResultActions resultActions = mockMvc.perform(post("/api/v1/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(couponRequest))
        );

        resultActions.andExpect(status().isOk())
                .andDo(document("post-coupon",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("휴대폰 번호"))
                ));
    }

}
