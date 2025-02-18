package com.teach.javafx.controller;

import com.teach.javafx.controller.base.MessageDialog;
import com.teach.javafx.request.OptionItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.fatmansoft.teach.util.CommonMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentEditController {
    @FXML
    private TextField numField; //学生信息  学号输入域
    @FXML
    private TextField nameField;  //学生信息  名称输入域
    @FXML
    private TextField deptField; //学生信息  院系输入域
    @FXML
    private TextField majorField; //学生信息  专业输入域
    @FXML
    private TextField classNameField; //学生信息  班级输入域
    @FXML
    private TextField cardField; //学生信息  证件号码输入域
    @FXML
    private ComboBox<OptionItem> genderComboBox;  //学生信息  性别输入域
    @FXML
    private DatePicker birthdayPick;  //学生信息  出生日期选择域
    @FXML
    private TextField birthdayField;
    @FXML
    private TextField emailField;  //学生信息  邮箱输入域
    @FXML
    private TextField phoneField;   //学生信息  电话输入域
    @FXML
    private TextField addressField;  //学生信息  地址输入域

    @FXML
    private TextField ageField;  //查询 姓名学号输入域

    private StudentController studentController;

    private Integer studentId = null;  //当前编辑修改的学生的主键

    private ArrayList<Map> studentList = new ArrayList();  // 学生信息列表数据
    private List<OptionItem> genderList;   //性别选择列表数据
    private ObservableList<Map> observableList= FXCollections.observableArrayList();

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

    public TextField getDeptField() {
        return deptField;
    }

    public void setDeptField(TextField deptField) {
        this.deptField = deptField;
    }

    public TextField getMajorField() {
        return majorField;
    }

    public void setMajorField(TextField majorField) {
        this.majorField = majorField;
    }

    public TextField getClassNameField() {
        return classNameField;
    }

    public void setClassNameField(TextField classNameField) {
        this.classNameField = classNameField;
    }

    public TextField getCardField() {
        return cardField;
    }

    public void setCardField(TextField cardField) {
        this.cardField = cardField;
    }

    public ComboBox<OptionItem> getGenderComboBox() {
        return genderComboBox;
    }

    public void setGenderComboBox(ComboBox<OptionItem> genderComboBox) {
        this.genderComboBox = genderComboBox;
    }

    public DatePicker getBirthdayPick() {
        return birthdayPick;
    }

    public void setBirthdayPick(DatePicker birthdayPick) {
        this.birthdayPick = birthdayPick;
    }

    public TextField getBirthdayField() {
        return birthdayField;
    }

    public void setBirthdayField(TextField birthdayField) {
        this.birthdayField = birthdayField;
    }

    public TextField getEmailField() {
        return emailField;
    }

    public void setEmailField(TextField emailField) {
        this.emailField = emailField;
    }

    public TextField getPhoneField() {
        return phoneField;
    }

    public void setPhoneField(TextField phoneField) {
        this.phoneField = phoneField;
    }

    public TextField getAddressField() {
        return addressField;
    }

    public void setAddressField(TextField addressField) {
        this.addressField = addressField;
    }

    public TextField getNumNameTextField() {
        return ageField;
    }

    public void setNumNameTextField(TextField numNameTextField) {
        this.ageField = numNameTextField;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public ArrayList<Map> getStudentList() {
        return studentList;
    }

    public void setStudentList(ArrayList<Map> studentList) {
        this.studentList = studentList;
    }

    public List<OptionItem> getGenderList() {
        return genderList;
    }

    public void setGenderList(List<OptionItem> genderList) {
        this.genderList = genderList;
    }

    public ObservableList<Map> getObservableList() {
        return observableList;
    }

    public void setObservableList(ObservableList<Map> observableList) {
        this.observableList = observableList;
    }

    public TextField getAgeField() {
        return ageField;
    }

    public void setAgeField(TextField ageField) {
        this.ageField = ageField;
    }

    public StudentController getStudentController() {
        return studentController;
    }

    public void setStudentController(StudentController studentController) {
        this.studentController = studentController;
    }

    @FXML
    public void initialize(){

    }

    public void init(){

        genderList=initGenders();
        genderComboBox.getItems().addAll(genderList);
    }

    private List<OptionItem> initGenders() {
        List<OptionItem> genders=new ArrayList<>();
        genders.add(new OptionItem(1,"男","男"));
        genders.add(new OptionItem(2,"女","女"));
        return genders;
    }
    @FXML
    public void showDialog(Map data) {

        studentId = null;
        genderComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByTitle(genderList, CommonMethod.getString(data, "gender")));
        genderComboBox.setDisable(false);
        ageField.setText(CommonMethod.getString(data,"age"));
        numField.setText(CommonMethod.getString(data,"num"));
        numField.setEditable(false);
        nameField.setText(CommonMethod.getString(data,"name"));
        classNameField.setText(CommonMethod.getString(data,"className"));
        majorField.setText(CommonMethod.getString(data,"major"));
        deptField.setText(CommonMethod.getString(data,"dept"));
        cardField.setText(CommonMethod.getString(data,"card"));
        birthdayField.setText(CommonMethod.getString(data,"birthday"));
        emailField.setText(CommonMethod.getString(data,"email"));
        phoneField.setText(CommonMethod.getString(data,"phone"));
        addressField.setText(CommonMethod.getString(data,"address"));
    }
    @FXML
    public void okButtonClick(){

        Map data = new HashMap();
        OptionItem op;
        if(genderComboBox.getSelectionModel() != null && genderComboBox.getSelectionModel().getSelectedItem() != null)
            data.put("gender",genderComboBox.getSelectionModel().getSelectedItem().getValue());
        /*if(op != null) {
            data.put("gender", op.getValue());
        }*/else{
            MessageDialog.showDialog("没有选择性别，请选择之后再保存");
            return;
        }
        String num= numField.getText();
        String name= nameField.getText();
        String className=classNameField.getText();
        String major= majorField.getText();
        String dept=deptField.getText();
        String card=cardField.getText();
        String birthday=birthdayField.getText();
        String email=emailField.getText();
        String phone=phoneField.getText();
        String address=addressField.getText();
        String age=ageField.getText();
        boolean flag=(num.isEmpty()) || name.isEmpty() ||className.isEmpty() ||major.isEmpty()||dept.isEmpty()||card.isEmpty()||birthday.isEmpty()||email.isEmpty()||phone.isEmpty()||address.isEmpty()||age.isEmpty();
        if(flag){
            MessageDialog.showDialog("信息未填写完整");
            return;
        }
        boolean flagJudge=judgeNum(phone,11)&&judgeNum(card,11)&&judgeNum(age,2)&&judgeNum(num,10);
        if(!flagJudge){
            MessageDialog.showDialog("信息格式不符合要求，请再次检查后提交");
            return;
        }
        data.put("num",num);
        data.put("name",name);
        data.put("className",className);
        data.put("major",major);
        data.put("dept",dept);
        data.put("card",card);
        data.put("birthday",birthday);
        data.put("email",email);
        data.put("phone",phone);
        data.put("address",address);
        data.put("age",age);
        studentController.doEditClose("ok",data);

    }

    boolean judgeNum(String card,int l){
        if(card.length()!=l){
            return false;
        }
        for(int i=0;i<card.length();i++){
            char ch=card.charAt(i);
            return ch>='0' && ch<='9';
        }
        return true;

    }
    @FXML
    public void cancelButtonClick(){
        studentController.doNewClose("cancel",null);

    }
}
