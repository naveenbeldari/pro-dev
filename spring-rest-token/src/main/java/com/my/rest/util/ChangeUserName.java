package com.my.rest.util;

public class ChangeUserName {
		 
		
		private String oldusername;
		private String newusername;
		
		public String getOldusername() {
			return oldusername;
		}

		public void setOldusername(String oldusername) {
			this.oldusername = oldusername;
		}

		public String getNewusername() {
			return newusername;
		}

		public void setNewusername(String newusername) {
			this.newusername = newusername;
		}
	 
		//getter and setter methods
	 
		@Override
		public String toString() {
			return "ChangeUserName [oldusername=" + oldusername + ", newusername=" + newusername + "]";
		}
	}


