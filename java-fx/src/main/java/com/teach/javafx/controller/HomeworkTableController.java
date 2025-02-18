package com.teach.javafx.controller;

import com.teach.javafx.MainApplication;
import com.teach.javafx.controller.HomeworkEditController;
import com.teach.javafx.controller.base.LocalDateStringConverter;
import com.teach.javafx.controller.base.MessageDialog;
import com.teach.javafx.models.Student;
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
import java.util.Objects;

public class HomeworkTableController {
    @FXML
    private TableView<Map> dataTableView;
    @FXML
    private TableColumn<Map,String> studentNumColumn;
    @FXML
    private TableColumn<Map,String> studentNameColumn;
    @FXML
    private TableColumn<Map,Integer> courseNumColumn;
    @FXML
    private TableColumn<Map,String> courseNameColumn;
    @FXML
    private TableColumn<Map, String> timeColumn;
    @FXML
    private TableColumn<Map,String> markColumn;
    @FXML
    private TableColumn<Map, Button> editColumn;
    @FXML
    private TableColumn<Map,Button> deleteColumn;
    @FXML
    private ComboBox<OptionItem> studentComboBox;
    @FXML
    private ComboBox<OptionItem> courseComboBox;
    @FXML
    private ComboBox<OptionItem> inputStudentComboBox;
    @FXML
    private ComboBox<OptionItem> inputCourseComboBox;
    @FXML
    private DatePicker timePicker;

    private List<OptionItem> studentList;
    @FXML
    private Label studentName;

    @FXML
    private ComboBox<OptionItem> inputStudent;
    @FXML
    private ComboBox<OptionItem> inputCourse;
    @FXML
    private TextField inputTime;
    @FXML
    private TextField inputMark;


    private List<OptionItem> courseList;

    private HomeworkEditController homeworkEditController=null;

    private List<Map> workList = new ArrayList();  // 学生信息列表数据
    private ObservableList<Map> observableList= FXCollections.observableArrayList();  // TableView渲染列表

    private Stage stage = null;
    private Integer homeworkId=null;
    private Integer studentId;

    public void setStudentList(List<OptionItem> studentList) {
        this.studentList = studentList;
    }

