package com.mszhan.redwine.manage.server.util;

public interface SequenceIdGenerator {

	String getNextSeqId(String seqName);

	String getNextSeqIdLong(String seqName, int length);

	String getNextSeqIdByYear(String seqName);

	String getNextSeqIdByYear(String seqName, int length);

	String getNextSeqIdByMonth(String seqName);

	String getNextSeqIdByMonth(String seqName, int length);

	String getNextSeqIdByDay(String seqName);

	String getNextSeqIdByDay(String seqName, int length);
}
