package com.mszhan.redwine.manage.server.util;


import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.SequenceMapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Sequence;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SequenceIdGeneratorImpl implements SequenceIdGenerator {
	
	private SequenceMapper sequenceMapper;
//
//	private BoundHashOperations bho;

	public SequenceIdGeneratorImpl(){
		this.sequenceMapper =  ApplicationContextUtils.getContext().getBean(SequenceMapper.class);

	}

	public String getNextSeqId(String seqName) {
		return getSeqValue(seqName, 7);
	}


	public String getNextSeqIdLong(String seqName, int length) {
		return getSeqValue(seqName, length);
	}


	public String getNextSeqIdByDay(String seqName) {
		String date = UtilDateTime.nowDateString("yyMMdd");
		return date + getSeqValue(seqName, 10);
	}


	public String getNextSeqIdByDay(String seqName, int length) {
		String date = UtilDateTime.nowDateString("yyMMdd");
		return length <= 0 ? date : date + getSeqValue(seqName, length);
	}


	public String getNextSeqIdByMonth(String seqName) {
		String date = UtilDateTime.nowDateString("yyyyMM");
		return date + getSeqValue(seqName, 6);
	}


	public String getNextSeqIdByMonth(String seqName, int length) {
		String date = UtilDateTime.nowDateString("yyMM");
		return length <= 0 ? date : date + getSeqValue(seqName, length);
	}


	public String getNextSeqIdByYear(String seqName) {
		String date = UtilDateTime.nowDateString("yy");
		return date + getSeqValue(seqName, 8);
	}


	public String getNextSeqIdByYear(String seqName, int length) {
		String date = UtilDateTime.nowDateString("yy");
		return length <= 0 ? date : date + getSeqValue(seqName, length);
	}
	private String getSeqValue(String seqName, int length){
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
