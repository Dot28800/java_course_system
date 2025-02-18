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

public class CompetitionTableController {
    @FXML
    private TableView<Map> dataTableView;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> studentNumColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> studentNameColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> contestNameColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> contestTimeColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> prizeColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> rankColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> typeColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> instructorColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map, Button> positionColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map, Button> editColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map, Button> deleteColumn;
    @FXML
    private TextField inputContestName;
    @FXML
    private ComboBox<OptionItem> inputRankComboBox;
    @FXML
    private TextField inputInstructor;
    @FXML
    private TextField inputPrize;
    @FXML
    private TextField inputType;
    @FXML
    private TextField inputTime;
    @FXML
    private TextField inputPosition;
    @FXML
    private ComboBox<OptionItem> inputStudentComboBox;
    @FXML
    private ComboBox<OptionItem> studentComboBox=new ComboBox<>();
    @FXML
    private DatePicker timePicker;
    //private List<OptionItem> competitionList;//这个是干么的
    private List<OptionItem> rankList;
    private List<OptionItem> studentList;
    private ArrayList<Map> competitionList=new ArrayList<>();

    private ObservableList<Map> observableList= FXCollections.observableArrayList();
    //private CompetitionTableController competitionTableController = null;
    private CompetitionEditController competitionEditController = null;

    private Stage stage = null;
    private Integer competitionId=null;

    public List<OptionItem> getStudentList() {
        return studentList;
    }
    private String num;
    private String userName;
    @FXML
    private Label name;
    String userType;

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
            contestNameColumn.setCellValueFactory(new MapValueFactory<>("contestName"));
            contestTimeColumn.setCellValueFactory(new MapValueFactory<>("contestTime"));
            rankColumn.setCellValueFactory(new MapValueFactory<>("rank"));
            prizeColumn.setCellValueFactory(new MapValueFactory<>("prize"));
            editColumn.setCellValueFactory(new MapValueFactory<>("edit"));
            deleteColumn.setCellValueFactory(new MapValueFactory<>("delete"));
            typeColumn.setCellValueFactory(new MapValueFactory<>("type"));
            instructorColumn.setCellValueFactory(new MapValueFactory<>("instructor"));
            positionColumn.setCellValueFactory(new MapValueFactory<>("position"));
            DataRequest req1 = new DataRequest();
            studentList = HttpRequestUtil.requestOptionItemList("/api/competition/getStudentItemOptionList", req1); //从后台获取所有学生信息列表集合

            OptionItem item = new OptionItem(null, "0", "请选择");
            rankList = initRankList();
            inputRankComboBox.getItems().addAll(item);
            inputRankComboBox.getItems().addAll(rankList);
            studentComboBox.getItems().addAll(item);
            studentComboBox.getItems().addAll(studentList);

