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
import java.util.logging.XMLFormatter;


public class PracticeTableController {
    @FXML
    private TableView<Map> dataTableView;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> studentNumColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> studentNameColumn;
    @FXML
    private TableColumn<Map,String> placeColumn;
    @FXML
    private TableColumn<Map,String> timeColumn;
    @FXML
    private TableColumn<Map,String> evaluationColumn;
    @FXML
    private TableColumn<Map,String> teamNameColumn;
    @FXML
    private TableColumn<Map,String> typeColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map, Button> editColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map, Button> deleteColumn;
    @FXML
    private TextField inputPlace;

    @FXML
    private TextField inputEvaluation;
    @FXML
    private TextField inputTeamName;
    @FXML
    private TextField inputTime;
    @FXML
    private TextField inputType;

    @FXML
    private ComboBox<OptionItem> inputStudentComboBox;
    @FXML
    private ComboBox<OptionItem> studentComboBox=new ComboBox<>();
    @FXML
    private DatePicker timePicker;
    private List<OptionItem> studentList;
    private ArrayList<Map> practiceList=new ArrayList<>();

    private ObservableList<Map> observableList= FXCollections.observableArrayList();
    private PracticeEditController practiceEditController = null;

    private Stage stage = null;
    private Integer practiceId=null;


    public List<OptionItem> getStudentList() {
        return studentList;
    }
    private String num;
    private String userName;
    String userType;
    @FXML
   public Label studentNameLabel;

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
            teamNameColumn.setCellValueFactory(new MapValueFactory<>("teamName"));
            timeColumn.setCellValueFactory(new MapValueFactory<>("time"));
            placeColumn.setCellValueFactory(new MapValueFactory<>("place"));
            evaluationColumn.setCellValueFactory(new MapValueFactory<>("evaluation"));
            editColumn.setCellValueFactory(new MapValueFactory<>("edit"));
            deleteColumn.setCellValueFactory(new MapValueFactory<>("delete"));
            typeColumn.setCellValueFactory(new MapValueFactory<>("type"));
            DataRequest req1 = new DataRequest();
            studentList = HttpRequestUtil.requestOptionItemList("/api/practice/getStudentItemOptionList", req1); //从后台获取所有学生信息列表集合

            OptionItem item = new OptionItem(null, "0", "请选择");

            studentComboBox.getItems().addAll(item);
            studentComboBox.getItems().addAll(studentList);

