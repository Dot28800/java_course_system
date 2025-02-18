package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.Competition;
import org.fatmansoft.teach.models.FamilyMember;
import org.fatmansoft.teach.models.Honor;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.payload.response.OptionItem;
import org.fatmansoft.teach.payload.response.OptionItemList;
import org.fatmansoft.teach.repository.CompetitionRepository;
import org.fatmansoft.teach.repository.HonorRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/competition")
public class CompetitionController {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CompetitionRepository competitionRepository;

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
    @PostMapping("/getCompetitionList")
    public DataResponse getCompetitionList(@Valid @RequestBody DataRequest dataRequest){
        Integer studentId = dataRequest.getInteger("studentId");
        if(studentId == null)
            studentId = 0;

        List<Competition> cList=competitionRepository.findByStudentStudentId(studentId);
        List dataList=new ArrayList<>();
        Map m;
        for(Competition s:cList){
            if (s != null) {
                m = new HashMap();
                m.put("competitionId", s.getCompetitionId()+"");
                m.put("studentId",s.getStudent().getStudentId()+"");
                m.put("contestName",s.getContestName());
                m.put("contestTime",s.getContestTime());
                m.put("prize",s.getPrize());
                m.put("student",s.getStudent());
                m.put("rank",s.getRank());
                m.put("studentNum",s.getStudent().getPerson().getNum());
                m.put("studentName",s.getStudent().getPerson().getName());
                m.put("instructor",s.getInstructor());
                m.put("type",s.getType());
                m.put("position",s.getPosition());
                dataList.add(m);
            }

        }
        return CommonMethod.getReturnData(dataList);
    }
    @PostMapping("/getCompetitionListByLevel")
    public DataResponse getCompetitionListByLevel(@Valid @RequestBody DataRequest dataRequest){
        String level= dataRequest.getString("rankLevel");
        String num= dataRequest.getString("num");
        if(num==null){
            num="";
        }
        String name= dataRequest.getString("name");
        if(name==null){
            name="";
        }
        if(level==null){
            level="";
        }
        List<Competition> cList=competitionRepository.getByLevel(level,num,name);
        List dataList=new ArrayList<>();
        Map m;
        for(Competition s:cList){
            if (s != null) {
                m = new HashMap();
                m.put("competitionId", s.getCompetitionId()+"");
                m.put("studentId",s.getStudent().getStudentId()+"");
                m.put("contestName",s.getContestName());
                m.put("contestTime",s.getContestTime());
                m.put("prize",s.getPrize());
                m.put("student",s.getStudent());
                m.put("rank",s.getRank());
                m.put("studentNum",s.getStudent().getPerson().getNum());
                m.put("studentName",s.getStudent().getPerson().getName());
                m.put("instructor",s.getInstructor());
                m.put("type",s.getType());
                m.put("position",s.getPosition());
                dataList.add(m);
            }

        }
        return CommonMethod.getReturnData(dataList);

    }
    @PostMapping("/newCompetition")
    public DataResponse newCompetition(@Valid@RequestBody DataRequest dataRequest){
        Integer studentId= dataRequest.getInteger("studentId");
        Optional<Student> op=studentRepository.findById(studentId);
        if(!op.isPresent() ){
            return CommonMethod.getReturnMessageError("该学生不存在");
        }
        String newPrize= dataRequest.getString("newPrize");
        String newRank=dataRequest.getString("newRank");
        String newTime=dataRequest.getString("newTime");
        String newPosition= dataRequest.getString("newPosition");
        String newType= dataRequest.getString("newType");
        String newInstructor =  dataRequest.getString("newInstructor");
        String newContestName= dataRequest.getString("newContestName");
        Competition h=new Competition();
        h.setStudent(op.get());
        h.setContestTime(newTime);
        h.setContestName(newContestName);
        h.setInstructor(newInstructor);
        h.setPosition(newPosition);
        h.setPrize(newPrize);
        h.setRank(newRank);
        h.setType(newType);
        competitionRepository.save(h);
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/deleteCompetition")
    public DataResponse deleteCompetition(@Valid@RequestBody DataRequest dataRequest){
        Integer competitionId= dataRequest.getInteger("competitionId");
        if(competitionId==null){
            return CommonMethod.getReturnMessageError("传输错误，找不到数据");
        }
        Optional<Competition> op=competitionRepository.findById(competitionId);
        if(!op.isPresent()){
            return CommonMethod.getReturnMessageError("找不到这条记录！");
        }
        Competition h= op.get();
        competitionRepository.delete(h);
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/competitionEditSave")
    public DataResponse competitionEditSave(@Valid@RequestBody DataRequest dataRequest){
        Integer competitionId=dataRequest.getInteger("competitionId");
        if(competitionId==null){
            return CommonMethod.getReturnMessageError("传输错误，找不到数据");
        }
        Optional<Competition> op=competitionRepository.findById(competitionId);
        if(!op.isPresent()){
            return CommonMethod.getReturnMessageError("找不到这条记录！");
        }
        Competition h=op.get();
        //再确定一下有没有缺少的字段
        String position= dataRequest.getString("position");
        String time= dataRequest.getString("time");
        String instructor= dataRequest.getString("instructor");
        String type= dataRequest.getString("type");
        String rank= dataRequest.getString("rank");
        String prize= dataRequest.getString("prize");
        String contestName=dataRequest.getString("contestName");
        h.setPosition(position);
        h.setType(type);
        h.setContestTime(time);
        h.setRank(rank);
        h.setInstructor(instructor);
        h.setPrize(prize);
        h.setContestName(contestName);
        competitionRepository.save(h);
        return CommonMethod.getReturnMessageOK();
    }
    public void localStudentDelete(Integer studentId){
        List<Competition> studentList=competitionRepository.getByStudentStudentId(studentId);
        competitionRepository.deleteAll(studentList);
    }
}
