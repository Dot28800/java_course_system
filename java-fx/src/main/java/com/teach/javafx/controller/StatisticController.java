package com.teach.javafx.controller;

import com.teach.javafx.controller.base.MessageDialog;
import com.teach.javafx.controller.base.ToolController;
import com.teach.javafx.request.HttpRequestUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.MapValueFactory;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StatisticController extends ToolController {

    @FXML
    private TableView<Map> dataTableView;
    private ObservableList<Map> observableList = FXCollections.observableArrayList();  // TableView渲染列表
    private ArrayList<Map> statisticList = new ArrayList();
    @FXML
    private TableColumn<Map,String> numColumn;

    @FXML
    private TableColumn<Map,String> nameColumn;

    @FXML
    private TableColumn<Map,String> sumFeeColumn;

    @FXML
    private TableColumn<Map,String> GPAColumn;

    @FXML
    private TableColumn<Map,String> courseNumColumn;

    @FXML
    private TableColumn<Map,String> disPassNumColumn;
    @FXML
    private TableColumn<Map,String> practiceColumn;
    @FXML
    private TableColumn<Map,String> competitionColumn;
    @FXML
    private TableColumn<Map,String> dailyColumn;
    @FXML
    private TableColumn<Map,String> honorColumn;
    @FXML
    private TextField numNameField;


    /**
     * 将数据集合设置到面板上显示
     */
    private void setTableViewData() {
        observableList.clear();
        for (int j = 0; j < statisticList.size(); j++) {
            observableList.addAll(FXCollections.observableArrayList(statisticList.get(j)));
        }
        dataTableView.setItems(observableList);
    }

    @FXML
    public void queryAndResetTable(){
        DataRequest dataRequest = new DataRequest();
        DataResponse dataResponse = HttpRequestUtil.request("/api/statistics/getAllStatistics",dataRequest);
        statisticList = (ArrayList<Map>) dataResponse.getData();
        setTableViewData();
        MessageDialog.showDialog("刷新成功！");
    }

    @FXML
    public void initialize(){
        DataRequest dataRequest = new DataRequest();
        DataResponse dataResponse = HttpRequestUtil.request("/api/statistics/getAllStatistics",dataRequest);

        statisticList = (ArrayList<Map>) dataResponse.getData();

        numColumn.setCellValueFactory(new MapValueFactory<>("studentNum"));
        nameColumn.setCellValueFactory(new MapValueFactory<>("name"));
        sumFeeColumn.setCellValueFactory(new MapValueFactory<>("sumMoney"));
        GPAColumn.setCellValueFactory(new MapValueFactory<>("gpa"));
        courseNumColumn.setCellValueFactory(new MapValueFactory<>("sumOfCourse"));
        disPassNumColumn.setCellValueFactory(new MapValueFactory<>("sumOfDisPassCourse"));
        honorColumn.setCellValueFactory(new MapValueFactory<>("honorTimes"));
        dailyColumn.setCellValueFactory(new MapValueFactory<>("dailyTimes"));
        practiceColumn.setCellValueFactory(new MapValueFactory<>("practiceTimes"));
        competitionColumn.setCellValueFactory(new MapValueFactory<>("competitionTimes"));

        TableView.TableViewSelectionModel<Map> tsm = dataTableView.getSelectionModel();

        setTableViewData();

    }
    @FXML
    public void queryByNumName(){
        String numName=numNameField.getText();
        DataRequest req=new DataRequest();
        req.add("numName",numName);
        DataResponse res=HttpRequestUtil.request("/api/statistics/queryByNumName",req);
        if(res != null && res.getCode()== 0) {
            statisticList = (ArrayList<Map>)res.getData();
            //MessageDialog.showDialog("查找成功！");
        }else if(res!=null){
            MessageDialog.showDialog(res.getMsg());
        }else{
            MessageDialog.showDialog("连接错误，未正常返回请求！");
        }
        setTableViewData();
    }
}
