package com.example.crysto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ContestService {

	private static List<Contest> contestList = new ArrayList<Contest>();
	private int size = 10;
	static {
		contestList.add(new Contest(1, "Contest 1"));
		contestList.add(new Contest(2, "Contest 2"));
		contestList.add(new Contest(3, "Contest 3"));
		contestList.add(new Contest(4, "Contest 4"));
		contestList.add(new Contest(5, "Contest 5"));
		contestList.add(new Contest(6, "Contest 6"));
		contestList.add(new Contest(7, "Contest 7"));
		contestList.add(new Contest(8, "Contest 8"));
		contestList.add(new Contest(9, "Contest 9"));
		contestList.add(new Contest(10, "Contest 10"));
	}

	public List<Contest> getAllContests() {
		return contestList;
	}

	public void removeContest() {
		contestList.remove(0);
	}

	public void addContest() {
		contestList.add(new Contest(++size, "Contest " + size));
	}
}