            inputStudentComboBox.getItems().addAll(item);
            inputStudentComboBox.getItems().addAll(studentList);
            dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            setTableViewData();
            onQueryButtonClick();
        }else {
            name.setText(userName);
            studentNumColumn.setCellValueFactory(new MapValueFactory("studentNum"));  //设置列值工程属性
            studentNameColumn.setCellValueFactory(new MapValueFactory<>("studentName"));
            contestNameColumn.setCellValueFactory(new MapValueFactory<>("contestName"));
            contestTimeColumn.setCellValueFactory(new MapValueFactory<>("contestTime"));
            rankColumn.setCellValueFactory(new MapValueFactory<>("rank"));
            prizeColumn.setCellValueFactory(new MapValueFactory<>("prize"));
            typeColumn.setCellValueFactory(new MapValueFactory<>("type"));
            instructorColumn.setCellValueFactory(new MapValueFactory<>("instructor"));
            positionColumn.setCellValueFactory(new MapValueFactory<>("position"));
            DataRequest req1 = new DataRequest();
            studentList = HttpRequestUtil.requestOptionItemList("/api/competition/getStudentItemOptionList", req1); //从后台获取所有学生信息列表集合

            OptionItem item = new OptionItem(null, "0", "请选择");
            rankList = initRankList();
            inputRankComboBox.getItems().addAll(item);
            inputRankComboBox.getItems().addAll(rankList);
            /*studentComboBox.getItems().addAll(item);
            studentComboBox.getItems().addAll(studentList);*/

            dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            setTableViewData();
            onQueryButtonClick();
        }
    }
    public List<OptionItem> initRankList(){
        List<OptionItem> ranks  = new ArrayList<>();
        ranks.add(new OptionItem(0,"","国际顶级"));
        ranks.add(new OptionItem(1,"","国家级"));
        ranks.add(new OptionItem(2,"","省级"));
        ranks.add(new OptionItem(3,"","校级"));
        ranks.add(new OptionItem(4,"","院级"));
        return ranks;
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

            res=HttpRequestUtil.request("/api/competition/getCompetitionList",req);
            if(res != null && res.getCode()== 0) {
                competitionList = (ArrayList<Map>)res.getData();
                setTableViewData();
            }else if(res!=null){
                MessageDialog.showDialog(res.getMsg());
            }else{
                MessageDialog.showDialog("通信错误！");
            }
        }else {
            String rankLevel="";
            OptionItem op;
            op = inputRankComboBox.getSelectionModel().getSelectedItem();
            if(op != null)
                rankLevel = op.getTitle();
            if(rankLevel.equals("请选择")){
                rankLevel="";
            }

            DataResponse res;
            DataRequest req =new DataRequest();
            req.add("rankLevel",rankLevel);
            req.add("num",num);
            req.add("name",userName);
            res=HttpRequestUtil.request("/api/competition/getCompetitionListByLevel",req);
            if(res != null && res.getCode()== 0) {
                competitionList = (ArrayList<Map>)res.getData();
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
        for (int j = 0; j < competitionList.size(); j++) {
            map = competitionList.get(j);
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
        Map data=competitionList.get(j);
        int jet=MessageDialog.choiceDialog("确定要删除吗");
        if(jet != MessageDialog.CHOICE_YES) {
            return;
        }
        competitionId= CommonMethod.getInteger(data,"competitionId");
        //System.out.println(courseAttendanceId);
        DataRequest req=new DataRequest();
        req.add("competitionId",competitionId);
        DataResponse res=HttpRequestUtil.request("/api/competition/deleteCompetition",req);
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
        Map data = competitionList.get(j);
        initDialog();
        competitionEditController.showDialog(data);

        MainApplication.setCanClose(false);
        stage.showAndWait();
    }

    private void initDialog(){
        if(stage!= null)
            return;
        FXMLLoader fxmlLoader ;
        Scene scene = null;
        try {
            fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Competition/competition-edit-dialog.fxml"));
            scene = new Scene(fxmlLoader.load(), 1200, 800);
            stage = new Stage();
            stage.initOwner(MainApplication.getMainStage());
            stage.initModality(Modality.NONE);
            stage.setAlwaysOnTop(true);
            stage.setScene(scene);
            stage.setTitle("竞赛经历修改对话框");
            stage.setOnCloseRequest(event ->{
                MainApplication.setCanClose(true);
            });
            competitionEditController = (CompetitionEditController) fxmlLoader.getController();
            competitionEditController.setCompetitionTableController(this);
            competitionEditController.init();

        } catch (IOException e) {
            throw new RuntimeException(e);
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
        //  String newContestname =  inputRankComboBox.getSelectionModel().getSelectedItem();
        String newTime=timePicker.getEditor().getText();
        String newInstructor=inputInstructor.getText();
        String newType=inputType.getText();
        String newPrize=inputPrize.getText();
        String newPosition=inputPosition.getText();
        String newContestName=inputContestName.getText();
        op=inputRankComboBox.getSelectionModel().getSelectedItem();
        if(op!=null){
            newRank=op.getTitle();
        }

        if(newRank==null){
            MessageDialog.showDialog("请选择荣誉奖项级别之后再点击此框");
            return;
        }

        boolean flag=  newTime.isEmpty() || newType.isEmpty() || newInstructor.isEmpty() || newPrize.isEmpty() || newPosition.isEmpty() || newContestName.isEmpty();
        if(flag){
            MessageDialog.showDialog("信息不完整，请填写完整后点击此框");
            return;
        }

        DataRequest req =new DataRequest();
        req.add("studentId",studentId);
        req.add("newRank",newRank);
        req.add("newTime",newTime);
        req.add("newInstructor",newInstructor);
        req.add("newType",newType);
        req.add("newPosition",newPosition);
        req.add("newPrize",newPrize);
        req.add("newContestName",newContestName);
        DataResponse res=HttpRequestUtil.request("/api/competition/newCompetition",req);
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
        req.add("competitionId",CommonMethod.getInteger(data,"competitionId"));
        req.add("rank",CommonMethod.getString(data,"rank"));
        req.add("position",CommonMethod.getString(data,"position"));
        req.add("prize",CommonMethod.getString(data,"prize"));
        req.add("time",CommonMethod.getString(data,"time"));
        req.add("type",CommonMethod.getString(data,"type"));
        req.add("instructor",CommonMethod.getString(data,"instructor"));
        req.add("contestName",CommonMethod.getString(data,"contestName"));
        res = HttpRequestUtil.request("/api/competition/competitionEditSave",req); //从后台获取所有学生信息列表集合
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

