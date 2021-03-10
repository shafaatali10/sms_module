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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void scanIncomingSms(){
        Optional<Config> configVal = configRepository.findByConfigName("SERIAL_PORT");


        if( 1==1 ){
            System.out.println("BOOOOOOOOM ----> " + configVal.get().getConfigValue());
            return;
        }

        gsmUtils.initialize(configVal.get().getConfigValue());
        gsmUtils.executeAT("AT+CSCS=\"IRA\""); // To set normal English mode

        List<Sms> smsList = new ArrayList<>();

        int j = 0;
        while (true){
            if(j==2){
                break;
            }

            String result = gsmUtils.executeAT("AT+CMGL=\"ALL\"");
            System.out.println("---->\n"+result+"\n<-----");


            String[] strs = result.replace("\"", "").split("(?:,)|(?:\r\n)");


//            System.out.println(Arrays.toString(strs));
            Sms sms;
            try{
                for (int i = 1; i < strs.length - 1; i++) {
                    //String str1 = strs[i];
                    sms = new Sms();
                    System.out.println(":::::::::::::::::" + strs[i]);
//                    sms.setId(Integer.parseInt(strs[i].charAt(strs[i].length() - 1) + "")); // TODO: REC READ is throwing error
                    i++;
                    sms.setStatus(strs[i]);
                    i++;
                    sms.setPhoneNumber(strs[i]);
                    i++;
                    sms.setPhoneName(strs[i]);
                    i++;
                    String xx = strs[i];
//                    sms.setDate(strs[i]);
                    i++;
                    xx = strs[i];
//                    sms.setTime(strs[i]);
                    i++;
                /*if (Longs.tryParse(strs[i].substring(0, 2)) != null) { //get the message UNICODE
                    Iterable<String> arr = Splitter.fixedLength(4).split(strs[i]);
                    String con = "";
                    for (String s : arr) {
                        int hexVal = Integer.parseInt(s, 16);
                        con += (char) hexVal;
                    }
                    sms.setContent(con);
                } else {//get the message String
                    sms.setContent(strs[i]);
                }*/
                    sms.setMessage(strs[i]);
                    if (!strs[i + 1].equals("") && !strs[i + 1].startsWith("+")) {
                        i++;
                        sms.setMessage(sms.getMessage() + "\n" + strs[i]);
                        i++;
                    }
                    //str.add(sms);

                    System.out.println(sms.toString());
                    smsList.add(sms);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            smsRepository.saveAll(smsList);

            gsmUtils.executeAT("AT+CMGD=,4");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            j++;
        }

        gsmUtils.closePort();

    }
}
