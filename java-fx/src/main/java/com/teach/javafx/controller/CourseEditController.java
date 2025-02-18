package com.teach.javafx.controller;

import com.teach.javafx.controller.base.MessageDialog;
import com.teach.javafx.request.OptionItem;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.fatmansoft.teach.util.CommonMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseEditController {
    @FXML
    private TextField numField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField creditField;

    private CourseController courseController;
    @FXML
    private TextField timeField;
    @FXML
    private TextField examField;
    @FXML
    private TextField positionField;
    @FXML
    private TextField preCourseField;
    @FXML
    private TextField resourceField;
    @FXML
    private ComboBox<OptionItem> typeComboBox;

    private List<OptionItem> typeList;

    private Integer courseId=null;

    public TextField getNumField() {
        return numField;
    }

    public void setNumField(TextField numField) {
        this.numField = numField;
    }

    public TextField getNameField() {
        return nameField;
    }

    public void setNameField(TextField nameField) {
        this.nameField = nameField;
    }

    public TextField getCreditField() {
        return creditField;
    }

    public void setCreditField(TextField creditField) {
        this.creditField = creditField;
    }

    public CourseController getCourseController() {
        return courseController;
    }

    public void setCourseController(CourseController courseController) {
        this.courseController = courseController;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }
    @FXML
    private void initialize(){


    }
    public void init(){
        typeList=initTypeList();
        typeComboBox.getItems().addAll(typeList);

    }
    public List<OptionItem> initTypeList(){
        List<OptionItem> ranks  = new ArrayList<>();
        ranks.add(new OptionItem(0,"","必修课"));
        ranks.add(new OptionItem(1,"","限选课"));
        ranks.add(new OptionItem(2,"","通选课"));
        return ranks;
    }

    public void showDialog(Map data) {
        if(data == null) {
            courseId = null;
            numField.setText("");
            nameField.setText("");
            creditField.setText("");
            timeField.setText("");
            examField.setText("");
            positionField.setText("");
            preCourseField.setText("");
            resourceField.setText("");

            /*timeField.setText("");
            flagField.setText("");*/
        }else {
            courseId = CommonMethod.getInteger(data,"courseId");

            numField.setText(CommonMethod.getString(data,"num"));
            nameField.setText(CommonMethod.getString(data,"name"));
            creditField.setText(CommonMethod.getString(data,"credit"));
            timeField.setText(CommonMethod.getString(data,"time"));
            positionField.setText(CommonMethod.getString(data,"position"));
            preCourseField.setText(CommonMethod.getString(data,"preCourse"));
            examField.setText(CommonMethod.getString(data,"exam"));
            timeField.setText(CommonMethod.getString(data,"time"));
            resourceField.setText(CommonMethod.getString(data,"resource"));
            typeComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByTitle(typeList, CommonMethod.getString(data, "level")));

            typeComboBox.setDisable(false);

            /*timeField.setText(CommonMethod.getString(data, "time"));
            flagField.setText(CommonMethod.getString(data,"flag"));*/
        }
    }
    @FXML
    public void okButtonClick(){
        Map data = new HashMap();
        OptionItem op;
        op = typeComboBox.getSelectionModel().getSelectedItem();
        if(op != null) {
            data.put("type", op.getTitle());
        } else {
            MessageDialog.showDialog("请选中课程类型之后再点击我");
        }
        data.put("courseId",courseId);
        data.put("num",numField.getText());
        data.put("name",nameField.getText());
        data.put("credit",creditField.getText());
        data.put("time",timeField.getText());
        data.put("exam",examField.getText());
        data.put("position",positionField.getText());
        data.put("preCourse",preCourseField.getText());
        data.put("resource",resourceField.getText());
        courseController.doClose("ok",data);

        /*Map data = new HashMap();
        data.put("courseId",courseId);
        data.put("num",numField.getText());
        data.put("name",nameField.getText());
        data.put("credit",creditField.getText());
        courseController.doClose("ok",data);*/

    }
    @FXML
    public void cancelButtonClick(){
        courseController.doClose("cancel",null);
    }
}
