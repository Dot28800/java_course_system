package org.fatmansoft.teach.init;

import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.repository.HonorRepository;
import org.fatmansoft.teach.repository.MarkRepository;
import org.fatmansoft.teach.repository.RankRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class RankInit {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private MarkRepository markRepository;
    @Autowired
    private RankRepository rankRepository;
    @Autowired
    private HonorRepository honorRepository;
    private Integer studentId;
    int maxId=1;
    public void initialize(){
        List<Student> studentList=studentRepository.findAll();
        for(Student s:studentList){
            studentId=s.getStudentId();
            Optional<Rank> rankOp=rankRepository.getByStudentId(studentId);
            List<Mark> markList=markRepository.getByStudentId(studentId);
            if(rankOp.isPresent()){
                reConstruct(markList,s, rankOp.get());
            }else {
                construct(markList,s);
            }


            //rankRepository.save(r);
        }
        List<Rank> rankList=rankRepository.findAll();
        for(Rank r:rankList){
            if(!studentList.contains(r.getStudent())){
                rankRepository.delete(r);

            }
        }
        setOrder();


    }
    private void reConstruct(List<Mark> markList,Student s,Rank r){
        Double credit;
        Course c;
        Double multiSum=0.0;
        Double creditSum=0.0;
        if(markList.isEmpty()){
            r.setAvgMark(0.00);
            r.setAvgGPA(0.00);
        }
        else{
            for(Mark m:markList){
                c=m.getCourseChoose().getCourse();
                credit= Double.valueOf(c.getCredit());
                creditSum+=credit;
                Double mark=m.getMark();
                if (mark==null){
                    mark=0.0;
                }
                multiSum+=mark*credit;
            }
            r.setAvgMark(multiSum/creditSum);
            double avgGPA=(r.getAvgMark()-50)/10;
            if(avgGPA<0){
                avgGPA=0.0;
            }
            r.setAvgGPA(avgGPA);
        }

        rankRepository.save(r);
    }

    private void construct(List<Mark> markList,Student s){

        List<Honor> honorList=honorRepository.findByStudentId(s.getStudentId());
        int count= honorList.size();
        Rank rank=new Rank();
        rank.setTotalRank(String.valueOf(count));

        Double credit;//每一门课学分
        Course c;
        Rank r=new Rank();
        Double multiSum=0.0;//课程总数
        Double creditSum=0.0;//总学分

        if(markList.isEmpty()){
            r.setStudent(s);
            r.setAvgMark(0.00);
            r.setAvgGPA(0.00);
        }
        else{
            for(Mark m:markList){
                c=m.getCourseChoose().getCourse();
                credit= Double.valueOf(c.getCredit());
                creditSum+=credit;
                Double mark=m.getMark();
                if (mark==null){
                    mark=0.0;
                }
                multiSum+=mark*credit;
            }

            r.setStudent(s);
            r.setAvgMark(multiSum/creditSum);
            double avgGPA=(r.getAvgMark()-50)/10;
            if(avgGPA<0){
                avgGPA=0.0;
            }
            r.setAvgGPA(avgGPA);
        }

        rankRepository.save(r);

    }

    private void setOrder(){
        List<Rank> rankList=rankRepository.findAll();
        Rank[] newList=getSortedList(rankList);
        for(int i=0;i< newList.length;i++){
            newList[i].setTotalRank(String.valueOf(i+1));
            rankRepository.save(newList[i]);
        }

    }

    Rank[] getSortedList(List<Rank> mapList){
        Rank[] ranks= mapList.toArray(new Rank[0]);
        Arrays.sort(ranks, new Comparator<Rank>() {
            @Override
            public int compare(Rank o1, Rank o2) {
                return o2.getAvgMark().compareTo(o1.getAvgMark());
            }


        });
        return ranks;
    }
}
