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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsumptionTableController {
    @FXML
    private TableView<Map> dataTableView;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> studentNumColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> studentNameColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> consumePlaceColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> consumeTimeColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> moneyColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map,String> consumeReasonColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map, Button> editColumn;
    @FXML
    private javafx.scene.control.TableColumn<Map, Button> deleteColumn;
    @FXML
    private TextField inputConsumePlace;
    @FXML
    private ComboBox<OptionItem> inputReasonComboBox;
    @FXML
    private TextField inputMoney;
    @FXML
    private TextField inputTime;
    @FXML
    private ComboBox<OptionItem> inputStudentComboBox;
    @FXML
    private ComboBox<OptionItem> studentComboBox=new ComboBox<>();
    @FXML
    private DatePicker timePicker;
    //private List<OptionItem> competitionList;//这个是干么的
    private List<OptionItem> reasonList;
    private List<OptionItem> studentList;
    private ArrayList<Map> consumptionList=new ArrayList<>();

    private ObservableList<Map> observableList= FXCollections.observableArrayList();

    private ConsumptionEditController consumptionEditController = null;

    private Stage stage = null;
    private Integer consumptionId=null;

    public List<OptionItem> getStudentList() {
        return studentList;
    }
    private String num;
    private String userName;
    String userType;
    @FXML
    private Label nameLabel;
    @FXML
    private BarChart<String,Number> barChart;
    @FXML
    private PieChart pieChart;
    private Map disMap;
    @FXML
    private Label sumLabel;
    private Double sum=0.0;

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
            consumePlaceColumn.setCellValueFactory(new MapValueFactory<>("consumePlace"));
            consumeReasonColumn.setCellValueFactory(new MapValueFactory<>("consumeReason"));
            consumeTimeColumn.setCellValueFactory(new MapValueFactory<>("consumeTime"));
            moneyColumn.setCellValueFactory(new MapValueFactory<>("money"));
            editColumn.setCellValueFactory(new MapValueFactory<>("edit"));
            deleteColumn.setCellValueFactory(new MapValueFactory<>("delete"));
            timePicker.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));
            DataRequest req1 = new DataRequest();
            studentList = HttpRequestUtil.requestOptionItemList("/api/consumption/getStudentItemOptionList", req1); //从后台获取所有学生信息列表集合

            OptionItem item = new OptionItem(null, "0", "请选择");
            reasonList = initReasonList();
            inputReasonComboBox.getItems().addAll(item);
            inputReasonComboBox.getItems().addAll(reasonList);
            studentComboBox.getItems().addAll(item);
            studentComboBox.getItems().addAll(studentList);

            inputStudentComboBox.getItems().addAll(item);
            inputStudentComboBox.getItems().addAll(studentList);
            dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            setTableViewData();
            onQueryButtonClick();
            setTableViewData();
        }else {
            nameLabel.setText(userName);
            studentNumColumn.setCellValueFactory(new MapValueFactory("studentNum"));  //设置列值工程属性
            studentNameColumn.setCellValueFactory(new MapValueFactory<>("studentName"));
            consumePlaceColumn.setCellValueFactory(new MapValueFactory<>("consumePlace"));
            consumeReasonColumn.setCellValueFactory(new MapValueFactory<>("consumeReason"));
            consumeTimeColumn.setCellValueFactory(new MapValueFactory<>("consumeTime"));
            moneyColumn.setCellValueFactory(new MapValueFactory<>("money"));


            DataRequest req1 = new DataRequest();
            studentList = HttpRequestUtil.requestOptionItemList("/api/consumption/getStudentItemOptionList", req1); //从后台获取所有学生信息列表集合

            OptionItem item = new OptionItem(null, "0", "请选择");
            reasonList = initReasonList();
            inputReasonComboBox.getItems().addAll(item);
            inputReasonComboBox.getItems().addAll(reasonList);
            /*studentComboBox.getItems().addAll(item);
            studentComboBox.getItems().addAll(studentList);

            inputStudentComboBox.getItems().addAll(item);
            inputStudentComboBox.getItems().addAll(studentList);*/
            dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            initCharts();
            onQueryButtonClick();
            setTableViewData();
        }

    }

    public void initCharts(){
        pieChart.setData(createPieChartData());
        pieChart.setClockwise(false);
        pieChart.setLabelLineLength(15);
        pieChart.setLabelsVisible(true);
        pieChart.setStartAngle(90);
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        String[] reasons=new String[reasonList.size()];
        for(int i=0;i< reasons.length;i++){
            reasons[i]=reasonList.get(i).getTitle();
        }
        for(String s:reasons){
            series1.getData().add(new XYChart.Data<>(s,(Number) disMap.get(s)));
        }
        ObservableList<XYChart.Series<String, Number>> barData =
                FXCollections.<XYChart.Series<String, Number>>observableArrayList();
        barData.add(series1);
        barChart.setLegendVisible(true);
        barChart.setData(barData);
    }
    private ObservableList<PieChart.Data> createPieChartData() {
        List<PieChart.Data> mapList=new ArrayList<>();
        Map<String ,Double> m=new HashMap<>();
        for(OptionItem op:reasonList) {
           m.put(op.getTitle(),0.00);
        }
        m.remove("请选择");
        DataRequest dataRequest=new DataRequest();
        dataRequest.add("reasonMap",m);
        dataRequest.add("num",num);
        DataResponse res=HttpRequestUtil.request("/api/consumption/getReasonList",dataRequest);
        if(res==null){
            MessageDialog.showDialog("接收失败");
            return (ObservableList<PieChart.Data>) new Object();
        }
        disMap= (Map) res.getData();
        Double moneySum=0.00;
        for(int i=0;i<disMap.size();i++){

            String reasonName= reasonList.get(i).getTitle();
            if(reasonName.equals("不限")){
                continue;
            }
            if((Double)disMap.get(reasonName)>0){
                PieChart.Data data=new PieChart.Data(reasonName, (Double) disMap.get(reasonName));

                mapList.add(data);
                moneySum+=(Double) disMap.get(reasonName);
            }
        }
        ObservableList<PieChart.Data> list = createPieChartDataFromExistingList(mapList);
        pieChart.setData(list);
        sumLabel.setText(String.format("%.2f", moneySum));

        return list;
    }

    private ObservableList<PieChart.Data> createPieChartDataFromExistingList(List<PieChart.Data> existingDataList) {
        return FXCollections.observableArrayList(existingDataList);
    }

    public List<OptionItem> initReasonList(){
        List<OptionItem> reasons  = new ArrayList<>();
        reasons.add(new OptionItem(0,"","饮食"));
        reasons.add(new OptionItem(1,"","出行"));
        reasons.add(new OptionItem(2,"","衣物"));
        reasons.add(new OptionItem(3,"","兴趣爱好"));
        reasons.add(new OptionItem(4,"","健康"));
        reasons.add(new OptionItem(5,"","学习"));
        reasons.add(new OptionItem(6,"","其它"));
        return reasons;
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

            res=HttpRequestUtil.request("/api/consumption/getConsumptionList",req);
            if(res != null && res.getCode()== 0) {
                consumptionList = (ArrayList<Map>)res.getData();
                setTableViewData();
            }else if(res!=null){
                MessageDialog.showDialog(res.getMsg());
            }else{
                MessageDialog.showDialog("通信错误！");
            }
        }else {
            String reason="";
            OptionItem op;
            op = inputReasonComboBox.getSelectionModel().getSelectedItem();
            if(op!=null){
                reason=op.getTitle();
            }
            reason=reason.equals("请选择")?"":reason;
            DataRequest req=new DataRequest();
            req.add("num",num);
            req.add("name",userName);
            req.add("reason",reason);
            DataResponse res=HttpRequestUtil.request("/api/consumption/getList",req);
            if(res != null && res.getCode()== 0) {
                consumptionList = (ArrayList<Map>)res.getData();
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
    void onNewButtonClick(){

        Integer studentId = 0;
        String newReason=null;
        OptionItem op;
        op = inputStudentComboBox.getSelectionModel().getSelectedItem();
        if(op != null)
            studentId=Integer.parseInt(op.getValue());
        else{
            MessageDialog.showDialog("请选择学生后再点击此框");
            return;
        }

        String newTime=timePicker.getEditor().getText();
        Double newMoney=Double.parseDouble(inputMoney.getText());
        String newConsumePlace=inputConsumePlace.getText();
        boolean moneyFlag=isValidAmount(String.valueOf(newMoney));
        if(!moneyFlag){
            MessageDialog.showDialog("金额格式不符合要求（除了数字字符外只允许有小数点，且最多只能有两位小数）");
            return;
        }
        op=inputReasonComboBox.getSelectionModel().getSelectedItem();
        if(op!=null){
            newReason=op.getTitle();
        }

        if(newReason==null || newReason.equals("请选择")){
            MessageDialog.showDialog("请选择消费原因之后再点击此框");
            return;
        }

        boolean flag=  newTime.isEmpty() || newConsumePlace.isEmpty() || newMoney==null ;
        if(flag){
            MessageDialog.showDialog("信息不完整，请填写完整后点击此框");
            return;
        }

        DataRequest req =new DataRequest();
        req.add("studentId",studentId);
        req.add("newReason",newReason);
        req.add("newTime",newTime);
        req.add("newMoney",newMoney);
        req.add("newConsumePlace",newConsumePlace);

        DataResponse res=HttpRequestUtil.request("/api/consumption/newConsumption",req);
        if(res!=null && res.getCode() == 0){
            MessageDialog.showDialog("新建记录成功！");
            onQueryButtonClick();
        }else if(res!=null){
            MessageDialog.showDialog(res.getMsg());
        }else{
            MessageDialog.showDialog("通信异常！");
        }

    }
    private boolean isValidAmount(String amount) {
        // 正则表达式：
        // ^ 表示字符串开始
        // \d+ 表示一个或多个数字
        // (\.\d{1,2})? 表示小数部分（可选），点号后面跟着1到2位数字
        // $ 表示字符串结束
        // 注意：这个正则表达式不会处理逗号或空格作为千位分隔符的情况
        String regex = "^\\d+(\\.\\d{1,2})?$";
        return amount.matches(regex);
    }

    private void setTableViewData() {
        observableList.clear();
        Map map;
        Button editButton;
        Button deleteButton;
        for (int j = 0; j < consumptionList.size(); j++) {
            map = consumptionList.get(j);
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
        Map data=consumptionList.get(j);
        int jet=MessageDialog.choiceDialog("确定要删除吗");
        if(jet != MessageDialog.CHOICE_YES) {
            return;
        }
        consumptionId= CommonMethod.getInteger(data,"consumptionId");

        DataRequest req=new DataRequest();
        req.add("consumptionId",consumptionId);
        DataResponse res=HttpRequestUtil.request("/api/consumption/deleteConsumption",req);
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
        Map data = consumptionList.get(j);
        initDialog();
        consumptionEditController.showDialog(data);

        MainApplication.setCanClose(false);
        stage.showAndWait();
    }

    private void initDialog() {

        if(stage!= null)
            return;
        FXMLLoader fxmlLoader ;
        Scene scene = null;
        try {
            fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Consumption/consumption-edit-dialog.fxml"));
            scene = new Scene(fxmlLoader.load(), 1200, 800);
            stage = new Stage();
            stage.initOwner(MainApplication.getMainStage());
            stage.initModality(Modality.NONE);
            stage.setAlwaysOnTop(true);
            stage.setScene(scene);
            stage.setTitle("消费信息修改对话框");
            stage.setOnCloseRequest(event ->{
                MainApplication.setCanClose(true);
            });
            consumptionEditController = (ConsumptionEditController) fxmlLoader.getController();
            consumptionEditController.setConsumptionTableController(this);
            consumptionEditController.init();

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
        DataRequest req =new DataRequest();
        req.add("consumptionId",CommonMethod.getInteger(data,"consumptionId"));
        req.add("reason",CommonMethod.getString(data,"reason"));
        req.add("consumePlace",CommonMethod.getString(data,"consumePlace"));
        req.add("money",CommonMethod.getString(data,"money"));
        req.add("time",CommonMethod.getString(data,"time"));
        res = HttpRequestUtil.request("/api/consumption/consumptionEditSave",req); //从后台获取所有学生信息列表集合
        if(res != null && res.getCode()== 0) {
            onQueryButtonClick();
            MessageDialog.showDialog("保存成功！");
        }else if(res!=null){
            MessageDialog.showDialog(res.getMsg());
        }else {
            MessageDialog.showDialog("通信异常！");
        }
    }
    @FXML
    public void refresh(){
        initCharts();
    }
}
