package com.teach.javafx.controller;

import com.teach.javafx.controller.base.MessageDialog;
import com.teach.javafx.request.HttpRequestUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RankController {
    @FXML
    private TableView<Map> dataTableView;
    @FXML
    private TableColumn<Map,String> numColumn;   //学生信息表 编号列
    @FXML
    private TableColumn<Map,String> nameColumn; //学生信息表 名称列

    @FXML
    private TableColumn<Map, String> rankColumn;
    @FXML
    private TableColumn<Map,String> markColumn;
    @FXML
    private TableColumn<Map, Button> GPAColumn;
    @FXML
    private TextField numNameField;
    private ArrayList<Map> rankList = new ArrayList();

    private List markList=new ArrayList<>();
    private ObservableList<Map> observableList= FXCollections.observableArrayList();
    private Integer rankId;
    @FXML
    private TextField rankField;

    @FXML
    void initialize(){

        DataResponse userResponse = HttpRequestUtil.request("/secure/user/getUser", new DataRequest());
        if(userResponse==null){
            MessageDialog.showDialog("连接失败，请重试");
        }
        Map data= (Map) userResponse.getData();
        String userType = (String) data.get("typeId");
        String num= (String) data.get("personNum");

        if(userType==null){
            MessageDialog.showDialog("访问失败");
            return;
        }


        if (userType.equals("管理员")){
            DataResponse res;
            DataRequest req = new DataRequest();
            req.add("numName", "");//为空，查询所有学生
            //req.add("studentId","");//为空，查询所有学生
            res = HttpRequestUtil.request("/api/rank/getRankList", req); //从后台获取所有学生信息列表集合
            if (res != null && res.getCode() == 0) {//只有200才会返回非空
                rankList = (ArrayList<Map>) res.getData();
            }
        }else {
            DataResponse res;
            DataRequest req = new DataRequest();

            req.add("numName", num);//为空，查询所有学生
            //req.add("studentId","");//为空，查询所有学生
            res = HttpRequestUtil.request("/api/rank/getRankList", req); //从后台获取所有学生信息列表集合
            if (res != null && res.getCode() == 0) {//只有200才会返回非空
                rankList = (ArrayList<Map>) res.getData();
            }
        }

        dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        numColumn.setCellValueFactory(new MapValueFactory<>("num"));
        nameColumn.setCellValueFactory(new MapValueFactory<>("name"));
        rankColumn.setCellValueFactory(new MapValueFactory<>("rank"));
        markColumn.setCellValueFactory(new MapValueFactory<>("mark"));
        GPAColumn.setCellValueFactory(new MapValueFactory<>("GPA"));
        setTableViewData();
        //onQueryButtonClick();



    }

    private void setTableViewData() {
        observableList.clear();
        for (int j = 0; j < rankList.size(); j++) {
            observableList.addAll(FXCollections.observableArrayList(rankList.get(j)));
        }
        dataTableView.setItems(observableList);
    }
    @FXML
    void onQueryButtonClick(){
        String numName=numNameField.getText();
        if(numName==null){
            numName="";
        }
        DataRequest req=new DataRequest();
        req.add("numName",numName);
        DataResponse res=HttpRequestUtil.request("/api/rank/getRankList",req);
        if(res!=null && res.getCode()==0){
            MessageDialog.showDialog("查找成功");
            rankList = (ArrayList<Map>) res.getData();
        } else if (res!=null) {
            MessageDialog.showDialog(res.getMsg());

        }else{
            MessageDialog.showDialog("刷新失败");
        }
        setTableViewData();

    }
    @FXML
    private void queryByRank(){
        DataRequest req=new DataRequest();
        req.add("rank",rankField.getText());
        DataResponse res=HttpRequestUtil.request("/api/rank/getListByRank",req);
        if(res!=null && res.getCode()==0){
            MessageDialog.showDialog("查找成功");
            rankList = (ArrayList<Map>) res.getData();
        } else if (res!=null) {
            MessageDialog.showDialog(res.getMsg());

        }else{
            MessageDialog.showDialog("刷新失败");
        }
        setTableViewData();
    }

    @FXML
    private void refresh(){
        DataRequest req=new DataRequest();
        req.add("numName","");
        DataResponse res=HttpRequestUtil.request("/api/rank/getRankList",req);
        if(res!=null && res.getCode()==0){
            MessageDialog.showDialog("刷新成功");
            rankList = (ArrayList<Map>) res.getData();
        } else if (res!=null) {
            MessageDialog.showDialog(res.getMsg());

        }else{
            MessageDialog.showDialog("刷新失败");
        }
        setTableViewData();
    }
}
