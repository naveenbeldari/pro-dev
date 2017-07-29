package com.hospitalmgt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HospitalMgt {

	public static void main(String[] args) throws IOException {
		String path = "C:\\Users\\LENOVO\\Desktop\\input.txt";
		BufferedReader bufferedReader=null;
			try {
				FileReader fileReader = new FileReader(path);
				 bufferedReader = new BufferedReader(fileReader);
				String ele;
				PatientVO patientVO = new PatientVO();
				while ((ele=bufferedReader.readLine()) != null){
					String[] inputary = ele.split(";");
						patientVO.setName(inputary[0]);
						patientVO.setMrn(inputary[1]);
						patientVO.setType(inputary[2]);
						patientVO.setAdmissonDate(inputary[3]);
						patientVO.setDischargeDate(inputary[4]);
						System.out.println(patientVO);
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); 	
				Date adminDt = dateFormat.parse(patientVO.getAdmissonDate());
				Date disDt = dateFormat.parse(patientVO.getDischargeDate());
				if(adminDt.after(disDt)){
					System.out.println("false");
				}
				else{
					System.out.println("true");
				}
				if(patientVO.getName().matches("[a-zA-Z0-9\\s]{0,19}")){
					System.out.println("ok");
				}
				else{
					System.out.println("no");
				}
				int diffDays = getTimeDiff(adminDt, disDt);
				System.out.println("diffDays " +diffDays);
			  }
			} catch (Exception e) {
				e.printStackTrace();
			}
		finally {
			bufferedReader.close();
		}
	}
	public static int getTimeDiff(Date dateOne, Date dateTwo) {  
		long timeDiff = Math.abs(dateOne.getTime() - dateTwo.getTime());
		int diffInDays = (int) ((timeDiff) / (1000 * 60 * 60 * 24));
		return diffInDays;
		}
	public static class PatientVO{
			String name;
			String mrn;
			String type;
			String admissonDate;
			String dischargeDate;
			
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			public String getMrn() {
				return mrn;
			}
			public void setMrn(String mrn) {
				this.mrn = mrn;
			}
			public String getType() {
				return type;
			}
			public void setType(String type) {
				this.type = type;
			}
			public String getAdmissonDate() {
				return admissonDate;
			};
			public void setAdmissonDate(String admissonDate) {
				this.admissonDate = admissonDate;
			}
			public String getDischargeDate() {
				return dischargeDate;
			}
			public void setDischargeDate(String dischargeDate) {
				this.dischargeDate = dischargeDate;
			}
			@Override
			public String toString() {
				return "PatientVO: [name=" + name + ", mrn=" + mrn
	                + ", type=" + type + ", admissonDate=" + admissonDate + ", dischargeDate="
	                + dischargeDate + "]";
			}
		}
}
