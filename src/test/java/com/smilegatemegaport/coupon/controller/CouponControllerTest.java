package com.smilegatemegaport.coupon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smilegatemegaport.coupon.domain.dto.CouponRequest;
import com.smilegatemegaport.coupon.domain.entity.Coupon;
import com.smilegatemegaport.coupon.exception.CouponAlreadyIssuedException;
import com.smilegatemegaport.coupon.service.CouponService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureRestDocs(outputDir = "docs")
@WebMvcTest(CouponController.class)
class CouponControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CouponService couponService;

    @Test
    @DisplayName("쿠폰 발급 테스트 - 정상")
    public void givenRequest_whenIssueCoupon_thenReturnsSuccessResponseWithoutBody() throws Exception {
        CouponRequest couponRequest = new CouponRequest();
        couponRequest.setPhoneNumber("01012345678");

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

    @Test
    @DisplayName("쿠폰 발급 테스트 - 이미 발급된 경우")
    public void givenRequest_whenIssueCoupon_thenReturnsErrorResponseWithBody() throws Exception {
        CouponRequest couponRequest = new CouponRequest();
        couponRequest.setPhoneNumber("01012345678");

        Coupon coupon = Coupon.builder()
                .issuedDate(LocalDateTime.now())
                .couponNumber("Hk1K-4Z3d-o8Q")
                .build();

        doThrow(CouponAlreadyIssuedException.thrown(coupon))
                .when(couponService).issueCoupon(couponRequest.getPhoneNumber());

        ResultActions resultActions = mockMvc.perform(post("/api/v1/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(couponRequest))
        );

        resultActions.andExpect(status().isConflict())
                .andDo(document("post-coupon-409",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("휴대폰 번호")
                        ),
                        responseFields(
                                fieldWithPath("couponNumber").type(JsonFieldType.STRING).description("이미 발급된 쿠폰 번호"),
                                fieldWithPath("issuedDate").type(JsonFieldType.STRING).description("쿠폰 발급 일시")
                        )
                ));
    }

    @Test
    @DisplayName("쿠폰 발급 내역 조회")
    public void whenGetCoupons_thenCorrect() throws Exception {

        couponService.issueCoupon("01012345678");
        couponService.issueCoupon("01098765432");

//        PageRequest pageRequest = PageRequest.of(1, 10);
//        when(couponService.getCoupons(pageRequest)).
//                thenReturn(new PageImpl<>(Arrays.asList(
//                        Coupon.builder()
//                                .phoneNumber("01012345678")
//                                .issuedDate(LocalDateTime.now())
//                                .sequence(1L)
//                                .couponNumber("Hk1K-4Z3d-o8Q")
//                                .build(),
//                        Coupon.builder()
//                                .phoneNumber("01098765432")
//                                .issuedDate(LocalDateTime.now().minus(Duration.ofMinutes(10)))
//                                .sequence(2L)
//                                .couponNumber("nvHM-i6qV-K4N")
//                                .build()
//                ), pageRequest, 2));

        ResultActions resultActions = mockMvc.perform(get("/api/v1/coupon")
                .accept(MediaType.APPLICATION_JSON)
                .queryParam("page", "1")
                .queryParam("size", "10")
        );

        resultActions.andExpect(status().isOk())
                .andDo(document("get-coupon",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("조회할 페이지").optional(),
                                parameterWithName("size").description("페이지에 노출될 항목 갯수").optional()
                        )/*,
                        responseFields(
                                fieldWithPath("content[]").type(JsonFieldType.ARRAY).description("쿠폰 내역 리스트"),
                                fieldWithPath("content[].sequence").type(JsonFieldType.NUMBER).description("쿠폰 순번"),
                                fieldWithPath("content[].phoneNumber").type(JsonFieldType.STRING).description("휴대폰 번호"),
                                fieldWithPath("content[].couponNUmber").type(JsonFieldType.STRING).description("쿠폰 번호"),
                                fieldWithPath("content[].issuedDate").type(JsonFieldType.STRING).description("쿠폰 발급 일시"),
                                fieldWithPath("pageable").type(JsonFieldType.STRING).description("페이징 정보"),
                                fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                                fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수"),
                                fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("전체 항목 갯수"),
                                fieldWithPath("number").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                fieldWithPath("size").type(JsonFieldType.NUMBER).description("현재 페이지 사이즈"),
                                fieldWithPath("sort").type(JsonFieldType.OBJECT).description("정렬 정보"),
                                fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("항목 갯수"),
                                fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"),
                                fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("조회 결과가 비어있는지 여부")
                        )*/
                ));
    }
}
