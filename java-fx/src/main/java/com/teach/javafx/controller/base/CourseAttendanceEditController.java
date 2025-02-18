package com.teach.javafx.controller.base;

import com.teach.javafx.controller.CourseAttendanceController;
import com.teach.javafx.request.HttpRequestUtil;
import com.teach.javafx.request.OptionItem;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.fatmansoft.teach.util.CommonMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.XMLFormatter;

public class CourseAttendanceEditController {
    @FXML
    private ComboBox<OptionItem> studentComboBox;
    private List<OptionItem> studentList;
    @FXML
    private ComboBox<OptionItem> courseComboBox;
    private List<OptionItem> courseList;
    @FXML
    private TextField timeField;
    @FXML
    private TextField flagField;
    @FXML
    private DatePicker timePicker;
    @FXML
    private ChoiceBox<OptionItem> flagBox;
    private List<OptionItem> flagList;

    private CourseAttendanceController courseAttendanceController;

    private Integer courseAttendanceId=null;

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

    public TextField getFlagField() {
        return flagField;
    }

    public void setFlagField(TextField flagField) {
        this.flagField = flagField;
    }

    public CourseAttendanceController getCourseAttendanceController() {
        return courseAttendanceController;
    }

    public void setCourseAttendanceController(CourseAttendanceController courseAttendanceController) {
        this.courseAttendanceController = courseAttendanceController;
    }

    public Integer getCourseAttendanceId() {
        return courseAttendanceId;
    }

    public void setCourseAttendanceId(Integer courseAttendanceId) {
        this.courseAttendanceId = courseAttendanceId;
    }

    @FXML
    private void initialize(){
        flagList = HttpRequestUtil.getDictionaryOptionItemList("DKQK");

        flagBox.getItems().addAll(flagList);
        timePicker.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));

    }
    @FXML
    public void showDialog(Map data) {
        if(data == null) {
            courseAttendanceId = null;
            studentComboBox.getSelectionModel().select(-1);
            courseComboBox.getSelectionModel().select(-1);
            studentComboBox.setDisable(false);
            courseComboBox.setDisable(false);
            /*timeField.setText("");
            flagField.setText("");*/
            timePicker.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));
            flagList = HttpRequestUtil.getDictionaryOptionItemList("DKQK");

            flagBox.getItems().addAll(flagList);
        }else {
            courseAttendanceId = CommonMethod.getInteger(data,"courseAttendanceId");
            studentComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(studentList, CommonMethod.getString(data, "studentId")));
            courseComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(courseList, CommonMethod.getString(data, "courseId")));
            studentComboBox.setDisable(true);
            courseComboBox.setDisable(true);
            timePicker.getEditor().setText(CommonMethod.getString(data, "time"));
            String flag=CommonMethod.getString(data,"flag");
            if(flag.equals("1")){
                flag="是";
            } else if (flag.equals("2")) {
                flag="否";
            }
            flagBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(flagList, flag));

            /*timeField.setText(CommonMethod.getString(data, "time"));
            flagField.setText(CommonMethod.getString(data,"flag"));*/
        }
    }
    @FXML
    public void okButtonClick(){
        Map data = new HashMap();
        OptionItem op;
        op = studentComboBox.getSelectionModel().getSelectedItem();
        String newTime=timePicker.getEditor().getText();
        String newFlag=null;
        if(flagBox.getSelectionModel() != null && flagBox.getSelectionModel().getSelectedItem() != null)
            newFlag=flagBox.getSelectionModel().getSelectedItem().getValue();
        if(newTime==null || newFlag==null ){
            MessageDialog.showDialog("信息不完整，请重新输入");
            return;
        }
        if(op != null) {
            data.put("studentId",Integer.parseInt(op.getValue()));
        }
        op = courseComboBox.getSelectionModel().getSelectedItem();
        if(op != null) {
            data.put("courseId", Integer.parseInt(op.getValue()));
        }
        data.put("courseAttendanceId",courseAttendanceId);
        data.put("time",newTime);
        data.put("flag",newFlag);
        courseAttendanceController.doClose("ok",data);

    }
    @FXML
    public void cancelButtonClick(){
        courseAttendanceController.doClose("cancel",null);

    }

    public void init(){
        studentList=courseAttendanceController.getStudentList();
        courseList=courseAttendanceController.getCourseList();
        studentComboBox.getItems().addAll(studentList);
        courseComboBox.getItems().addAll(courseList);
    }

}
