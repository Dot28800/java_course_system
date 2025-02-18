package com.teach.javafx.controller;

import com.teach.javafx.request.OptionItem;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.fatmansoft.teach.util.CommonMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreEditController {
    @FXML
    private ComboBox<OptionItem> studentComboBox;
    private List<OptionItem> studentList;
    @FXML
    private ComboBox<OptionItem> provinceComboBox;
    private List<OptionItem> provinceList;
    @FXML
    private TextField sourcePlaceField;
    @FXML
    private TextField preSchoolField;
    @FXML
    private TextField preScoreField;
    @FXML
    private TextField preRankField;
    @FXML
    private TextField preHonorField;

    private PreInformationController preInformationController;
    private Integer preInformationId=null;

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

    public ComboBox<OptionItem> getProvinceComboBox() {
        return provinceComboBox;
    }

    public void setProvinceComboBox(ComboBox<OptionItem> provinceComboBox) {
        this.provinceComboBox = provinceComboBox;
    }

    public List<OptionItem> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(List<OptionItem> provinceList) {
        this.provinceList = provinceList;
    }

    public TextField getSourcePlaceField() {
        return sourcePlaceField;
    }

    public void setSourcePlaceField(TextField sourcePlaceField) {
        this.sourcePlaceField = sourcePlaceField;
    }

    public TextField getPreSchoolField() {
        return preSchoolField;
    }

    public void setPreSchoolField(TextField preSchoolField) {
        this.preSchoolField = preSchoolField;
    }

    public TextField getPreScoreField() {
        return preScoreField;
    }

    public void setPreScoreField(TextField preScoreField) {
        this.preScoreField = preScoreField;
    }

    public TextField getPreRankField() {
        return preRankField;
    }

    public void setPreRankField(TextField preRankField) {
        this.preRankField = preRankField;
    }

    public TextField getPreHonorField() {
        return preHonorField;
    }

    public void setPreHonorField(TextField preHonorField) {
        this.preHonorField = preHonorField;
    }

    public PreInformationController getPreInformationController() {
        return preInformationController;
    }

    public void setPreInformationController(PreInformationController preInformationController) {
        this.preInformationController = preInformationController;
    }

    public Integer getPreInformationId() {
        return preInformationId;
    }

    public void setPreInformationId(Integer preInformationId) {
        this.preInformationId = preInformationId;
    }
    @FXML
    public void initialize(){

    }

    public void init(){
        studentList=preInformationController.getStudentList();
        //courseList=courseChooseTableController.getCourseList();
        studentComboBox.getItems().addAll(studentList);
        provinceList=initProvinces();
        provinceComboBox.getItems().addAll(provinceList);
    }
    public List<OptionItem> initProvinces(){
        List<OptionItem> provinces=new ArrayList<>();
        provinces.add(new OptionItem(1, "", "北京市"));
        provinces.add(new OptionItem(2, "", "天津市"));
        provinces.add(new OptionItem(3, "", "河北省"));
        provinces.add(new OptionItem(4, "", "山西省"));
        provinces.add(new OptionItem(5, "", "内蒙古自治区"));
        provinces.add(new OptionItem(6, "", "辽宁省"));
        provinces.add(new OptionItem(7, "", "吉林省"));
        provinces.add(new OptionItem(8, "", "黑龙江省"));
        provinces.add(new OptionItem(9, "", "上海市"));
        provinces.add(new OptionItem(10, "", "江苏省"));
        provinces.add(new OptionItem(11, "", "浙江省"));
        provinces.add(new OptionItem(12, "", "安徽省"));
        provinces.add(new OptionItem(13, "", "福建省"));
        provinces.add(new OptionItem(14, "", "江西省"));
        provinces.add(new OptionItem(15, "", "山东省"));
        provinces.add(new OptionItem(16, "", "河南省"));
        provinces.add(new OptionItem(17, "", "湖北省"));
        provinces.add(new OptionItem(18, "", "湖南省"));
        provinces.add(new OptionItem(19, "", "广东省"));
        provinces.add(new OptionItem(20, "", "广西壮族自治区"));
        provinces.add(new OptionItem(21, "", "海南省"));
        provinces.add(new OptionItem(22, "", "重庆市"));
        provinces.add(new OptionItem(23, "", "四川省"));
        provinces.add(new OptionItem(24, "", "贵州省"));
        provinces.add(new OptionItem(25, "", "云南省"));
        provinces.add(new OptionItem(26, "", "西藏自治区"));
        provinces.add(new OptionItem(27, "", "陕西省"));
        provinces.add(new OptionItem(28, "", "甘肃省"));
        provinces.add(new OptionItem(29, "", "青海省"));
        provinces.add(new OptionItem(30, "", "宁夏回族自治区"));
        provinces.add(new OptionItem(31, "", "新疆维吾尔自治区"));
        provinces.add(new OptionItem(32, "", "台湾省")); // 注：台湾省是省级行政区，但实际情况中可能需要特殊处理
        provinces.add(new OptionItem(33, "", "香港特别行政区"));
        provinces.add(new OptionItem(34,"","澳门特别行政区"));
        return provinces;
    }

    @FXML
    public void showDialog(Map data) {
        if(data == null) {
            preInformationId = null;
            studentComboBox.getSelectionModel().select(-1);
            provinceComboBox.getSelectionModel().select(-1);
            studentComboBox.setDisable(false);
            provinceComboBox.setDisable(false);
            /*timeField.setText("");
            flagField.setText("");*/
        }else {
            preInformationId = CommonMethod.getInteger(data,"preInformationId");
            Integer studentId=CommonMethod.getInteger(data,"studentId");
            String sourcePlace=CommonMethod.getString(data,"province");
            studentComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(studentList, CommonMethod.getInteger(data, "studentId").toString()));
           provinceComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByTitle(provinceList, CommonMethod.getString(data, "province")));
            studentComboBox.setDisable(false);
            provinceComboBox.setDisable(false);
            preSchoolField.setText(CommonMethod.getString(data,"preSchool"));
            preScoreField.setText(CommonMethod.getString(data, "preScore"));
            preRankField.setText(CommonMethod.getString(data,"preRank"));
            preHonorField.setText(CommonMethod.getString(data,"preHonor"));
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
        op = provinceComboBox.getSelectionModel().getSelectedItem();
        if(op != null) {
            data.put("province", op.getTitle());
        }
        data.put("preInformationId",preInformationId);
        data.put("preSchool",preSchoolField.getText());

        data.put("preScore",preScoreField.getText());
        data.put("preRank",preRankField.getText());
        data.put("preHonor",preHonorField.getText());
       preInformationController.doEditClose("ok",data);

    }
    @FXML
    public void cancelButtonClick(){
        preInformationController.doEditClose("cancel",null);

    }
}
