package com.teach.javafx.controller;

import com.teach.javafx.MainApplication;
import com.teach.javafx.controller.base.LocalDateStringConverter;
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

public class InnovativeController {

    @FXML
    private TableView<Map> dataTableView;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> studentNumColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> studentNameColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> titleColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> timeColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> hostColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> typeColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> levelColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map, Button> editColumn;
    @FXML
    private TableColumn<Map,Button> deleteColumn;
    @FXML
    private TextField inputTime;
    @FXML
    private TextField inputTitle;
    @FXML
    private TextField inputType;
    @FXML
    private ComboBox<OptionItem> levelBox;
    @FXML
    private TextField inputHost;
    @FXML
    private DatePicker timePicker;

    @FXML
    private ComboBox<OptionItem> inputStudentComboBox;
    @FXML
    private ComboBox<OptionItem> studentComboBox = new ComboBox<>();
    private List<OptionItem> levelList;

    private ArrayList<Map> pList = new ArrayList<>();
    private ArrayList<Map> cList = new ArrayList<>();
    private ArrayList<Map> sList = new ArrayList<>();

    private ObservableList<Map> observableList = FXCollections.observableArrayList();
    private List<OptionItem> studentList;

    //private HonorEditController honorEditController = null;
    private InnovativeEditController innovativeEditController = null;

    @FXML
    private Button button;
    private Stage stage = null;
    private Integer innovativeId=null;

    public List<OptionItem> getStudentList() {
        return studentList;
    }

