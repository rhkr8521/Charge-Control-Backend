package com.ChargeControl.www.Backend.common.sms;

import com.ChargeControl.www.Backend.api.member.domain.Member;
import com.ChargeControl.www.Backend.api.member.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sms")
public class SmsController {

    private final MemberRepository memberRepository;
    private DefaultMessageService messageService;

    @Value("${coolsms.senderNumber}")
    private String senderNumber;

    @Value("${coolsms.api.Key}")
    private String apiKey;

    @Value("${coolsms.api.Secret}")
    private String apiSecret;

    public SmsController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @PostConstruct
    private void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    @PostMapping("/send-one/{carNumber}")
    public SingleMessageSentResponse sendOne(@PathVariable String carNumber) {
        Member member = memberRepository.findByCarNumber(carNumber);
        Message message = new Message();
        message.setFrom(senderNumber);
        message.setTo(member.getPhoneNumber());
        message.setText("[ChargeControl]\n"+member.getName()+ "님의 주차가 확인되었습니다.\n"
                + " 2시간 내 미출차 시 [환경친화적자동차의 개발 및 보급 촉진에 관한 법률] 제18조 2항 위반으로 과태료가 부과됩니다.");

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);

        return response;
    }
}
