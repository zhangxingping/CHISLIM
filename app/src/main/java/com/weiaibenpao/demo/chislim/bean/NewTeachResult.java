package com.weiaibenpao.demo.chislim.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2016/11/1.
 */

public class NewTeachResult implements Serializable{


    /**
     * error : 0
     * newTeachBean : [{"teach_id":1,"teachName":"燃脂机","teach_tab1":1,"teach_tab2":2,"teach_grade":1,"teach_userhad":0,"teach_image":"http://img.easthardware.com/upload/z/z2/zjqms/picture/2016/07/23/983a4af0-c070-4269-8bdc-cf52822b31b6.jpg","teach_text":"这是对应的描述，这是对应的描述，这是对应的描述，"},{"teach_id":2,"teachName":"抽油机","teach_tab1":2,"teach_tab2":2,"teach_grade":1,"teach_userhad":0,"teach_image":"http://img.easthardware.com/upload/z/z2/zjqms/picture/2016/07/23/983a4af0-c070-4269-8bdc-cf52822b31b6.jpg","teach_text":"这是对应的描述，这是对应的描述，这是对应的描述，"},{"teach_id":3,"teachName":"抽油机","teach_tab1":2,"teach_tab2":2,"teach_grade":1,"teach_userhad":0,"teach_image":"http://img.easthardware.com/upload/z/z2/zjqms/picture/2016/07/23/983a4af0-c070-4269-8bdc-cf52822b31b6.jpg","teach_text":"这是对应的描述，这是对应的描述，"},{"teach_id":4,"teachName":"抽油机","teach_tab1":2,"teach_tab2":2,"teach_grade":1,"teach_userhad":0,"teach_image":"http://img.easthardware.com/upload/z/z2/zjqms/picture/2016/07/23/983a4af0-c070-4269-8bdc-cf52822b31b6.jpg","teach_text":"这是对应的描述，这是对应的描述，这是对应的描述，这是对应的描述，"},{"teach_id":5,"teachName":"抽油机","teach_tab1":2,"teach_tab2":2,"teach_grade":1,"teach_userhad":0,"teach_image":"http://img.easthardware.com/upload/z/z2/zjqms/picture/2016/07/23/983a4af0-c070-4269-8bdc-cf52822b31b6.jpg","teach_text":"这是对应的描述，这是对应的描述，这是对应的描述，这是对应的描述，"}]
     */

    private int error;
    /**
     * teach_id : 1
     * teachName : 燃脂机
     * teach_tab1 : 1
     * teach_tab2 : 2
     * teach_grade : 1
     * teach_userhad : 0
     * teach_image : http://img.easthardware.com/upload/z/z2/zjqms/picture/2016/07/23/983a4af0-c070-4269-8bdc-cf52822b31b6.jpg
     * teach_text : 这是对应的描述，这是对应的描述，这是对应的描述，
     */

    private List<NewTeachBeanBean> newTeachBean;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public List<NewTeachBeanBean> getNewTeachBean() {
        return newTeachBean;
    }

    public void setNewTeachBean(List<NewTeachBeanBean> newTeachBean) {
        this.newTeachBean = newTeachBean;
    }

    public static class NewTeachBeanBean implements Serializable{
        private int teach_id;
        private String teachName;
        private int teach_tab1;
        private int teach_tab2;
        private int teach_grade;
        private int teach_userhad;
        private String teach_image;
        private String teach_text;

        public int getTeach_id() {
            return teach_id;
        }

        public void setTeach_id(int teach_id) {
            this.teach_id = teach_id;
        }

        public String getTeachName() {
            return teachName;
        }

        public void setTeachName(String teachName) {
            this.teachName = teachName;
        }

        public int getTeach_tab1() {
            return teach_tab1;
        }

        public void setTeach_tab1(int teach_tab1) {
            this.teach_tab1 = teach_tab1;
        }

        public int getTeach_tab2() {
            return teach_tab2;
        }

        public void setTeach_tab2(int teach_tab2) {
            this.teach_tab2 = teach_tab2;
        }

        public int getTeach_grade() {
            return teach_grade;
        }

        public void setTeach_grade(int teach_grade) {
            this.teach_grade = teach_grade;
        }

        public int getTeach_userhad() {
            return teach_userhad;
        }

        public void setTeach_userhad(int teach_userhad) {
            this.teach_userhad = teach_userhad;
        }

        public String getTeach_image() {
            return teach_image;
        }

        public void setTeach_image(String teach_image) {
            this.teach_image = teach_image;
        }

        public String getTeach_text() {
            return teach_text;
        }

        public void setTeach_text(String teach_text) {
            this.teach_text = teach_text;
        }
    }
}
