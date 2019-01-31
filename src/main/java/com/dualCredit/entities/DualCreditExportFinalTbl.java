package com.dualCredit.entities;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class DualCreditExportFinalTbl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String cleanEmail;

    private Date stageSubmitDate;

    private String opt;

    private String course;


    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getCleanEmail() {
        return cleanEmail;
    }

    public void setCleanEmail(String cleanEmail) {
        this.cleanEmail = cleanEmail;
    }

    public Date getStageSubmitDate() {
        return stageSubmitDate;
    }

    public void setStageSubmitDate(Date stageSubmitDate) {
        this.stageSubmitDate = stageSubmitDate;
    }

    public String getOpt() {
        return opt;
    }

    public void setOpt(String opt) {
        this.opt = opt;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DualCreditExportFinalTbl that = (DualCreditExportFinalTbl) o;

        if (cleanEmail != null ? !cleanEmail.equals(that.cleanEmail) : that.cleanEmail != null) return false;
        return course != null ? course.equals(that.course) : that.course == null;
    }

    @Override
    public int hashCode() {
        int result = cleanEmail != null ? cleanEmail.hashCode() : 0;
        result = 31 * result + (course != null ? course.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Id +"," + cleanEmail+"," + stageSubmitDate +"," + opt +"," + course +"\n";
    }
}
