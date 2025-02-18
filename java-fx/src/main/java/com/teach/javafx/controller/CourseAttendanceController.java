package com.teach.javafx.controller;

import com.teach.javafx.MainApplication;
import com.teach.javafx.controller.base.CourseAttendanceEditController;
import com.teach.javafx.controller.base.LocalDateStringConverter;
import com.teach.javafx.controller.base.MessageDialog;
import com.teach.javafx.models.Student;
import com.teach.javafx.request.HttpRequestUtil;
import com.teach.javafx.request.OptionItem;
import com.teach.javafx.request.SQLiteJDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.util.Objects;

public class CourseAttendanceController {
    @FXML
    private TableView<Map> dataTableView;
    @FXML
    private TableColumn<Map,String> studentNumColumn;
    @FXML
    private TableColumn<Map,String> studentNameColumn;
    @FXML
    private TableColumn<Map,String> courseNameColumn;
    @FXML
    private TableColumn<Map,String> timeColumn;
    @FXML
    private TableColumn<Map,String> flagColumn;
    @FXML
    private TableColumn<Map, Button> editColumn;
    @FXML
    private TableColumn<Map,Button> deleteColumn;
    @FXML
    private ComboBox<OptionItem> inputStudentComboBox;
    @FXML
    private ComboBox<OptionItem> inputCourseComboBox;
    @FXML
    private TextField inputTime;
    @FXML
    private TextField inputFlag;
    @FXML
    private DatePicker timePicker;
    @FXML
    private ComboBox<OptionItem> flagBox;

    private List<OptionItem> flagList;

    private ArrayList<Map> courseAttendanceList=new ArrayList<>();

    private ObservableList<Map> observableList= FXCollections.observableArrayList();
    @FXML
    private ComboBox<OptionItem> studentComboBox;


    private List<OptionItem> studentList;
    @FXML
    private ComboBox<OptionItem> courseComboBox;


    private List<OptionItem> courseList;

    private CourseAttendanceEditController courseAttendanceEditController = null;
    private Stage stage = null;
    private Integer courseAttendanceId=null;
    private String num;
    private String userName;
    private String userType;



