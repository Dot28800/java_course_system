package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.init.MarkInit;
import org.fatmansoft.teach.init.RankInit;
import org.fatmansoft.teach.models.Mark;
import org.fatmansoft.teach.models.Rank;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.CourseRepository;
import org.fatmansoft.teach.repository.MarkRepository;
import org.fatmansoft.teach.repository.RankRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/rank")
public class RankController {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private MarkRepository markRepository;
    @Autowired
    private RankRepository rankRepository;
    @Autowired
    private MarkInit markInit;
    @Autowired
    private RankInit rankInit;
    @PostMapping("/getRankList")
    public DataResponse getRankList(@Valid @RequestBody DataRequest dataRequest){
        String num= dataRequest.getString("numName");
        rankInit.initialize();
        List<Rank> rankList=rankRepository.getByStudentNumName(num);
        List dataList = new ArrayList<>();
        Map m;
        for(Rank r:rankList){
            if(r!=null){
                m = new HashMap<>();
                m.put("studentId",r.getStudent().getStudentId());
                m.put("num",r.getStudent().getPerson().getNum());
                m.put("name",r.getStudent().getPerson().getName());
                m.put("rank",r.getTotalRank());
                String mark=r.getAvgMark().toString();
                String GPA=r.getAvgGPA().toString();
                m.put("mark",mark.length()>4?mark.substring(0,4):mark);
                m.put("GPA",GPA.length()>4?GPA.substring(0,4):GPA);
                /*m.put("mark",r.getAvgMark());
                m.put("GPA",r.getAvgGPA());*/
                dataList.add(m);
            }
        }
        return CommonMethod.getReturnData(dataList);
    }
    @PostMapping("/getListByRank")
    public DataResponse getList(@Valid @RequestBody DataRequest dataRequest){
        String rank= dataRequest.getString("rank");
        if(rank==null){
            rank="";
        }
        rankInit.initialize();
        List<Rank> rankList=rankRepository.getByTotalRank(rank);
        List dataList = new ArrayList<>();
        Map m;
        for(Rank r:rankList){
            if(r!=null){
                m = new HashMap<>();
                m.put("studentId",r.getStudent().getStudentId());
                m.put("num",r.getStudent().getPerson().getNum());
                m.put("name",r.getStudent().getPerson().getName());
                m.put("rank",r.getTotalRank());
                String mark=r.getAvgMark().toString();
                String GPA=r.getAvgGPA().toString();
                m.put("mark",mark.length()>4?mark.substring(0,4):mark);
                m.put("GPA",GPA.length()>4?GPA.substring(0,4):GPA);
                /*m.put("mark",r.getAvgMark());
                m.put("GPA",r.getAvgGPA());*/
                dataList.add(m);
            }
        }
        return CommonMethod.getReturnData(dataList);

    }

    public void localStudentDelete(Integer studentId){
        Optional<Rank> op=rankRepository.getByStudentId(studentId);
        op.ifPresent(rank -> rankRepository.delete(rank));
    }
}
