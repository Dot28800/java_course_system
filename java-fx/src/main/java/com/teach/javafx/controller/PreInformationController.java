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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.util.CommonMethod;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;

import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.*;

public class PreInformationController {
    @FXML
    public PieChart distribution;
    private List<OptionItem> genderList;

    @FXML
    private TableView<Map> dataTableView;
    @FXML
    private TableColumn<Map,String> studentNumColumn;
    @FXML
    private TableColumn<Map,String> studentNameColumn;
    @FXML
    private TableColumn<Map,String> genderColumn;
    @FXML
    private TableColumn<Map,String> ageColumn;
    @FXML
    private TableColumn<Map,String> sourcePlaceColumn;
    @FXML
    private TableColumn<Map,String> preSchoolColumn;
    @FXML
    private TableColumn<Map,String> preScoreColumn;
    @FXML
    private TableColumn<Map, String> preRankColumn;
    @FXML
    private TableColumn<Map, String> preHonorColumn;
    @FXML
    private TableColumn<Map,Button> editColumn;
    @FXML
    private TableColumn<Map,Button> deleteColumn;
    @FXML
    private TextField numNameField;

    private ArrayList<Map> preInformationList=new ArrayList<>();

    private ObservableList<Map> observableList= FXCollections.observableArrayList();  // TableView渲染列表

    private List<OptionItem> studentList;
    @FXML
    private ComboBox<OptionItem> provinceComboBox;
    @FXML
    private ComboBox<OptionItem> genderComboBox;


   // private List<OptionItem> courseList;

    private PreEditController preEditController = null;

    private PreNewController preNewController=null;
    private Stage stage = null;
    List<OptionItem> provinceList;
    private Integer preInformationId=null;

    @FXML
    private BarChart<String,Number> rankChart;

