package com.teach.javafx.controller;


import com.teach.javafx.controller.base.LocalDateStringConverter;
import com.teach.javafx.request.HttpRequestUtil;
import com.teach.javafx.request.OptionItem;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.util.CommonMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeworkEditController {
    @FXML
    private ComboBox<OptionItem> studentComboBox;
    private List<OptionItem> studentList;
    @FXML
    private ComboBox<OptionItem> courseComboBox;
    private List<OptionItem> courseList;

    @FXML
    private TextField timeField;

    @FXML
    private TextField markField;

    @FXML
    private DatePicker timePick;

    private HomeworkTableController homeworkTableController;

    private Integer homeworkId=null;

    public ComboBox<OptionItem> getStudentComboBox() {
        return studentComboBox;
    }

    public void setStudentComboBox(ComboBox<OptionItem> studentComboBox) {
        this.studentComboBox = studentComboBox;
    }

    public List<OptionItem> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<OptionItem> studentList) {
        this.studentList = studentList;
    }

    public ComboBox<OptionItem> getCourseComboBox() {
        return courseComboBox;
    }

    public void setCourseComboBox(ComboBox<OptionItem> courseComboBox) {
        this.courseComboBox = courseComboBox;
    }

    public List<OptionItem> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<OptionItem> courseList) {
        this.courseList = courseList;
    }

    public TextField getTimeField() {
        return timeField;
    }

    public void setTimeField(TextField timeField) {
        this.timeField = timeField;
    }

    public TextField getMarkField() {
        return markField;
    }

    public void setMarkField(TextField markField) {
        this.markField = markField;
    }

    public HomeworkTableController getHomeworkTableController() {
        return homeworkTableController;
    }

    public void setHomeworkTableController(HomeworkTableController homeworkTableController) {
        this.homeworkTableController = homeworkTableController;
    }

    public Integer getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(Integer homeworkId) {
        this.homeworkId = homeworkId;
    }

    @FXML
    private void initialize(){


    }

    @FXML
    public void showDialog(Map data) {
        if(data == null) {
            homeworkId = null;
            studentComboBox.getSelectionModel().select(-1);
            courseComboBox.getSelectionModel().select(-1);
            studentComboBox.setDisable(false);
            courseComboBox.setDisable(false);
            //timeField.setText("");
            timePick.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));
            markField.setText("");
        }else {
            homeworkId = CommonMethod.getInteger(data,"workId");
            studentComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(studentList, CommonMethod.getString(data, "studentId")));
            courseComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(courseList, CommonMethod.getString(data, "courseId")));
            studentComboBox.setDisable(true);
            courseComboBox.setDisable(true);
           timePick.getEditor().setText(CommonMethod.getString(data, "time"));
            //timePick.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));
            //timeField.setText(CommonMethod.getString(data,"time"));
            markField.setText(CommonMethod.getString(data, "mark"));

        }
    }

    @FXML
    public void okButtonClick(){
        Map data = new HashMap();
        OptionItem op;
        op = studentComboBox.getSelectionModel().getSelectedItem();
        /*if(timeField.getText()==null || flagField.getText()==null ){
            MessageDialog.showDialog("信息不完整，请重新输入");
            return;
        }*/
        if(op != null) {
            data.put("studentId",Integer.parseInt(op.getValue()));
        }
        op = courseComboBox.getSelectionModel().getSelectedItem();
        if(op != null) {
            data.put("courseId", Integer.parseInt(op.getValue()));
        }
        data.put("workId",homeworkId);
        data.put("time",timePick.getEditor().getText());
        data.put("mark",markField.getText());
        /*data.put("time",timeField.getText());
        data.put("flag",flagField.getText());*/
        homeworkTableController.doClose("ok",data);

    }
    @FXML
    public void cancelButtonClick(){
        homeworkTableController.doClose("cancel",null);

    }

    public void init(){
        studentList=homeworkTableController.getStudentList();
        courseList=homeworkTableController.getCourseList();
        studentComboBox.getItems().addAll(studentList);
        courseComboBox.getItems().addAll(courseList);
        //teacherField.setText(CommonMethod.getString(data,""));
    }
}
