package com.mszhan.redwine.manage.server.util;


import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.SequenceMapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Sequence;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
@Component
public class SequenceIdGeneratorImpl implements SequenceIdGenerator {

	@Autowired
	private SequenceMapper sequenceMapper;

	@Override
	public synchronized String getSeqValue(String seqName, Integer length){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
	    Date nowDate = new Date();
	    String str = format.format(nowDate);
        Sequence sequence = sequenceMapper.queryByTypeAndDate(seqName, str);

        String seq = "1";
        if (sequence == null){
            sequence = new Sequence();
            sequence.setType(seqName);
            sequence.setDate(str);
            sequence.setValue(seq);
            sequenceMapper.insert(sequence);
        } else {
                sequence.setValue(String.valueOf(Integer.parseInt(sequence.getValue()) + 1));
                sequenceMapper.updateByPrimaryKey(sequence);
                seq = sequence.getValue() ;
        }
         return String.format("%s%s", str, StringUtils.leftPad(seq, length, "0"));
	}

}
