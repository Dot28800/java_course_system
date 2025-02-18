package com.teach.javafx.controller;

import com.teach.javafx.MainApplication;
import com.teach.javafx.controller.base.MessageDialog;
import com.teach.javafx.request.HttpRequestUtil;
import com.teach.javafx.request.OptionItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.util.CommonMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CourseChooseTableController {
    @FXML
    private TableView<Map> dataTableView;
    @FXML
    private TableColumn<Map,String> studentNumColumn;
    @FXML
    private TableColumn<Map,String> studentNameColumn;
    @FXML
    private TableColumn<Map,String> courseNameColumn;
    @FXML
    private TableColumn<Map,String> creditColumn;
    @FXML
    private TableColumn<Map,String> teacherColumn;
    @FXML
    private TableColumn<Map, Button> editColumn;
    @FXML
    private TableColumn<Map,Button> deleteColumn;
    @FXML
    private ComboBox<OptionItem> inputStudentComboBox;
    @FXML
    private ComboBox<OptionItem> inputCourseComboBox;


    @FXML
    private TextField inputTeacher;

    private ArrayList<Map> courseChooseList=new ArrayList<>();

    private ObservableList<Map> observableList= FXCollections.observableArrayList();
    @FXML
    private ComboBox<OptionItem> studentComboBox;


    private List<OptionItem> studentList;
    @FXML
    private ComboBox<OptionItem> courseComboBox;


    private List<OptionItem> courseList;

    private CourseChooseEditController courseChooseEditController = null;
    private Stage stage = null;
    private Integer id=null;



    public List<OptionItem> getStudentList() {
        return studentList;
    }
    public List<OptionItem> getCourseList() {
        return courseList;
    }
    @FXML
    public void initialize(){
        studentNumColumn.setCellValueFactory(new MapValueFactory("studentNum"));  //设置列值工程属性
        studentNameColumn.setCellValueFactory(new MapValueFactory<>("studentName"));
        courseNameColumn.setCellValueFactory(new MapValueFactory<>("courseName"));
        creditColumn.setCellValueFactory(new MapValueFactory<>("credit"));
        teacherColumn.setCellValueFactory(new MapValueFactory<>("teacher"));
        /*timeColumn.setCellValueFactory(new MapValueFactory<>("time"));
        flagColumn.setCellValueFactory(new MapValueFactory<>("flag"));*/
        editColumn.setCellValueFactory(new MapValueFactory<>("edit"));
        deleteColumn.setCellValueFactory(new MapValueFactory<>("delete"));
        DataRequest req =new DataRequest();
        studentList = HttpRequestUtil.requestOptionItemList("/api/courseChoose/getStudentItemOptionList",req); //从后台获取所有学生信息列表集合
        //courseList = HttpRequestUtil.requestOptionItemList("/api/courseAttendance/getCourseItemOptionList",req); //从后台获取所有学生信息列表集合
        courseList = HttpRequestUtil.requestOptionItemList("/api/courseChoose/getCourseItemOptionList",req);
        OptionItem item = new OptionItem(null,"0","请选择");
        studentComboBox.getItems().addAll(item);
        studentComboBox.getItems().addAll(studentList);
        courseComboBox.getItems().addAll(item);
        courseComboBox.getItems().addAll(courseList);
        inputStudentComboBox.getItems().addAll(item);
        inputStudentComboBox.getItems().addAll(studentList);
        inputCourseComboBox.getItems().addAll(item);
        inputCourseComboBox.getItems().addAll(courseList);
        dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        onQueryButtonClick();
    }
    @FXML
    public void onQueryButtonClick() {
        Integer studentId = 0;
        Integer courseId = 0;
        OptionItem op;
        op = studentComboBox.getSelectionModel().getSelectedItem();
        if(op != null)
            studentId = Integer.parseInt(op.getValue());
        op = courseComboBox.getSelectionModel().getSelectedItem();
        if(op != null)
            courseId = Integer.parseInt(op.getValue());
        DataResponse res;
        DataRequest req =new DataRequest();
        req.add("studentId",studentId);
        req.add("courseId",courseId);
        res=HttpRequestUtil.request("/api/courseChoose/getCourseChooseList",req);
        if(res != null && res.getCode()== 0) {
            courseChooseList = (ArrayList<Map>)res.getData();
        }
        setTableViewData();
        //MessageDialog.showDialog("查找成功！");
    }
    private void setTableViewData(){
        observableList.clear();
        Map map;
        Button editButton;
        Button deleteButton;
        for (int j = 0; j < courseChooseList.size(); j++) {
            map = courseChooseList.get(j);
            editButton = new Button("修改");
            editButton.setId("edit"+j);
            deleteButton=new Button("删除");
            deleteButton.setId("delete"+j);
            editButton.setOnAction(e->{
                editItem(((Button)e.getSource()).getId());
            });
            deleteButton.setOnAction(e->{
                deleteItem(((Button)e.getSource()).getId());
            });
            map.put("edit",editButton);
            map.put("delete",deleteButton);
            observableList.addAll(FXCollections.observableArrayList(map));
        }
        dataTableView.setItems(observableList);

    }
    public void editItem(String name){
        if(name == null)
            return;
        int j = Integer.parseInt(name.substring(4,name.length()));
        Map data = courseChooseList.get(j);
        initDialog();
        courseChooseEditController.showDialog(data);

        MainApplication.setCanClose(false);
        stage.showAndWait();

    }
    public void deleteItem(String name){
        if(name==null){
            return;
        }
        int j = Integer.parseInt(name.substring(6,name.length()));
        Map data=courseChooseList.get(j);
        int jet= MessageDialog.choiceDialog("确定要删除吗");
        if(jet != MessageDialog.CHOICE_YES) {
            return;
        }
        id= CommonMethod.getInteger(data,"id");
        //System.out.println(courseAttendanceId);
        DataRequest req=new DataRequest();
        req.add("id",id);
        DataResponse res=HttpRequestUtil.request("/api/courseChoose/deleteCourseChoose",req);
        if(res.getCode()==0){
            MessageDialog.showDialog("删除记录成功！");
            onQueryButtonClick();
        }else{
            MessageDialog.showDialog(res.getMsg());
        }

    }
    private void initDialog() {
        if(stage!= null)
            return;
        FXMLLoader fxmlLoader ;
        Scene scene = null;
        try {
            fxmlLoader = new FXMLLoader(MainApplication.class.getResource("courseChoose/choose-edit-dialog.fxml"));
            scene = new Scene(fxmlLoader.load(), 800, 400);
            stage = new Stage();
            stage.initOwner(MainApplication.getMainStage());
            stage.initModality(Modality.NONE);
            stage.setAlwaysOnTop(true);
            stage.setScene(scene);
            stage.setTitle("考勤信息修改对话框");
            stage.setOnCloseRequest(event ->{
                MainApplication.setCanClose(true);
            });
            courseChooseEditController = (CourseChooseEditController) fxmlLoader.getController();
            courseChooseEditController.setCourseChooseTableController(this);
            courseChooseEditController.init();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void doClose(String cmd, Map data) {
        MainApplication.setCanClose(true);
        stage.close();
        if(!"ok".equals(cmd))
            return;
        DataResponse res;
        Integer studentId = CommonMethod.getInteger(data,"studentId");
        if(studentId == null) {
            MessageDialog.showDialog("没有选中学生不能添加保存！");
            return;
        }
        Integer courseId = CommonMethod.getInteger(data,"courseId");
        if(courseId == null) {
            MessageDialog.showDialog("没有选中课程不能添加保存！");
            return;
        }
        /*if(CommonMethod.getString(data,"time")==null || CommonMethod.getString(data,"flag")==null){
            MessageDialog.showDialog("信息不完整，请重新输入！");
            return;
        }*/
        DataRequest req =new DataRequest();
        req.add("studentId",studentId);
        req.add("courseId",courseId);
        req.add("id",CommonMethod.getInteger(data,"id"));
        req.add("teacher",CommonMethod.getString(data,"teacher"));
        /*req.add("time",CommonMethod.getString(data,"time"));
        req.add("flag",CommonMethod.getString(data,"flag"));*/
        res = HttpRequestUtil.request("/api/courseChoose/courseChooseEditSave",req); //从后台获取所有学生信息列表集合
        if(res != null && res.getCode()== 0) {
            onQueryButtonClick();
        }
        MessageDialog.showDialog("保存成功！");
    }
    @FXML
    public void onNewButtonClick(){
        /*List<OptionItem> newstudentList = HttpRequestUtil.requestOptionItemList("/api/courseAttendance/getStudentItemOptionList",new DataRequest()); //从后台获取所有学生信息列表集合
        //courseList = HttpRequestUtil.requestOptionItemList("/api/courseAttendance/getCourseItemOptionList",req); //从后台获取所有学生信息列表集合
        List<OptionItem> newcourseList = HttpRequestUtil.requestOptionItemList("/api/courseAttendance/getCourseItemOptionList",new DataRequest());
        OptionItem item = new OptionItem(null,"0","请选择");
        inputStudentComboBox.getItems().addAll(item);
        inputStudentComboBox.getItems().addAll(newstudentList);
        inputCourseComboBox.getItems().addAll(item);
        inputCourseComboBox.getItems().addAll(newcourseList);
        dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);*/
        Integer studentId = 0;
        Integer courseId = 0;
        OptionItem op;
        op = inputStudentComboBox.getSelectionModel().getSelectedItem();
        if(op != null)
            studentId = Integer.parseInt(op.getValue());
        op = inputCourseComboBox.getSelectionModel().getSelectedItem();
        if(op != null)
            courseId = Integer.parseInt(op.getValue());
        /*String newTime=inputTime.getText();
        String newFlag=inputFlag.getText();
        if(newFlag==null || newTime==null){
            MessageDialog.showDialog("信息不完整，请重新输入！");
            return;
        }*/

        DataRequest req =new DataRequest();
        req.add("studentId",studentId);
        req.add("courseId",courseId);
        req.add("teacher",inputTeacher.getText());
        /*req.add("newTime",newTime);
        req.add("newFlag",newFlag);*/
        DataResponse res=HttpRequestUtil.request("/api/courseChoose/newCourseChoose",req);
        if(res.getCode() == 0) {
            id = CommonMethod.getIntegerFromObject(res.getData());
            MessageDialog.showDialog("新建记录成功！");
            onQueryButtonClick();
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }

    }
}