    public List<OptionItem> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<OptionItem> studentList) {
        this.studentList = studentList;
    }
    String userType;
    String personNum;

    private boolean flag=false;
    @FXML
    public void initialize(){
        DataResponse userResponse = HttpRequestUtil.request("/secure/user/getUser", new DataRequest());
        if(userResponse==null){
            MessageDialog.showDialog("连接失败，请重试");
        }
        Map data= (Map) userResponse.getData();
        userType = (String) data.get("typeId");
        personNum= (String) data.get("personNum");
        if(userType==null){
            MessageDialog.showDialog("访问失败");
            return;
        }
        if (userType.equals("管理员")) {
            DataResponse res;
            DataRequest req =new DataRequest();
            req.add("numName","");//为空，查询所有学生
            //req.add("studentId","");//为空，查询所有学生
            res = HttpRequestUtil.request("/api/pre/getPreListByNumName",req); //从后台获取所有学生信息列表集合
            if(res != null && res.getCode()== 0) {//只有200才会返回非空
                preInformationList = (ArrayList<Map>)res.getData();
            }
            studentNumColumn.setCellValueFactory(new MapValueFactory("num"));  //设置列值工程属性
            studentNameColumn.setCellValueFactory(new MapValueFactory<>("name"));
            genderColumn.setCellValueFactory(new MapValueFactory<>("gender"));
            ageColumn.setCellValueFactory(new MapValueFactory<>("age"));
            sourcePlaceColumn.setCellValueFactory(new MapValueFactory<>("province"));
            preSchoolColumn.setCellValueFactory(new MapValueFactory<>("preSchool"));
            preScoreColumn.setCellValueFactory(new MapValueFactory<>("preScore"));
            preRankColumn.setCellValueFactory(new MapValueFactory<>("preRank"));
            preHonorColumn.setCellValueFactory(new MapValueFactory<>("preHonor"));
            editColumn.setCellValueFactory(new MapValueFactory<>("edit"));
            deleteColumn.setCellValueFactory(new MapValueFactory<>("delete"));
            genderList = HttpRequestUtil.getDictionaryOptionItemList("XBM");
            provinceList = initProvinces();
            DataRequest req1 =new DataRequest();
            studentList=HttpRequestUtil.requestOptionItemList("/api/pre/getStudentItemOptionList",req1);
            OptionItem item = new OptionItem(null,"0","请选择生源省份");
            provinceComboBox.getItems().addAll(item);
            provinceComboBox.getItems().addAll(provinceList);
            dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            createChart();
            QueryByNumName();
        }else{
            DataResponse res;
            DataRequest req =new DataRequest();
            req.add("numName",personNum);//为空，查询所有学生
            res = HttpRequestUtil.request("/api/pre/getPreListByNumName",req); //从后台获取所有学生信息列表集合
            if(res != null && res.getCode()== 0) {//只有200才会返回非空
                preInformationList = (ArrayList<Map>)res.getData();
            }
            studentNumColumn.setCellValueFactory(new MapValueFactory("num"));  //设置列值工程属性
            studentNameColumn.setCellValueFactory(new MapValueFactory<>("name"));
            genderColumn.setCellValueFactory(new MapValueFactory<>("gender"));
            ageColumn.setCellValueFactory(new MapValueFactory<>("age"));
            sourcePlaceColumn.setCellValueFactory(new MapValueFactory<>("province"));
            preSchoolColumn.setCellValueFactory(new MapValueFactory<>("preSchool"));
            preScoreColumn.setCellValueFactory(new MapValueFactory<>("preScore"));
            preRankColumn.setCellValueFactory(new MapValueFactory<>("preRank"));
            preHonorColumn.setCellValueFactory(new MapValueFactory<>("preHonor"));
            editColumn.setCellValueFactory(new MapValueFactory<>("edit"));
            genderList = HttpRequestUtil.getDictionaryOptionItemList("XBM");
            provinceList = initProvinces();
            DataRequest req1 =new DataRequest();
            studentList=HttpRequestUtil.requestOptionItemList("/api/pre/getStudentItemOptionList",req1);
            OptionItem item = new OptionItem(null,"0","请选择生源省份");
            /*provinceComboBox.getItems().addAll(item);
            provinceComboBox.getItems().addAll(provinceList);*/
            dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            //createChart();
            QueryByNumName();
        }



    }
    private void createChart(){
        distribution.setData(createPieChartData());
        distribution.setClockwise(false);
        distribution.setLabelLineLength(15);
        distribution.setLabelsVisible(true);
        distribution.setStartAngle(90);
        DataResponse res=HttpRequestUtil.request("/api/pre/getRankData",new DataRequest());
        Map<String ,Number> data= null;
        if (res != null) {
            data = (Map<String, Number>) res.getData();
        }
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        String[] ranks={"<3000","3000~4500","4500~5500","5500~6500","6500~7500","7500~9000",">9000"};
        for(String s:ranks){
            series1.getData().add(new XYChart.Data<>(s,(Number) data.get(s)));
        }
        series1.setName("人数");
        ObservableList<XYChart.Series<String, Number>> barData =
                FXCollections.<XYChart.Series<String, Number>>observableArrayList();
        barData.add(series1);
        rankChart.setData(barData);
    }
    private ObservableList<PieChart.Data> createPieChartData() {
        List<PieChart.Data> mapList=new ArrayList<>();
        Map m=new HashMap<>();
        List<String> provinceArr=new ArrayList<>();
        for(OptionItem op:provinceList) {
            String curProvince = op.getTitle();
            //if(!curProvince.equals("不限")){
                m.put(curProvince,0);
            //}
            /*provinceArr.add(curProvince);*/

        }
        m.remove("不限");

        DataRequest dataRequest=new DataRequest();
        dataRequest.add("provinceMap",m);
        DataResponse res=HttpRequestUtil.request("/api/pre/getNumByProvince",dataRequest);
        if(res==null){
            MessageDialog.showDialog("接收失败");
            return (ObservableList<PieChart.Data>) new Object();
        }
        Map disMap= (Map) res.getData();
        int a=5;
        for(int i=0;i<disMap.size();i++){
            String provinceName= provinceList.get(i).getTitle();
            if(provinceName.equals("不限")){
                continue;
            }
            if((Double)disMap.get(provinceName)>0){
                PieChart.Data data=new PieChart.Data(provinceName, (Double) disMap.get(provinceName));

                mapList.add(data);
            }

        }

        ObservableList<PieChart.Data> list = createPieChartDataFromExistingList(mapList);
        distribution.setData(list);
        return list;
    }

    private ObservableList<PieChart.Data> createPieChartDataFromExistingList(List<PieChart.Data> existingDataList) {
        return FXCollections.observableArrayList(existingDataList);
    }



    public List<OptionItem> initProvinces(){
        List<OptionItem> provinces=new ArrayList<>();
        provinces.add(new OptionItem(0,"","不限"));
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
    public void QueryByProvince(){
        String province="";
        OptionItem op;
        op = provinceComboBox.getSelectionModel().getSelectedItem();
        if(op != null)
            province = op.getTitle();
        if(province.equals("不限")){
            province="";
        }
        DataResponse res;
        DataRequest req =new DataRequest();
        req.add("province",province);

        res = HttpRequestUtil.request("/api/pre/getPreListByProvince",req); //从后台获取所有学生信息列表集合
        if(res != null && res.getCode()== 0) {
            preInformationList = (ArrayList<Map>)res.getData();
        }else if(res!=null){
            MessageDialog.showDialog(res.getMsg());
        }else {
            MessageDialog.showDialog("通信异常！");
        }
        setTableViewData();

    }
    @FXML
    public void QueryByNumName(){
        String numName;
        if(userType.equals("管理员")){
            numName = numNameField.getText();
        }
        else{
            numName=personNum;
        }
        DataRequest req = new DataRequest();
        req.add("numName",numName);
        DataResponse res = HttpRequestUtil.request("/api/pre/getPreListByNumName",req);
        if(res != null && res.getCode()== 0) {
             preInformationList= (ArrayList<Map>)res.getData();
            setTableViewData();
        }else if(res!=null){
            MessageDialog.showDialog(res.getMsg());
        }else {
            MessageDialog.showDialog("通信异常！");
        }
    }
    @FXML
    public void ComprehensiveQuery(){
        String numName = numNameField.getText();
        String province="";
        OptionItem op;
        op = provinceComboBox.getSelectionModel().getSelectedItem();
        if(op != null)
            province = op.getTitle();
        if(province.equals("不限")){
            province="";
        }
        DataResponse res;
        DataRequest req =new DataRequest();
        req.add("province",province);
        req.add("numName",numName);
        res = HttpRequestUtil.request("/api/pre/getComprehensiveList",req); //从后台获取所有学生信息列表集合
        if(res != null && res.getCode()== 0) {
            preInformationList = (ArrayList<Map>)res.getData();
        }else if(res!=null){
            MessageDialog.showDialog(res.getMsg());
        }else {
            MessageDialog.showDialog("通信异常！");
        }
        setTableViewData();
    }
    private void setTableViewData() {
        observableList.clear();
        Map map;
        Button editButton;
        Button deleteButton;
        for (int j = 0; j < preInformationList.size(); j++) {
            map = preInformationList.get(j);
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
            observableList.addAll(FXCollections.observableArrayList(preInformationList.get(j)));
        }
        dataTableView.setItems(observableList);
    }

    public void onNewButtonClick(){
        initNewDialog();
        preNewController.showDialog(new HashMap<>());
        MainApplication.setCanClose(false);
        stage.showAndWait();

    }
    private void initNewDialog() {
        if(stage!= null && flag==false)
            return;
        FXMLLoader fxmlLoader ;
        flag=true;
        Scene scene = null;
        try {
            fxmlLoader = new FXMLLoader(MainApplication.class.getResource("preInformation/pre-new-dialog.fxml"));
            scene = new Scene(fxmlLoader.load(), 800, 400);
            stage = new Stage();
            stage.initOwner(MainApplication.getMainStage());
            stage.initModality(Modality.NONE);
            stage.setAlwaysOnTop(true);
            stage.setScene(scene);
            stage.setTitle("入学前信息修改对话框");
            stage.setOnCloseRequest(event ->{
                MainApplication.setCanClose(true);
            });
            preNewController = (PreNewController) fxmlLoader.getController();
            preNewController.setPreInformationController(this);
            preNewController.init();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void doNewClose(String cmd, Map data) {
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
        String province = CommonMethod.getString(data,"province");
        if(province == null) {
            MessageDialog.showDialog("没有选中生源地省份不能添加保存！");
            return;
        }
        /*if(CommonMethod.getString(data,"time")==null || CommonMethod.getString(data,"flag")==null){
            MessageDialog.showDialog("信息不完整，请重新输入！");
            return;
        }*/
        DataRequest req =new DataRequest();
        String preSchool=CommonMethod.getString(data,"preSchool");
        String preScore=CommonMethod.getString(data,"preScore");
        String preRank=CommonMethod.getString(data,"preRank");
        String preHonor=CommonMethod.getString(data,"preHonor");
        boolean flag= !preSchool.isEmpty() && !preScore.isEmpty() && !preRank.isEmpty() && !preHonor.isEmpty();
        if(!flag){
            MessageDialog.showDialog("信息不完整，请修改后再提交");
            return;
        }
        try{
            Integer s=Integer.parseInt(preScore);
            Integer r=Integer.parseInt(preRank);
        }catch (Exception e){
            MessageDialog.showDialog("分数和省排名格式不对，请修改后再提交");
            return;
        }
        req.add("studentId",studentId);
        req.add("province",province);
        req.add("preInformationId",CommonMethod.getInteger(data,"preInformationId"));
        req.add("preSchool",CommonMethod.getString(data,"preSchool"));
        req.add("preScore",CommonMethod.getString(data,"preScore"));
        req.add("preRank",CommonMethod.getString(data,"preRank"));
        req.add("preHonor",CommonMethod.getString(data,"preHonor"));
        res = HttpRequestUtil.request("/api/pre/preNewSave",req); //从后台获取所有学生信息列表集合
        if(res != null && res.getCode()== 0) {
            ComprehensiveQuery();
            MessageDialog.showDialog("保存成功！");
        }else if(res!=null){
            MessageDialog.showDialog(res.getMsg());
        }else {
            MessageDialog.showDialog("通信异常！");
        }

    }

    public void editItem(String name){
        if(name == null)
            return;
        int j = Integer.parseInt(name.substring(4,name.length()));
        Map data = preInformationList.get(j);
        initEditDialog();
        preEditController.showDialog(data);

        MainApplication.setCanClose(false);
        stage.showAndWait();

    }
    private void initEditDialog() {
        if(stage!= null && flag==false)
            return;
        FXMLLoader fxmlLoader ;
        flag=true;
        Scene scene = null;
        try {
            fxmlLoader = new FXMLLoader(MainApplication.class.getResource("preInformation/pre-edit-dialog.fxml"));
            scene = new Scene(fxmlLoader.load(), 800, 400);
            stage = new Stage();
            stage.initOwner(MainApplication.getMainStage());
            stage.initModality(Modality.NONE);
            stage.setAlwaysOnTop(true);
            stage.setScene(scene);
            stage.setTitle("入学前信息修改对话框");
            stage.setOnCloseRequest(event ->{
                MainApplication.setCanClose(true);
            });
            preEditController = (PreEditController) fxmlLoader.getController();
            preEditController.setPreInformationController(this);
            preEditController.init();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void doEditClose(String cmd, Map data) {
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
        String province = CommonMethod.getString(data,"province");
        if(province == null) {
            MessageDialog.showDialog("没有选中生源地省份不能添加保存！");
            return;
        }
        /*if(CommonMethod.getString(data,"time")==null || CommonMethod.getString(data,"flag")==null){
            MessageDialog.showDialog("信息不完整，请重新输入！");
            return;
        }*/
        DataRequest req =new DataRequest();
        String preSchool=CommonMethod.getString(data,"preSchool");
        String preScore=CommonMethod.getString(data,"preScore");
        String preRank=CommonMethod.getString(data,"preRank");
        String preHonor=CommonMethod.getString(data,"preHonor");
        boolean flag= !preSchool.isEmpty() && !preScore.isEmpty() && !preRank.isEmpty() && !preHonor.isEmpty();
        if(!flag){
            MessageDialog.showDialog("信息不完整，请修改后再提交");
            return;
        }
        try{
            Integer s=Integer.parseInt(preScore);
            Integer r=Integer.parseInt(preRank);
        }catch (Exception e){
            MessageDialog.showDialog("分数和省排名格式不对，请修改后再提交");
            return;
        }
        req.add("studentId",studentId);
        req.add("province",province);
        req.add("preInformationId",CommonMethod.getInteger(data,"preInformationId"));
        req.add("preSchool",CommonMethod.getString(data,"preSchool"));
        req.add("preScore",CommonMethod.getString(data,"preScore"));
        req.add("preRank",CommonMethod.getString(data,"preRank"));
        req.add("preHonor",CommonMethod.getString(data,"preHonor"));
        res = HttpRequestUtil.request("/api/pre/preEditSave",req); //从后台获取所有学生信息列表集合
        if(res != null && res.getCode()== 0) {
            if(userType.equals("管理员"))
            ComprehensiveQuery();
            else{
                QueryByNumName();
            }
            MessageDialog.showDialog("保存成功！");
        }else if(res!=null){
            MessageDialog.showDialog(res.getMsg());
        }else {
            MessageDialog.showDialog("通信异常！");
        }

    }
    public void deleteItem(String name){
        if(name==null){
            return;
        }
        int j = Integer.parseInt(name.substring(6,name.length()));
        Map data=preInformationList.get(j);
        int jet=MessageDialog.choiceDialog("确定要删除吗");
        if(jet != MessageDialog.CHOICE_YES) {
            return;
        }
        preInformationId=CommonMethod.getInteger(data,"preInformationId");
        //System.out.println(courseAttendanceId);
        DataRequest req=new DataRequest();
        req.add("preInformationId",preInformationId);
        DataResponse res=HttpRequestUtil.request("/api/pre/delete",req);
        if(res.getCode()==0){
            MessageDialog.showDialog("删除记录成功！");
            ComprehensiveQuery();
        }else{
            MessageDialog.showDialog(res.getMsg());
        }

    }
    @FXML
    public void refresh() {
        createChart();
    }
}
