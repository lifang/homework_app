package com.comdosoft.homework.tools;

import java.util.ArrayList;
import java.util.List;

import com.comdosoft.homework.pojo.ListeningPojo;

public class ListeningQuestionMap {
	public static List<ListeningPojo> listeningList = new ArrayList<ListeningPojo>();

	public static void addListeningPojo(ListeningPojo lp) {
		listeningList.add(lp);
	}

	public static ListeningPojo getListeningPojo(int index) {
		return listeningList.get(index);
	}

	public static List<ListeningPojo> getListeningPojoList() {
		return listeningList;
	}

	public static void delListeningPojoList(int index) {
		listeningList.remove(index);
	}
}
