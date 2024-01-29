package ai.ecma.eskizsmsprovider.service;


import ai.ecma.eskizsmsprovider.entity.EskizSendMessage;
import ai.ecma.eskizsmsprovider.payload.*;
import ai.ecma.eskizsmsprovider.repository.EskizSendMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class EskizSmsService {
    private final EskizSendMessageRepository eskizSendMessageRepository;

    private static Map<String, EskizTokenAndExpiredDateDTO> eskizTokens = new HashMap<>();


    public static String loginUrl = "https://notify.eskiz.uz/api/auth/login";
    public static String sendSMSUrl = "https://notify.eskiz.uz/api/message/sms/send";

    private final static RestTemplate restTemplate = new RestTemplate();


    public void sendMessage(String phoneNumber, String message, String login, String password) {
        sendMessage(Set.of(phoneNumber), message, login, password);
    }

    public void sendMessage(Set<String> phoneNumbers, String message, String login, String password) {

        loginEskizService(login, password);

        phoneNumbers = checkPhoneNumbers(phoneNumbers);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(eskizTokens.get(login).getToken());

        List<EskizSendMessage> sendMessages = new ArrayList<>(phoneNumbers.size());
        UUID hash = UUID.randomUUID();

        phoneNumbers.forEach(phoneNumber -> {

            EskizSendSMSDTO smsDTO = new EskizSendSMSDTO(phoneNumber, message);
            HttpEntity<EskizSendSMSDTO> smsDTOHttpEntity = new HttpEntity<>(smsDTO, httpHeaders);
            EskizSendSMSResDTO eskizResponce = restTemplate.postForObject(
                    sendSMSUrl,
                    smsDTOHttpEntity,
                    EskizSendSMSResDTO.class
            );

            if (eskizResponce == null)
                eskizResponce = new EskizSendSMSResDTO();

            sendMessages.add(new EskizSendMessage(phoneNumber, message, true, hash, eskizResponce.getId(), eskizResponce.getMessage(), eskizResponce.getStatus()));
        });

        eskizSendMessageRepository.saveAll(sendMessages);

    }


    private static void loginEskizService(String login, String password) {

        checkEmailAndPassword(login, password);

        EskizTokenAndExpiredDateDTO tokenAndExpiredDateDTO = eskizTokens.get(login);


        if (tokenAndExpiredDateDTO == null || tokenAndExpiredDateDTO.getToken() == null || tokenAndExpiredDateDTO.getToken().isBlank() || LocalDate.now().isAfter(tokenAndExpiredDateDTO.getExpiredDate())) {
            tokenAndExpiredDateDTO = generateNewToken(login, password);
        }

        eskizTokens.put(login, tokenAndExpiredDateDTO);
    }

    private static EskizTokenAndExpiredDateDTO generateNewToken(String login, String password) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        EskizGenerateTokenReqDTO eskizGenerateTokenReqDTO = new EskizGenerateTokenReqDTO(login, password);
        HttpEntity<EskizGenerateTokenReqDTO> requestBody = new HttpEntity<>(eskizGenerateTokenReqDTO, httpHeaders);

        EskizGenerateTokenResDTO tokenDTO = restTemplate.postForObject(loginUrl, requestBody, EskizGenerateTokenResDTO.class);

        if (tokenDTO == null || tokenDTO.getData() == null || tokenDTO.getData().getToken() == null)
            throw new RuntimeException("Error Eskiz Service");

        return new EskizTokenAndExpiredDateDTO(tokenDTO.getData().getToken(), LocalDate.now().plusDays(25));

    }

    private static void checkEmailAndPassword(String login, String password) {
        if (login == null || login.isBlank())
            throw new RuntimeException("Login not blank");

        if (password == null || password.isBlank())
            throw new RuntimeException("Password not blank");
    }


    private static Set<String> checkPhoneNumbers(Set<String> phoneNumbers) {
        if (phoneNumbers == null || phoneNumbers.isEmpty())
            throw new RuntimeException("Phone number is not null");

        return phoneNumbers.stream().map(EskizSmsService::checkPhoneNumber).collect(Collectors.toSet());
    }

    private static String checkPhoneNumber(String phoneNumber) {

        if (phoneNumber == null || phoneNumber.isBlank())
            throw new RuntimeException("Phone number not null");

        phoneNumber = phoneNumber.replaceAll(" ", "");

        if (Pattern.matches("[+]998[0-9]{9}", phoneNumber))
            return phoneNumber.replaceAll("[+]", "");

        if (Pattern.matches("998[0-9]{9}", phoneNumber))
            return phoneNumber;

        if (Pattern.matches("[0-9]{9}", phoneNumber))
            return "998" + phoneNumber;

        else
            throw new RuntimeException(String.format("Phone number is invalid %s %n Phone number format  +998901234567 or 998901234567 or 901234567. %n", phoneNumber));
    }

}
