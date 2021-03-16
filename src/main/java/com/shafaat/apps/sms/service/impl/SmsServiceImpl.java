package com.shafaat.apps.sms.service.impl;

import com.shafaat.apps.sms.domain.Config;
import com.shafaat.apps.sms.repository.ConfigRepository;
import com.shafaat.apps.sms.service.GsmUtils;
import com.shafaat.apps.sms.service.SmsService;
import com.shafaat.apps.sms.domain.Sms;
import com.shafaat.apps.sms.repository.SmsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.DatatypeConverter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Sms}.
 */
@Service
@Transactional
public class SmsServiceImpl implements SmsService {

    private final Logger log = LoggerFactory.getLogger(SmsServiceImpl.class);

    private final SmsRepository smsRepository;

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private GsmUtils gsmUtils;

    public SmsServiceImpl(SmsRepository smsRepository) {
        this.smsRepository = smsRepository;
    }

    @Override
    public Sms save(Sms sms) {
        log.debug("Request to save Sms : {}", sms);
        return smsRepository.save(sms);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Sms> findAll(Pageable pageable) {
        log.debug("Request to get all Sms");
        return smsRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Sms> findOne(Long id) {
        log.debug("Request to get Sms : {}", id);
        return smsRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sms : {}", id);
        smsRepository.deleteById(id);
    }

    // 60 sec * 15 = 15 min
    @Scheduled(fixedRate = 60000)
    public void scanIncomingSms() {

        try{
            Optional<Config> configVal = configRepository.findByConfigName("SERIAL_PORT");

            gsmUtils.initialize(configVal.get().getConfigValue());
            gsmUtils.executeAT("AT+CMGF=1"); // To set text mode
            gsmUtils.executeAT("AT+CSCS=\"UCS2\""); // To set Hex mode
            gsmUtils.executeAT("AT+CPMS=\"SM\",\"ME\""); // To read from both sim and memory


            String result = gsmUtils.executeAT("AT+CMGL=\"ALL\"");
            String[] strs = result.trim().replace("\"", "").split("(?:,)|(?:\n)");


            /**
             // This will be split in the multiples of 7. for example

             int i=1;
             for(String s: strs){
             System.out.println(i+"::: " +s );
             i++;
             }

             1::: +CMGL: 0
             2::: REC READ
             3::: 00410044002D00500048004F004E00500045
             4:::
             5::: 21/03/12
             6::: 20:30:02+32
             7::: 0033003800340036003800200069007300200079006F007500720020006F006E0065002000740069006D0065002000700061007300730077006F0072006400200074006F002000700072006F00630065006500640020006F006E002000500068006F006E006500500065002E002000490074002000690073002000760061006C0069006400200066006F00720020003100300020006D0069006E0075007400650073002E00200044006F0020006E006F007400200073006800610072006500200079006F007500720020004F005400500020007700690074006800200061006E0079006F006E0065002E

             8::: +CMGL: 1
             9::: REC READ
             10::: 00410041002D004100490052004700530046
             11:::
             12::: 21/03/12
             13::: 20:02:33+22
             14::: 004E006F0074002000610062006C006500200074006F002000630061006C006C003F0020004F007500740067006F0069006E0067002000730065007200760069006300650073002000610072006500200069006E00610063007400690076006500200066006F007200200038003600380036003300340035003800320030002E00200054006F00200072006500730074006100720074002C0020007200650063006800610072006700650020004E004F00570020007700690074006800200061006E002000410069007200740065006C00200055006E006C0069006D00690074006500640020005000610063006B00200075002E00610069007200740065006C002E0069006E002F0046004400500031002000490067006E006F007200650020006900660020007200650063006800610072006700650064

             15:::
             16::: OK
             */
            int groupLength = 7; // 1 SMS after the split logic above is split across 7 parts
            int totalSmsReceived = strs.length / groupLength;

            List<Sms> smsList = new ArrayList<>();
            for (int i = 0; i < totalSmsReceived; i++) {

                Sms sms = new Sms();

                System.out.println("------------");
                System.out.println("SMS# " + i);

                String from = new String(DatatypeConverter.parseHexBinary(strs[(i * groupLength)+ 2].trim()));
                String at = strs[(i * groupLength) + 4] + " " + strs[(i * groupLength) + 5];
                String message = new String(DatatypeConverter.parseHexBinary(strs[(i * groupLength) + 6].trim()));

                sms.setPhoneNumber(from);
                sms.setStatus("UNREAD");
                sms.setMessage(message);
                sms.setDateTime(_convertStringToInstant(at));

                System.out.println("From: " + from);
                System.out.println("At: " + at);
                System.out.println("Message: " + message);

                smsList.add(sms);
            }
            smsRepository.saveAll(smsList);

//        gsmUtils.executeAT("AT+CMGD=,4");

        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
        }finally {
            gsmUtils.closePort();
        }

    }

    // Expected example: 21/03/12 20:02:33+22
    private Instant _convertStringToInstant(String smsDate) {
        final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm:ss");

        String noOffset = smsDate.split("\\+")[0];

        TemporalAccessor temporalAccessor = FORMATTER.parse(noOffset);
        LocalDateTime localDateTime = LocalDateTime.from(temporalAccessor);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
        Instant result = Instant.from(zonedDateTime);

        return result;
    }
}
