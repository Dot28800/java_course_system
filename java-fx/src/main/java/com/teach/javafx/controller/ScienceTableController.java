package com.teach.javafx.controller;
import com.teach.javafx.MainApplication;
import com.teach.javafx.controller.base.MessageDialog;
import com.teach.javafx.request.HttpRequestUtil;
import com.teach.javafx.request.OptionItem;
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


public class ScienceTableController {
    public TextField studentNumField;
    public TextField studentNameField;
    public TextField projectNameField;
    public TextField achievementField;
    public TextField teacherNameField;
    public TextField inputStudentNumNameField;
    @FXML
    private TableView<Map> dataTableView;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> studentNumColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> studentNameColumn;
    @FXML
    private TableColumn<Map,String> projectNameColumn;
    @FXML
    private TableColumn<Map,String> achievementColumn;
    @FXML
    private TableColumn<Map,String> teacherNameColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map, Button> editColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map, Button> deleteColumn;
    @FXML
    private TextField inputProjectName;
    @FXML
    private TextField inputAchievement;
    @FXML
    private TextField inputTeacherName;
    @FXML
    private ComboBox<OptionItem> inputStudentComboBox;
    @FXML
    private ComboBox<OptionItem> studentComboBox=new ComboBox<>();
    @FXML
    private DatePicker timePicker;
    private List<OptionItem> studentList;
    private ArrayList<Map> scienceList=new ArrayList<>();

    private ObservableList<Map> observableList= FXCollections.observableArrayList();


    private Stage stage = null;
    private Integer scienceId=null;
    private ScienceEditController scienceEditController=null;


    public List<OptionItem> getStudentList() {
        return studentList;
    }