    @FXML
    public void initialize(){
        studentNumColumn.setCellValueFactory(new MapValueFactory("studentNum"));  //设置列值工程属性
        studentNameColumn.setCellValueFactory(new MapValueFactory<>("studentName"));
        hostColumn.setCellValueFactory(new MapValueFactory<>("host"));
        timeColumn.setCellValueFactory(new MapValueFactory<>("time"));
        levelColumn.setCellValueFactory(new MapValueFactory<>("level"));
        editColumn.setCellValueFactory(new MapValueFactory<>("edit"));
        deleteColumn.setCellValueFactory(new MapValueFactory<>("delete"));
        titleColumn.setCellValueFactory(new MapValueFactory<>("title"));
        typeColumn.setCellValueFactory(new MapValueFactory<>("type"));
        timePicker.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));
        DataRequest req1 =new DataRequest();
        studentList = HttpRequestUtil.requestOptionItemList("/api/innovative/getStudentItemOptionList",req1);
        OptionItem item = new OptionItem(null,"0","请选择");
        /*studentComboBox.getItems().addAll(item);
        studentComboBox.getItems().addAll(studentList);*/
        levelList=initLevelList();
        levelBox.getItems().addAll(item);
        levelBox.getItems().addAll(levelList);
        studentComboBox.getItems().addAll(item);
        studentComboBox.getItems().addAll(studentList);

        inputStudentComboBox.getItems().addAll(item);
        inputStudentComboBox.getItems().addAll(studentList);
        dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        if (button.getText().equals("查找实践记录"))
            onQueryButtonClickP();
        else if (button.getText().equals("查找科研记录"))
            onQueryButtonClickS();
        else if (button.getText().equals("查找竞赛记录"))
            onQueryButtonClickC();
    }
    public List<OptionItem> initLevelList(){
        List<OptionItem> levels=new ArrayList<>();
        levels.add(new OptionItem(0,"","院级"));
        levels.add(new OptionItem(1,"","校级"));
        levels.add(new OptionItem(2,"","省级"));
        levels.add(new OptionItem(3,"","国家级"));
        return levels;
    }

    @FXML
    void onQueryButtonClickP(){
        onQueryButtonClick(1);
    }
    @FXML
    void onQueryButtonClickS(){
        onQueryButtonClick(2);
    }
    @FXML
    void onQueryButtonClickC(){
        onQueryButtonClick(3);
    }
    void onQueryButtonClick(Integer innovativeType){
        Integer studentId = 0;
        OptionItem op;
        op = studentComboBox.getSelectionModel().getSelectedItem();
        if(op != null)
            studentId = Integer.parseInt(op.getValue());

        DataResponse res;
        DataRequest req =new DataRequest();
        req.add("studentId",studentId);
        req.add("innovativeType",innovativeType);
        res=HttpRequestUtil.request("/api/innovative/getInnovativeList",req);
        if(res != null && res.getCode()== 0) {
            switch (innovativeType){
                case 1:
                    pList = (ArrayList<Map>)res.getData();
                    break;
                case 2:
                    sList = (ArrayList<Map>)res.getData();
                    break;
                case 3:
                    cList = (ArrayList<Map>)res.getData();
                    break;
            }
            setTableViewData(innovativeType);
        }else if(res!=null){
            MessageDialog.showDialog(res.getMsg());
        }else{
            MessageDialog.showDialog("通信错误！");
        }


    }


    private void setTableViewData(Integer innovativeType) {
        observableList.clear();
        Map map;
        Button editButton;
        Button deleteButton;
        ArrayList<Map> innovativeList = new ArrayList<>();
        switch (innovativeType){
            case 1:
                innovativeList = pList;
                break;
            case 2:
                innovativeList = sList;
                break;
            case 3:
                innovativeList = cList;
                break;
        }
        for (int j = 0; j < innovativeList.size(); j++) {
            map = innovativeList.get(j);
            editButton = new Button("修改");
            editButton.setId("edit"+j);
            deleteButton=new Button("删除");
            deleteButton.setId("delete"+j);
            editButton.setOnAction(e->{
                editItem(((Button)e.getSource()).getId(), innovativeType);
            });
            deleteButton.setOnAction(e->{
                deleteItem(((Button)e.getSource()).getId(), innovativeType);
            });
            map.put("edit",editButton);
            map.put("delete",deleteButton);
            observableList.addAll(FXCollections.observableArrayList(map));
        }
        dataTableView.setItems(observableList);
    }

    private void deleteItem(String name, Integer innovativeType) {
        if(name==null){
            return;
        }
        int j = Integer.parseInt(name.substring(6,name.length()));
        Map data = null;
        switch (innovativeType){
            case 1:
                data = pList.get(j);
                break;
            case 2:
                data = sList.get(j);
                break;
            case 3:
                data = cList.get(j);
                break;
        }
        int jet=MessageDialog.choiceDialog("确定要删除吗");
        if(jet != MessageDialog.CHOICE_YES) {
            return;
        }
        innovativeId = CommonMethod.getInteger(data,"innovativeId");
        //System.out.println(courseAttendanceId);
        DataRequest req=new DataRequest();
        req.add("innovativeId",innovativeId);
        DataResponse res=HttpRequestUtil.request("/api/innovative/deleteInnovative",req);
        if(res.getCode()==0){
            MessageDialog.showDialog("删除记录成功！");
            onQueryButtonClick(1);
        }else{
            MessageDialog.showDialog(res.getMsg());
        }

    }

    private void editItem(String name, Integer innovativeType) {
        if(name == null)
            return;
        int j = Integer.parseInt(name.substring(4,name.length()));
        Map data = null;
        switch (innovativeType){
            case 1:
                data = pList.get(j);
                break;
            case 2:
                data = sList.get(j);
                break;
            case 3:
                data = cList.get(j);
                break;
        }
        initDialog();
        innovativeEditController.showDialog(data);

        MainApplication.setCanClose(false);
        stage.showAndWait();
    }
    private void initDialog() {
        if(stage!= null)
            return;
        FXMLLoader fxmlLoader ;
        Scene scene = null;
        try {
            fxmlLoader = new FXMLLoader(MainApplication.class.getResource("innovative/editDialog.fxml"));
            scene = new Scene(fxmlLoader.load(), 1200, 800);
            stage = new Stage();
            stage.initOwner(MainApplication.getMainStage());
            stage.initModality(Modality.NONE);
            stage.setAlwaysOnTop(true);
            stage.setScene(scene);

            stage.setTitle("荣誉信息修改对话框");
            stage.setOnCloseRequest(event ->{
                MainApplication.setCanClose(true);
            });
            innovativeEditController = (InnovativeEditController) fxmlLoader.getController();
            innovativeEditController.setInnovativeController(this);;
            innovativeEditController.init();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onNewButtonClickP(){
        onNewButtonClick(1);
    }
    @FXML
    void onNewButtonClickS(){
        onNewButtonClick(2);
    }
    @FXML
    void onNewButtonClickC(){
        onNewButtonClick(3);
    }

    void onNewButtonClick(Integer innovativeType){
        Integer studentId = 0;
        String newLevel=null;
        OptionItem op;
        op = inputStudentComboBox.getSelectionModel().getSelectedItem();
        //newLevel=levelBox.getSelectionModel().getSelectedItem().getTitle();//补充的地方
        if(op != null)
            studentId=Integer.parseInt(op.getValue());
        else{
            MessageDialog.showDialog("请选择学生后再点击我");
            return;
        }
        String newTime=timePicker.getEditor().getText();
        String newTitle=inputTitle.getText();
        String newType=inputType.getText();
        String newHost=inputHost.getText();
        op=levelBox.getSelectionModel().getSelectedItem();
        if(op!=null){
            newLevel=op.getTitle();
        }
        if(newLevel==null){
            MessageDialog.showDialog("请选择荣誉奖项级别之后再点击我");
            return;
        }

        boolean flag=  newTime.isEmpty() || newType.isEmpty() || newTitle.isEmpty() || newHost.isEmpty();
        if(flag){
            MessageDialog.showDialog("信息不完整，请填写完整后点击我");
            return;
        }
        DataRequest req =new DataRequest();
        req.add("studentId",studentId);
        req.add("newTime",newTime);
        req.add("newTitle",newTitle);
        req.add("newType",newType);
        req.add("newHost",newHost);
        req.add("newLevel",newLevel);

        req.add("innovativeType",innovativeType);

        DataResponse res=HttpRequestUtil.request("/api/innovative/newInnovative",req);
        if(res!=null && res.getCode() == 0) {
            //honorId = CommonMethod.getIntegerFromObject(res.getData());
            MessageDialog.showDialog("新建记录成功！");
            onQueryButtonClick(1);
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
        req.add("innovativeId",CommonMethod.getInteger(data,"innovativeId"));
        req.add("level",CommonMethod.getString(data,"level"));
        req.add("title",CommonMethod.getString(data,"title"));
        req.add("host",CommonMethod.getString(data,"host"));
        req.add("time",CommonMethod.getString(data,"time"));
        req.add("type",CommonMethod.getString(data,"type"));
        res = HttpRequestUtil.request("/api/innovative/innovativeEditSave",req); //从后台获取所有学生信息列表集合
        if(res != null && res.getCode()== 0) {
            onQueryButtonClick(1);
            MessageDialog.showDialog("保存成功！");
        }else if(res!=null){
            MessageDialog.showDialog(res.getMsg());
        }else {
            MessageDialog.showDialog("通信异常！");
        }
    }
}