    public void setCourseList(List<OptionItem> courseList) {
        this.courseList = courseList;
    }
    private String num;
    private String userName;
    String userType;

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
        if(userType.equals("管理员")){
            studentNumColumn.setCellValueFactory(new MapValueFactory("studentNum"));  //设置列值工程属性
            studentNameColumn.setCellValueFactory(new MapValueFactory<>("studentName"));
            courseNameColumn.setCellValueFactory(new MapValueFactory<>("courseName"));
            courseNumColumn.setCellValueFactory(new MapValueFactory<>("courseNum"));
            timeColumn.setCellValueFactory(new MapValueFactory<>("time"));
            markColumn.setCellValueFactory(new MapValueFactory<>("mark"));
            editColumn.setCellValueFactory(new MapValueFactory<>("edit"));
            deleteColumn.setCellValueFactory(new MapValueFactory<>("delete"));

            DataRequest req =new DataRequest();
            studentList = HttpRequestUtil.requestOptionItemList("/api/homework/getStudentItemOptionList",req); //从后台获取所有学生信息列表集合
            //courseList = HttpRequestUtil.requestOptionItemList("/api/courseAttendance/getCourseItemOptionList",req); //从后台获取所有学生信息列表集合
            courseList = HttpRequestUtil.requestOptionItemList("/api/homework/getCourseItemOptionList",req);
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
            timePicker.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));
            onQueryButtonClick();

        }else{
            studentNumColumn.setCellValueFactory(new MapValueFactory("studentNum"));  //设置列值工程属性
            studentNameColumn.setCellValueFactory(new MapValueFactory<>("studentName"));
            courseNameColumn.setCellValueFactory(new MapValueFactory<>("courseName"));
            courseNumColumn.setCellValueFactory(new MapValueFactory<>("courseNum"));
            timeColumn.setCellValueFactory(new MapValueFactory<>("time"));
            markColumn.setCellValueFactory(new MapValueFactory<>("mark"));
            studentName.setText(userName);
            DataRequest req =new DataRequest();
            //studentList = HttpRequestUtil.requestOptionItemList("/api/homework/getStudentItemOptionList",req); //从后台获取所有学生信息列表集合*/
            courseList = HttpRequestUtil.requestOptionItemList("/api/courseAttendance/getCourseItemOptionList",req); //从后台获取所有学生信息列表集合
            courseList = HttpRequestUtil.requestOptionItemList("/api/homework/getCourseItemOptionList",req);
            OptionItem item = new OptionItem(null,"0","请选择");
            courseComboBox.getItems().addAll(item);
            courseComboBox.getItems().addAll(courseList);
            dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            onQueryButtonClick();

        }


    }
    private void setTableViewData(){
        observableList.clear();
        Map map;
        Button editButton;
        Button deleteButton;
        for (int j = 0; j < workList.size(); j++) {
            map = workList.get(j);
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

    private void deleteItem(String name) {
        if(name==null){
            return;
        }
        int j = Integer.parseInt(name.substring(6,name.length()));
        Map data=workList.get(j);
        int jet= MessageDialog.choiceDialog("确定要删除吗");
        if(jet != MessageDialog.CHOICE_YES) {
            return;
        }
        homeworkId= CommonMethod.getInteger(data,"workId");
        //System.out.println(courseAttendanceId);
        DataRequest req=new DataRequest();
        req.add("workId",homeworkId);
        DataResponse res=HttpRequestUtil.request("/api/homework/deleteHomework",req);
        if(res.getCode()==0){
            MessageDialog.showDialog("删除记录成功！");
            onQueryButtonClick();
        }else{
            MessageDialog.showDialog(res.getMsg());
        }
    }

    private void editItem(String name) {
        if(name == null)
            return;
        int j = Integer.parseInt(name.substring(4,name.length()));
        Map data = workList.get(j);
        initDialog();
        homeworkEditController.showDialog(data);

        MainApplication.setCanClose(false);
        stage.showAndWait();
    }
    private void initDialog() {
        if(stage!= null)
            return;
        FXMLLoader fxmlLoader ;
        Scene scene = null;
        try {
            fxmlLoader = new FXMLLoader(MainApplication.class.getResource("homeWork/work-dialog.fxml"));
            scene = new Scene(fxmlLoader.load(), 800, 400);
            stage = new Stage();
            stage.initOwner(MainApplication.getMainStage());
            stage.initModality(Modality.NONE);
            stage.setAlwaysOnTop(true);
            stage.setScene(scene);
            stage.setTitle("作业信息修改对话框");
            stage.setOnCloseRequest(event ->{
                MainApplication.setCanClose(true);
            });
            homeworkEditController = (HomeworkEditController) fxmlLoader.getController();
            homeworkEditController.setHomeworkTableController(this);
            homeworkEditController.init();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onQueryButtonClick(){
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
            res=HttpRequestUtil.request("/api/homework/getWorkList",req);
            if(res != null && res.getCode()== 0) {
                workList = (ArrayList<Map>)res.getData();
                //MessageDialog.showDialog("查找成功！");
            }else if(res!=null){
                MessageDialog.showDialog(res.getMsg());
            }else{
                MessageDialog.showDialog("连接错误，未正常返回请求！");
            }
            setTableViewData();
        }else{
            Integer courseId=0;
            DataRequest req=new DataRequest();
            OptionItem op;
            op = courseComboBox.getSelectionModel().getSelectedItem();
            if(op != null)
                courseId = Integer.parseInt(op.getValue());
            req.add("num",num);
            req.add("name",userName);
            req.add("courseId",courseId);
            DataResponse res=HttpRequestUtil.request("/api/homework/getWorkListByNumName",req);
            if(res != null && res.getCode()== 0) {
                workList = (ArrayList<Map>)res.getData();
                //MessageDialog.showDialog("查找成功！");
            }else if(res!=null){
                MessageDialog.showDialog(res.getMsg());
            }else{
                MessageDialog.showDialog("连接错误，未正常返回请求！");
            }
            setTableViewData();
        }


    }
    @FXML
    public void onEditButtonClick(){

    }
    @FXML
    public void onNewButtonClick(){
        Integer studentId = 0;
        Integer courseId = 0;
        OptionItem op;
        op = inputStudentComboBox.getSelectionModel().getSelectedItem();
        if(op != null)
            studentId = Integer.parseInt(op.getValue());
        op = inputCourseComboBox.getSelectionModel().getSelectedItem();
        if(op != null)
            courseId = Integer.parseInt(op.getValue());
        if(inputMark.getText()==null|| Objects.equals(timePicker.getEditor().getText(), "") || Objects.equals(inputMark.getText(), "")){
            MessageDialog.showDialog("信息不完整，请填写完整后再点击我");
            return;
        }
        try{
            Double d=Double.parseDouble(inputMark.getText());
        }catch (Exception e){
            MessageDialog.showDialog("输入的分数格式不对，请修改之后再提交");
            return;
        }
        /*String newTime=inputTime.getText();
        String newFlag=inputFlag.getText();
        if(newFlag==null || newTime==null){
            MessageDialog.showDialog("信息不完整，请重新输入！");
            return;
        }*/

        DataRequest req =new DataRequest();
        req.add("studentId",studentId);
        req.add("courseId",courseId);
        req.add("time",timePicker.getEditor().getText());
        req.add("mark",inputMark.getText());
        /*req.add("newTime",newTime);
        req.add("newFlag",newFlag);*/
        DataResponse res=HttpRequestUtil.request("/api/homework/newHomework",req);
        if(res!=null && res.getCode()!=null&&res.getCode() == 0) {
            homeworkId = CommonMethod.getIntegerFromObject(res.getData());
            MessageDialog.showDialog("新建记录成功！");
            onQueryButtonClick();
        } else if (res!=null&&res.getCode()!=null) {
            MessageDialog.showDialog(res.getMsg());

        } else {
            MessageDialog.showDialog("通信异常");
        }

    }
    @FXML
    public void onDeleteButtonClick(){

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
        String time=CommonMethod.getString(data,"time");
        String mark=CommonMethod.getString(data,"mark");
        if(Objects.equals(time, "") || Objects.equals(mark, "")){
            MessageDialog.showDialog("信息未填写完整！");
            return;
        }
        try{
            Double d=Double.parseDouble(mark);
        }catch (Exception e){
            MessageDialog.showDialog("输入分数的格式不对，请修改之后再提交！");
            return;
        }
        req.add("studentId",studentId);
        req.add("courseId",courseId);
        req.add("workId",CommonMethod.getInteger(data,"workId"));

        req.add("time",CommonMethod.getString(data,"time"));
        req.add("mark",CommonMethod.getString(data,"mark"));
        res = HttpRequestUtil.request("/api/homework/homeworkEditSave",req); //从后台获取所有学生信息列表集合
        if(res != null && res.getCode()== 0) {
            onQueryButtonClick();
            MessageDialog.showDialog("保存成功！");
        } else if (res!=null ) {
            MessageDialog.showDialog(res.getMsg());

        }else{
            MessageDialog.showDialog("通讯错误，未获得反馈");
        }

    }


}
