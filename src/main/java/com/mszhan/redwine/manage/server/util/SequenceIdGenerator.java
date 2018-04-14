package com.mszhan.redwine.manage.server.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public interface SequenceIdGenerator {

	 String getSeqValue(String seqName, Integer length);
}