    public List<OptionItem> getStudentList() {
        return studentList;
    }
    public List<OptionItem> getCourseList() {
        return courseList;
    }
    @FXML
    public void initialize(){
        DataResponse userResponse = HttpRequestUtil.request("/secure/user/getUser", new DataRequest());
        if(userResponse==null){
            MessageDialog.showDialog("连接失败，请重试");
        }
        Map data= (Map) userResponse.getData();
        userType = (String) data.get("typeId");
        num= (String) data.get("personNum");
        userName=(String) data.get("userName");

        if(userType==null){
            MessageDialog.showDialog("访问失败");
            return;
        }
        if(userType.equals("管理员")) {
            studentNumColumn.setCellValueFactory(new MapValueFactory("studentNum"));  //设置列值工程属性
            studentNameColumn.setCellValueFactory(new MapValueFactory<>("studentName"));
            courseNameColumn.setCellValueFactory(new MapValueFactory<>("courseName"));
            timeColumn.setCellValueFactory(new MapValueFactory<>("time"));
            flagColumn.setCellValueFactory(new MapValueFactory<>("flag"));
            editColumn.setCellValueFactory(new MapValueFactory<>("edit"));
            deleteColumn.setCellValueFactory(new MapValueFactory<>("delete"));
            DataRequest req = new DataRequest();
            studentList = HttpRequestUtil.requestOptionItemList("/api/courseAttendance/getStudentItemOptionList", req); //从后台获取所有学生信息列表集合
            //courseList = HttpRequestUtil.requestOptionItemList("/api/courseAttendance/getCourseItemOptionList",req); //从后台获取所有学生信息列表集合
            courseList = HttpRequestUtil.requestOptionItemList("/api/courseAttendance/getCourseItemOptionList", req);
            OptionItem item = new OptionItem(null, "0", "请选择");
            flagList = HttpRequestUtil.getDictionaryOptionItemList("DKQK");
            flagBox.getItems().addAll(flagList);
            studentComboBox.getItems().addAll(item);
            studentComboBox.getItems().addAll(studentList);
            courseComboBox.getItems().addAll(item);
            courseComboBox.getItems().addAll(courseList);
            inputStudentComboBox.getItems().addAll(item);
            inputStudentComboBox.getItems().addAll(studentList);
            inputCourseComboBox.getItems().addAll(item);
            inputCourseComboBox.getItems().addAll(courseList);
            timePicker.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));
            dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            setTableViewData();
            onQueryButtonClick();
        }else{
            studentNumColumn.setCellValueFactory(new MapValueFactory("studentNum"));  //设置列值工程属性
            studentNameColumn.setCellValueFactory(new MapValueFactory<>("studentName"));
            courseNameColumn.setCellValueFactory(new MapValueFactory<>("courseName"));
            timeColumn.setCellValueFactory(new MapValueFactory<>("time"));
            flagColumn.setCellValueFactory(new MapValueFactory<>("flag"));
            DataRequest req = new DataRequest();
            courseList = HttpRequestUtil.requestOptionItemList("/api/courseAttendance/getCourseItemOptionList", req);
            OptionItem item = new OptionItem(null, "0", "请选择");
            courseComboBox.getItems().addAll(item);
            courseComboBox.getItems().addAll(courseList);
            dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            setTableViewData();
            onQueryButtonClick();

        }
    }
    @FXML
    public void onQueryButtonClick() {
        if(userType.equals("管理员")){
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
            res=HttpRequestUtil.request("/api/courseAttendance/getCourseAttendanceList",req);
            if(res != null && res.getCode()== 0) {
                courseAttendanceList = (ArrayList<Map>)res.getData();
            }
            setTableViewData();
        }else {
            Integer courseId=0;
            DataRequest req=new DataRequest();
            req.add("num",num);
            req.add("name",userName);
            OptionItem op=courseComboBox.getSelectionModel().getSelectedItem();
            if(op!=null){
                courseId=Integer.parseInt(op.getValue());
            }
            req.add("courseId",courseId);
            DataResponse res=HttpRequestUtil.request("/api/courseAttendance/getListByNumName",req);
            if(res != null && res.getCode()== 0) {
                courseAttendanceList = (ArrayList<Map>)res.getData();
                setTableViewData();
            }else if(res!=null){
                MessageDialog.showDialog(res.getMsg());
            }else{
                MessageDialog.showDialog("通信错误！");
            }
            setTableViewData();
        }

        //MessageDialog.showDialog("查找成功！");
    }
    private void setTableViewData(){
        observableList.clear();
        Map map;
        Button editButton;
        Button deleteButton;
        for (int j = 0; j < courseAttendanceList.size(); j++) {
            map = courseAttendanceList.get(j);
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
        Map data = courseAttendanceList.get(j);
        initDialog();
        courseAttendanceEditController.showDialog(data);

        MainApplication.setCanClose(false);
        stage.showAndWait();

    }
    public void deleteItem(String name){
        if(name==null){
            return;
        }
        int j = Integer.parseInt(name.substring(6,name.length()));
        Map data=courseAttendanceList.get(j);
        int jet=MessageDialog.choiceDialog("确定要删除吗");
        if(jet != MessageDialog.CHOICE_YES) {
            return;
        }
        courseAttendanceId=CommonMethod.getInteger(data,"courseAttendanceId");
        //System.out.println(courseAttendanceId);
        DataRequest req=new DataRequest();
        req.add("courseAttendanceId",courseAttendanceId);
        DataResponse res=HttpRequestUtil.request("/api/courseAttendance/deleteCourseAttendance",req);
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
            fxmlLoader = new FXMLLoader(MainApplication.class.getResource("courseAttendance/attendance-edit-dialog.fxml"));
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
            courseAttendanceEditController = (CourseAttendanceEditController) fxmlLoader.getController();
            courseAttendanceEditController.setCourseAttendanceController(this);
            courseAttendanceEditController.init();

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
        if(CommonMethod.getString(data,"time")==null || CommonMethod.getString(data,"flag")==null || Objects.equals(CommonMethod.getString(data, "time"), "") || Objects.equals(CommonMethod.getString(data, "flag"), "")){
            MessageDialog.showDialog("信息不完整，请重新输入！");
            return;
        }
        DataRequest req =new DataRequest();
        req.add("studentId",studentId);
        req.add("courseId",courseId);
        req.add("courseAttendanceId",CommonMethod.getInteger(data,"courseAttendanceId"));
        req.add("time",CommonMethod.getString(data,"time"));
        req.add("flag",CommonMethod.getString(data,"flag"));
        res = HttpRequestUtil.request("/api/courseAttendance/courseAttendanceEditSave",req); //从后台获取所有学生信息列表集合
        if(res != null && res.getCode()== 0) {
            MessageDialog.showDialog("保存成功！");
            onQueryButtonClick();
        }else if(res!=null){
            MessageDialog.showDialog(res.getMsg());
        }else{
            MessageDialog.showDialog("通信异常！");
        }

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
        String newTime=timePicker.getEditor().getText();
        String newFlag=null;
        if(flagBox.getSelectionModel() != null && flagBox.getSelectionModel().getSelectedItem() != null)
            newFlag=flagBox.getSelectionModel().getSelectedItem().getValue();
        if(newFlag==null || newTime==null){
            MessageDialog.showDialog("信息不完整，请重新输入！");
            return;
        }

        DataRequest req =new DataRequest();
        req.add("studentId",studentId);
        req.add("courseId",courseId);
        req.add("newTime",newTime);
        req.add("newFlag",newFlag);
        DataResponse res=HttpRequestUtil.request("/api/courseAttendance/newCourseAttendance",req);
        if(res!=null && res.getCode() == 0) {
            courseAttendanceId = CommonMethod.getIntegerFromObject(res.getData());
            MessageDialog.showDialog("新建记录成功！");
            onQueryButtonClick();
        }else if(res!=null){
            MessageDialog.showDialog(res.getMsg());
        }else{
            MessageDialog.showDialog("通信异常！");
        }

    }
}
