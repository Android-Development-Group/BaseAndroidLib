package com.mobisoft.mbswebplugin.view.PopuMenu.Entity;

import java.util.List;

/**
 * Created by zz on 2016/8/18.
 */
public class TaskListEntity {

	private String code;
	private String msg;
	private List<LeftMenuEntity> data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<LeftMenuEntity> getData() {
		return data;
	}

	public void setData(List<LeftMenuEntity> data) {
		this.data = data;
	}

	// for left listView, java bean must extends BaseMenuBean
	public static class LeftMenuEntity extends BaseMenuBean {
		private int id;

		private int version;

		private long create_datetime;

		private String company;

		private String code;

		private String name;
		

		private List<TaskListDataEntity> taskList;
	    private boolean isShowImg;
	    


	
		
		
		public LeftMenuEntity() {
			super();
		}



		public LeftMenuEntity(int id, int version, long create_datetime, String company, String code, String name,
				List<TaskListDataEntity> taskList) {
			super();
			this.id = id;
			this.version = version;
			this.create_datetime = create_datetime;
			this.company = company;
			this.code = code;
			this.name = name;
			this.taskList = taskList;
		}



		public boolean isShowImg() {
			return (isShowImg!=true?false:true);	}

		public void setShowImg(boolean isShowImg) {
			this.isShowImg = isShowImg;
		}
		public int getId() {
			return id;
		}



		public void setId(int id) {
			this.id = id;
		}



		public int getVersion() {
			return version;
		}



		public void setVersion(int version) {
			this.version = version;
		}



		public long getCreate_datetime() {
			return create_datetime;
		}



		public void setCreate_datetime(long create_datetime) {
			this.create_datetime = create_datetime;
		}



		public String getCompany() {
			return company;
		}



		public void setCompany(String company) {
			this.company = company;
		}



		public String getCode() {
			return code;
		}



		public void setCode(String code) {
			this.code = code;
		}



		public String getName() {
			return name;
		}



		public void setName(String name) {
			this.name = name;
		}



		public List<TaskListDataEntity> getTaskList() {
			return taskList;
		}

		public void setTaskList(List<TaskListDataEntity> taskList) {
			this.taskList = taskList;
		}

		public static class TaskListDataEntity {

			private int id;

			private int version;

			private long create_datetime;

			private String company;

			private String code;

			private String name;
			private boolean isSelected;
			
			

			public TaskListDataEntity() {
				super();
			}

			

			public TaskListDataEntity(int id, int version, long create_datetime, String company, String code,
					String name, boolean isSelected) {
				super();
				this.id = id;
				this.version = version;
				this.create_datetime = create_datetime;
				this.company = company;
				this.code = code;
				this.name = name;
				this.isSelected = isSelected;
			}



			public int getId() {
				return id;
			}

			public void setId(int id) {
				this.id = id;
			}

			public int getVersion() {
				return version;
			}

			public void setVersion(int version) {
				this.version = version;
			}

			public long getCreate_datetime() {
				return create_datetime;
			}

			public void setCreate_datetime(long create_datetime) {
				this.create_datetime = create_datetime;
			}

			public String getCompany() {
				return company;
			}

			public void setCompany(String company) {
				this.company = company;
			}

			public String getCode() {
				return code;
			}

			public void setCode(String code) {
				this.code = code;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public boolean getIsSelect() {
				return isSelected!=true?false:true;
			}

			public void setIsSelect(boolean isSelect) {
				this.isSelected = isSelect;
			}
		}
	}
}