            inputStudentComboBox.getItems().addAll(item);
            inputStudentComboBox.getItems().addAll(studentList);
            dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            setTableViewData();
            onQueryButtonClick();
        }else {
            studentNumColumn.setCellValueFactory(new MapValueFactory("studentNum"));  //设置列值工程属性
            studentNameColumn.setCellValueFactory(new MapValueFactory<>("studentName"));
            teamNameColumn.setCellValueFactory(new MapValueFactory<>("teamName"));
            timeColumn.setCellValueFactory(new MapValueFactory<>("time"));
            placeColumn.setCellValueFactory(new MapValueFactory<>("place"));
            evaluationColumn.setCellValueFactory(new MapValueFactory<>("evaluation"));
            typeColumn.setCellValueFactory(new MapValueFactory<>("type"));
            studentNameLabel.setText(userName);
            dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            setTableViewData();
            onQueryButtonClick();
        }
    }
    @FXML
    void onQueryButtonClick(){
        if(userType.equals("管理员")){
            Integer studentId = 0;
            OptionItem op;
            op = studentComboBox.getSelectionModel().getSelectedItem();
            if(op != null)
                studentId = Integer.parseInt(op.getValue());

            DataResponse res;
            DataRequest req =new DataRequest();
            req.add("studentId",studentId);

            res=HttpRequestUtil.request("/api/practice/getPracticeList",req);
            if(res != null && res.getCode()== 0) {
                practiceList = (ArrayList<Map>)res.getData();
                setTableViewData();
            }else if(res!=null){
                MessageDialog.showDialog(res.getMsg());
            }else{
                MessageDialog.showDialog("通信错误！");
            }
        }else{
            DataRequest req=new DataRequest();
            req.add("num",num);
            req.add("name",userName);
            DataResponse res=HttpRequestUtil.request("/api/practice/getListByNumName",req);
            if(res != null && res.getCode()== 0) {
                practiceList = (ArrayList<Map>)res.getData();
                //MessageDialog.showDialog("查找成功！");
            }else if(res!=null){
                MessageDialog.showDialog(res.getMsg());
            }else{
                MessageDialog.showDialog("连接错误，未正常返回请求！");
            }
            setTableViewData();
        }

    }

    private void setTableViewData() {
        observableList.clear();
        Map map;
        Button editButton;
        Button deleteButton;
        for (int j = 0; j < practiceList.size(); j++) {
            map = practiceList.get(j);
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
        Map data = practiceList.get(j);

        if(stage == null){
            FXMLLoader fxmlLoader ;
            Scene scene = null;
            try {
                fxmlLoader = new FXMLLoader(MainApplication.class.getResource("practice/practice-edit-dialog.fxml"));
                scene = new Scene(fxmlLoader.load(), 1200, 800);
                stage = new Stage();
                stage.initOwner(MainApplication.getMainStage());
                stage.initModality(Modality.NONE);
                stage.setAlwaysOnTop(true);
                stage.setScene(scene);
                stage.setTitle("实践经历修改对话框");
                stage.setOnCloseRequest(event ->{
                    MainApplication.setCanClose(true);
                });
                practiceEditController = fxmlLoader.getController();
                practiceEditController.setPracticeTableController(this);
                practiceEditController.init();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        practiceEditController.showDialog(data);
        MainApplication.setCanClose(false);
        stage.showAndWait();
    }

    private void deleteItem(String name) {

        if(name==null){
            return;
        }
        int j = Integer.parseInt(name.substring(6,name.length()));
        Map data=practiceList.get(j);
        int jet=MessageDialog.choiceDialog("确定要删除吗");
        if(jet != MessageDialog.CHOICE_YES) {
            return;
        }
        practiceId= CommonMethod.getInteger(data,"practiceId");
        DataRequest req=new DataRequest();
        req.add("practiceId",practiceId);
        DataResponse res=HttpRequestUtil.request("/api/practice/deletePractice",req);
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
        OptionItem op;
        op = inputStudentComboBox.getSelectionModel().getSelectedItem();
        if(op != null)
            studentId=Integer.parseInt(op.getValue());
        else{
            MessageDialog.showDialog("请选择学生后再点击此框");
            return;
        }
        String newTime=timePicker.getEditor().getText();
        String newPlace=inputPlace.getText();
        String newType=inputType.getText();
        String newTeamName=inputTeamName.getText();
        String newEvaluation=inputEvaluation.getText();

        boolean flag=  newTime.isEmpty() || newType.isEmpty() || newTeamName.isEmpty() || newPlace.isEmpty() || newEvaluation.isEmpty();
        if(flag){
            MessageDialog.showDialog("信息不完整，请填写完整后点击此框");
            return;
        }

        DataRequest req =new DataRequest();
        req.add("studentId",studentId);
        req.add("newTime",newTime);
        req.add("newPlace",newPlace);
        req.add("newType",newType);
        req.add("newTeamName",newTeamName);
        req.add("newEvaluation",newEvaluation);
        DataResponse res=HttpRequestUtil.request("/api/practice/newPractice",req);
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
        req.add("practiceId",CommonMethod.getInteger(data,"practiceId"));;
        req.add("studentId",CommonMethod.getInteger(data,"studentId"));
        req.add("place",CommonMethod.getString(data,"place"));
        req.add("teamName",CommonMethod.getString(data,"teamName"));
        req.add("time",CommonMethod.getString(data,"time"));
        req.add("type",CommonMethod.getString(data,"type"));
        req.add("evaluation",CommonMethod.getString(data,"evaluation"));
        res = HttpRequestUtil.request("/api/practice/practiceEditSave",req); //从后台获取所有学生信息列表集合
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

