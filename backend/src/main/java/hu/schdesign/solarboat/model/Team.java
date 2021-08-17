package hu.schdesign.solarboat.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Entity
@Table
public class Team {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    @NotNull
    @Size(min=2, max=50)
    private String name_hu;
    @NotNull
    @Size(min=2, max=50)
    private String name_en;
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Member> members;
    @OneToOne(cascade = CascadeType.ALL)
    private Member leader;
    @NotNull
    @Size(min=2, max=2000000)
    private String description_hu;
    @NotNull
    @Size(min=2, max=2000000)
    private String description_en;
    @NotNull
    private long teamType;


    public Team(@JsonProperty("name_hu") String name_hu, @JsonProperty("name_en") String name_en,
                @JsonProperty("members") Set<Member> members, @JsonProperty("leader") Member leader,
                @JsonProperty("description_hu") String description_hu, @JsonProperty("description_en") String description_en,
                @JsonProperty("teamType") long teamType) {

        this.name_hu = name_hu;
        this.name_en = name_en;
        this.members = members;
        this.leader = leader;
        this.description_hu = description_hu;
        this.description_en = description_en;
        this.teamType = teamType;
    }

    public Team() {
    }

    public boolean addMember(Member member){
        return this.members.add(member);
    }
    public boolean deleteMember(Member member){
        return this.members.remove(member);
    }

    public boolean isMember(Member member){
        return members.contains(member);
    }
    public long getId() {
        return id;
    }

    public String getName_hu() {
        return name_hu;
    }

    public void setName_hu(String name_hu) {
        this.name_hu = name_hu;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public Set<Member> getMembers() {
        return members;
    }

    public void setMembers(Set<Member> members) {
        this.members = members;
    }

    public Member getLeader() {
        return leader;
    }

    public void setLeader(Member leader) {
        this.leader = leader;
    }

    public String getDescription_hu() {
        return description_hu;
    }

    public void setDescription_hu(String description_hu) {
        this.description_hu = description_hu;
    }

    public String getDescription_en() {
        return description_en;
    }

    public void setDescription_en(String description_en) {
        this.description_en = description_en;
    }

    public long getTeamType() {
        return teamType;
    }

    public void setTeamType(long teamType) {
        this.teamType = teamType;
    }
}
