package com.rezzcomm.reservation.controller.parser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;

import com.rezzcomm.reservation.controller.CSVReader;
import com.rezzcomm.reservation.model.ReasonForVisitCSV;
import com.rezzcomm.reservation.model.SpecialtyCSV;

public class CSVListParser {

	private ArrayList<SpecialtyCSV> specialty_arr;
	private ArrayList<ReasonForVisitCSV> reason_for_visit_arr;
	private ArrayList<String> specialty_name;

	public CSVListParser (Context context) {
		// TODO Auto-generated method stub
		CSVReader readerSpecialty, readerReasonOfVisit;
		String nextSp[] = {};
		String nextRov[] = {};
		
		specialty_arr = new ArrayList<SpecialtyCSV>();
		specialty_name = new ArrayList<String>();
		reason_for_visit_arr = new ArrayList<ReasonForVisitCSV>();

		try {
			readerSpecialty = new CSVReader(new InputStreamReader(context.getAssets().open("csv/specialty.csv")));
			readerReasonOfVisit = new CSVReader(new InputStreamReader(context.getAssets().open("csv/reasonofvisit.csv")));

			for(;;) {
				nextSp = readerSpecialty.readNext();
				if(nextSp != null) {
					SpecialtyCSV sp = new SpecialtyCSV();
					sp.id = nextSp[0].toString();
					sp.name = nextSp[1].toString();
					sp.short_name = nextSp[2].toString();
					specialty_arr.add(sp);
					specialty_name.add(nextSp[1].toString());
				} else {
					break;
				}
			}
			
			for(;;) {
				nextRov = readerReasonOfVisit.readNext();
				if(nextRov != null) {
					ReasonForVisitCSV rov = new ReasonForVisitCSV();
					rov.id = nextRov[0].toString();
					rov.name = nextRov[1].toString();
					rov.short_name = nextRov[2].toString();
					rov.parent = nextRov[3].toString();
					reason_for_visit_arr.add(rov);
				} else {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<SpecialtyCSV> getSpecialty_arr() {
		return specialty_arr;
	}

	public ArrayList<ReasonForVisitCSV> getReason_for_visit_arr() {
		return reason_for_visit_arr;
	}

	public ArrayList<String> getSpecialty_name() {
		return specialty_name;
	}
}
