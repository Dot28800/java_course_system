package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.payload.response.OptionItem;
import org.fatmansoft.teach.payload.response.OptionItemList;
import org.fatmansoft.teach.repository.CompetitionRepository;
import org.fatmansoft.teach.repository.ConsumptionRepository;
import org.fatmansoft.teach.repository.HonorRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/consumption")
public class ConsumptionController {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ConsumptionRepository consumptionRepository;
    @PostMapping("/getStudentItemOptionList")
    public OptionItemList getStudentItemOptionList(@Valid @RequestBody DataRequest dataRequest) {
        List<Student> sList = studentRepository.findStudentListByNumName("");  //数据库查询操作
        OptionItem item;
        List<OptionItem> itemList = new ArrayList();
        for (Student s : sList) {
            itemList.add(new OptionItem( s.getStudentId(),s.getStudentId()+"", s.getPerson().getNum()+"-"+s.getPerson().getName()));
        }
        return new OptionItemList(0, itemList);
    }
    @PostMapping("/getConsumptionList")
    public DataResponse getConsumptionList(@Valid @RequestBody DataRequest dataRequest){
        Integer studentId = dataRequest.getInteger("studentId");
        if(studentId == null)
            studentId = 0;

        List<Consumption> cList=consumptionRepository.findByStudentStudentId(studentId);
        List dataList=new ArrayList<>();
        Map m;
        for(Consumption s:cList){
            if (s != null) {
                m = new HashMap();
                m.put("consumptionId", s.getConsumptionId()+"");
                m.put("studentId",s.getStudent().getStudentId()+"");

                m.put("consumeTime",s.getConsumeTime());
                m.put("money",s.getMoney());
                m.put("student",s.getStudent());
                m.put("consumeReason",s.getConsumeReason());
                m.put("studentNum",s.getStudent().getPerson().getNum());
                m.put("studentName",s.getStudent().getPerson().getName());
                m.put("consumePlace",s.getConsumePlace());

                dataList.add(m);
            }

        }
        return CommonMethod.getReturnData(dataList);
    }
    @PostMapping("/getList")
    public DataResponse getList(@Valid @RequestBody DataRequest dataRequest){
        String num= dataRequest.getString("num");
        if(num==null){
            num="";
        }
        String name= dataRequest.getString("name");
        if(name==null){
            name="";
        }
        String reason= dataRequest.getString("reason");
        if(reason==null){
            reason="";
        }

        List<Consumption> cList=consumptionRepository.findByReasonAndStudent(num,name,reason);
        List dataList=new ArrayList<>();
        Map m;
        for(Consumption s:cList){
            if (s != null) {
                m = new HashMap();
                m.put("consumptionId", s.getConsumptionId()+"");
                m.put("studentId",s.getStudent().getStudentId()+"");
                m.put("consumeTime",s.getConsumeTime());
                m.put("money",s.getMoney());
                m.put("student",s.getStudent());
                m.put("consumeReason",s.getConsumeReason());
                m.put("studentNum",s.getStudent().getPerson().getNum());
                m.put("studentName",s.getStudent().getPerson().getName());
                m.put("consumePlace",s.getConsumePlace());
                dataList.add(m);
            }

        }
        return CommonMethod.getReturnData(dataList);
    }
    @PostMapping("/getReasonList")
    public DataResponse getReasonList(@Valid@RequestBody DataRequest dataRequest){
        Map<String,Double> m=dataRequest.getMap("reasonMap");
        String num= dataRequest.getString("num");
        Double moneyValue=0.00;
        String money="";
        for(String reasonName:m.keySet()){
            List<Consumption> consumptionList=consumptionRepository.findByConsumeReason(reasonName,num);
            for(Consumption c:consumptionList){
                moneyValue+=c.getMoney();
            }

            m.put(reasonName, moneyValue);
            moneyValue=0.00;
        }
        return CommonMethod.getReturnData(m);
    }
    @PostMapping("/newConsumption")
    public DataResponse newConsumption(@Valid@RequestBody DataRequest dataRequest){
        Integer studentId= dataRequest.getInteger("studentId");
        Optional<Student> op=studentRepository.findById(studentId);
        if(!op.isPresent() ){
            return CommonMethod.getReturnMessageError("该学生不存在");
        }
        Double newMoney= Double.parseDouble(dataRequest.getString("newMoney"));

        String newReason=dataRequest.getString("newReason");
        String newTime=dataRequest.getString("newTime");

        String newConsumePlace= dataRequest.getString("newConsumePlace");

        Consumption h=new Consumption();
        h.setStudent(op.get());
        h.setMoney(newMoney);
        h.setConsumeTime(newTime);
        h.setConsumeReason(newReason);
        h.setConsumePlace(newConsumePlace);
        consumptionRepository.save(h);
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/deleteConsumption")
    public DataResponse deleteCompetition(@Valid@RequestBody DataRequest dataRequest){
        Integer consumptionId=dataRequest.getInteger("consumptionId");
        if(consumptionId==null){
            return CommonMethod.getReturnMessageError("传输错误，找不到数据");
        }
        Optional<Consumption> op=consumptionRepository.findById(consumptionId);
        if(!op.isPresent()){
            return CommonMethod.getReturnMessageError("找不到这条记录！");
        }
        Consumption h= op.get();
        consumptionRepository.delete(h);
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/consumptionEditSave")
    public DataResponse consumptionEditSave(@Valid@RequestBody DataRequest dataRequest){
        Integer consumptionId=dataRequest.getInteger("consumptionId");
        if(consumptionId==null){
            return CommonMethod.getReturnMessageError("传输错误，找不到数据");
        }
        Optional<Consumption> op=consumptionRepository.findById(consumptionId);
        if(!op.isPresent()){
            return CommonMethod.getReturnMessageError("找不到这条记录！");
        }
        Consumption h=op.get();
        //再确定一下有没有缺少的字段
        String consumePlace= dataRequest.getString("consumePlace");
        String time= dataRequest.getString("time");
        String consumeReason= dataRequest.getString("reason");
        //Double money= Double.parseDouble(dataRequest.getString("money"));
        Double money= Double.valueOf(dataRequest.getString("money"));
        h.setConsumePlace(consumePlace);
        h.setConsumeTime(time);
        h.setConsumeReason(consumeReason);
        h.setMoney(money);
        consumptionRepository.save(h);
        return CommonMethod.getReturnMessageOK();
    }
    public void localStudentDelete(Integer studentId){
        List<Consumption> consumptionList=consumptionRepository.findByStudentId(studentId);
        consumptionRepository.deleteAll(consumptionList);
    }
}