    private String num;
    private String userName;
    private String userType;
    @FXML
    private Label studentNameLabel;

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
            projectNameColumn.setCellValueFactory(new MapValueFactory<>("projectName"));
            achievementColumn.setCellValueFactory(new MapValueFactory<>("achievement"));
            teacherNameColumn.setCellValueFactory(new MapValueFactory<>("teacherName"));
            editColumn.setCellValueFactory(new MapValueFactory<>("edit"));
            deleteColumn.setCellValueFactory(new MapValueFactory<>("delete"));
            DataRequest req1 = new DataRequest();
            studentList = HttpRequestUtil.requestOptionItemList("/api/science/getStudentItemOptionList", req1); //从后台获取所有学生信息列表集合
            OptionItem item = new OptionItem(null, "0", "请选择");
            dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            setTableViewData();
            onQueryButtonClick();
        }else{
            studentNameLabel.setText(userName);
            studentNumColumn.setCellValueFactory(new MapValueFactory("studentNum"));  //设置列值工程属性
            studentNameColumn.setCellValueFactory(new MapValueFactory<>("studentName"));
            projectNameColumn.setCellValueFactory(new MapValueFactory<>("projectName"));
            achievementColumn.setCellValueFactory(new MapValueFactory<>("achievement"));
            teacherNameColumn.setCellValueFactory(new MapValueFactory<>("teacherName"));
            /*editColumn.setCellValueFactory(new MapValueFactory<>("edit"));
            deleteColumn.setCellValueFactory(new MapValueFactory<>("delete"));*/
            dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            setTableViewData();
            onQueryButtonClick();
        }
    }
    @FXML
    void onQueryButtonClick(){
        /*Integer studentId = 0;
        OptionItem op;
        op = studentComboBox.getSelectionModel().getSelectedItem();
        if(op != null)
            studentId = Integer.parseInt(op.getValue());*/
        if(userType.equals("管理员")){
            DataResponse res;
            DataRequest req =new DataRequest();
            String numName=inputStudentNumNameField.getText();
            String teacherName=inputTeacherName.getText();
            req.add("numName",numName);
            req.add("teacherName",teacherName);
            res=HttpRequestUtil.request("/api/science/getScienceList",req);
            if(res != null && res.getCode()== 0) {
                scienceList = (ArrayList<Map>)res.getData();
                setTableViewData();
            }else if(res!=null){
                MessageDialog.showDialog(res.getMsg());
            }else{
                MessageDialog.showDialog("通信错误！");
            }
        }else{
            DataResponse res;
            DataRequest req =new DataRequest();

            String teacherName=inputTeacherName.getText();
            req.add("num",num);
            req.add("name",userName);
            req.add("teacherName",teacherName);
            res=HttpRequestUtil.request("/api/science/getList",req);
            if(res != null && res.getCode()== 0) {
                scienceList = (ArrayList<Map>)res.getData();
                setTableViewData();
            }else if(res!=null){
                MessageDialog.showDialog(res.getMsg());
            }else{
                MessageDialog.showDialog("通信错误！");
            }
        }

    }

    private void setTableViewData() {
        observableList.clear();
        Map map;
        Button editButton;
        Button deleteButton;
        for (int j = 0; j < scienceList.size(); j++) {
            map = scienceList.get(j);
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
    private void editItem(String name) {
        if(name == null)
            return;
        int j = Integer.parseInt(name.substring(4,name.length()));
        Map data = scienceList.get(j);

        if(stage == null){
            FXMLLoader fxmlLoader ;
            Scene scene = null;
            try {
                fxmlLoader = new FXMLLoader(MainApplication.class.getResource("science/science-dialog.fxml"));
                scene = new Scene(fxmlLoader.load(), 600, 400);
                stage = new Stage();
                stage.initOwner(MainApplication.getMainStage());
                stage.initModality(Modality.NONE);
                stage.setAlwaysOnTop(true);
                stage.setScene(scene);
                stage.setTitle("科研经历修改对话框");
                stage.setOnCloseRequest(event ->{
                    MainApplication.setCanClose(true);
                });
                scienceEditController = fxmlLoader.getController();
                scienceEditController.setScienceTableController(this);
                scienceEditController.init();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        scienceEditController.showDialog(data);
        MainApplication.setCanClose(false);
        stage.showAndWait();
    }

    private void deleteItem(String name) {

        if(name==null){
            return;
        }
        int j = Integer.parseInt(name.substring(6,name.length()));
        Map data=scienceList.get(j);
        int jet=MessageDialog.choiceDialog("确定要删除吗");
        if(jet != MessageDialog.CHOICE_YES) {
            return;
        }
        scienceId= CommonMethod.getInteger(data,"scienceId");
        DataRequest req=new DataRequest();
        req.add("scienceId",scienceId);
        DataResponse res=HttpRequestUtil.request("/api/science/deleteScience",req);
        if(res.getCode()==0){
            MessageDialog.showDialog("删除记录成功！");
            onQueryButtonClick();
        }else{
            MessageDialog.showDialog(res.getMsg());
        }

    }

    @FXML
    void onNewButtonClick(){
        Integer studentId = 0;
        String newRank=null;
        /*OptionItem op;
        op = inputStudentComboBox.getSelectionModel().getSelectedItem();
        if(op != null)
            studentId=Integer.parseInt(op.getValue());
        else{
            MessageDialog.showDialog("请选择学生后再点击此框");
            return;
        }*/
        String num=studentNumField.getText();
        String name=studentNameField.getText();
        String newProjectName=projectNameField.getText();
        String newAchievement=achievementField.getText();
        String newTeacherName=teacherNameField.getText();
        boolean flag= num.isEmpty() || name.isEmpty() || newProjectName.isEmpty() || newAchievement.isEmpty() || newTeacherName.isEmpty() ;
        if(flag){
            MessageDialog.showDialog("信息不完整，请填写完整后点击此框");
            return;
        }

        DataRequest req =new DataRequest();
        req.add("num",num);
        req.add("name",name);
        req.add("projectName",newProjectName);
        req.add("achievement",newAchievement);
        req.add("teacherName",newTeacherName);

        DataResponse res=HttpRequestUtil.request("/api/science/newScience",req);
        if(res!=null && res.getCode() == 0){
            MessageDialog.showDialog("新建记录成功！");
            onQueryButtonClick();
        }else if(res!=null){
            MessageDialog.showDialog(res.getMsg());
        }else{
            MessageDialog.showDialog("通信异常！");
        }
    }

    public void doEditClose(String cmd, Map data) {
        MainApplication.setCanClose(true);
        stage.close();
        if(!"ok".equals(cmd))
            return;
        DataResponse res;
        DataRequest req =new DataRequest();
        req.add("scienceId",CommonMethod.getInteger(data,"scienceId"));;
        req.add("studentId",CommonMethod.getInteger(data,"studentId"));
        req.add("projectName",CommonMethod.getString(data,"projectName"));
        req.add("achievement",CommonMethod.getString(data,"achievement"));
        req.add("teacherName",CommonMethod.getString(data,"teacherName"));
        res = HttpRequestUtil.request("/api/science/scienceEditSave",req); //从后台获取所有学生信息列表集合
        if(res != null && res.getCode()== 0) {
            onQueryButtonClick();
            MessageDialog.showDialog("保存成功！");
        }else if(res!=null){
            MessageDialog.showDialog(res.getMsg());
        }else {
            MessageDialog.showDialog("通信异常！");
        }
    }


}
