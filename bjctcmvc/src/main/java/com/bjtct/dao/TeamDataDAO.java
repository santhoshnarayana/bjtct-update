package com.bjtct.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import com.bjtct.connection.JdbcConnection;
import com.bjtct.form.BestPlayersForm;
import com.bjtct.form.FixturesForm;
import com.bjtct.form.GeneralStatsForm;
import com.bjtct.form.LatestNewsForm;
import com.bjtct.form.PoolForm;
import com.bjtct.form.ResultsForm;
import com.bjtct.form.TeamMemberForm;
import com.bjtct.form.TeamRankingForm;
import com.bjtct.pojo.Team;
import com.bjtct.query.string.SQLConstants;

public class TeamDataDAO {

    public static Team getTeam(Long teamId) {

        Team team = null;

        String query = SQLConstants.GET_TEAM_BY_TEAM_ID;

        try {
            team = new Team();
            Connection con = JdbcConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setLong(1, teamId);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                team.setId(rs.getLong("id"));
                team.setName(rs.getString("name"));
                team.setDescribe(rs.getString("describe"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return team;

    }
    
    /**
     * To use existing connection
     * @param con
     * @param teamId
     * @return
     */
    private static  Team getTeam(Connection con,Long teamId) {

        Team team = null;

        String query = SQLConstants.GET_TEAM_BY_TEAM_ID;

        try {
            team = new Team();
            //Connection con = JdbcConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setLong(1, teamId);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                team.setId(rs.getLong("id"));
                team.setName(rs.getString("name"));
                team.setDescribe(rs.getString("describe"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return team;

    }

    public static List<Team> getAllTeams() {

        List<Team> teamList = null;
        Team team = null;
        String query = SQLConstants.GET_ALL_TEAMS;

        try {
            teamList = new ArrayList<Team>();
            Connection con = JdbcConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                team = new Team();

                team.setId(rs.getLong("id"));
                team.setName(rs.getString("name"));
                team.setDescribe(rs.getString("describe"));
                team.setLogo(rs.getBlob("logo"));
                team.setTeamphoto(rs.getBlob("teamphoto"));
                teamList.add(team);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return teamList;

    }

    /**
     * To get the team members info
     * @param teamId
     * @param teamMemberId
     * @return
     */
    public static TeamMemberForm getNextTeamMember(Long teamId, Long teamMemberId) {

        TeamMemberForm teamMember = null;

        Team team = null;
        String query = SQLConstants.GET_NEXT_TEAM_MEMBER;
        Connection con = null;
        try {

            teamMember = new TeamMemberForm();
            con = JdbcConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setLong(1, teamMemberId);
            pstmt.setLong(2, teamId);

            ResultSet rs = pstmt.executeQuery();
            SimpleDateFormat myFormat = new SimpleDateFormat("dd-MMM-yyyy");

            while (rs.next()) {

                teamMember.setId(rs.getLong("id"));
                teamMember.setTeamId(rs.getLong("teamid"));
                teamMember.setmName(rs.getString("mname"));
                teamMember.setSurName(rs.getString("surname"));
                Date d = rs.getDate("dob");

                if (null != d) {

                    teamMember.setDob(myFormat.format(d));
                }
                teamMember.setAge(rs.getLong("age"));
                teamMember.setRecidence(rs.getString("recidence"));
                teamMember.setProfession(rs.getString("profession"));
                teamMember.setMeritalStatus(rs.getString("meritalstatus"));
                d = rs.getDate("aniversary");
                if (null != d) {
                    teamMember.setAniversary(myFormat.format(d));
                }
                teamMember.setBloodGroup(rs.getString("bloodgroup"));
                teamMember.setSocialInvolveMent(rs.getString("socialinvolvement"));
                teamMember.setContact(rs.getString("contact"));
                teamMember.setPhoto(new String(Base64.encodeBase64(rs.getBlob("photo").getBytes(1, (int) rs.getBlob("photo").length()))));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return teamMember;

    }
    
    /**
     * To get the team members info
     * @param teamId
     * @param teamMemberId
     * @return
     */
    public static TeamMemberForm getNextTeamMember(Long teamId, Long teamMemberId,Connection con) {

        TeamMemberForm teamMember = null;

        Team team = null;
        String query = SQLConstants.GET_NEXT_TEAM_MEMBER;
        //Connection con = null;
        try {

            teamMember = new TeamMemberForm();
           // con = JdbcConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setLong(1, teamMemberId);
            pstmt.setLong(2, teamId);

            ResultSet rs = pstmt.executeQuery();
            SimpleDateFormat myFormat = new SimpleDateFormat("dd-MMM-yyyy");

            while (rs.next()) {

                teamMember.setId(rs.getLong("id"));
                teamMember.setTeamId(rs.getLong("teamid"));
                teamMember.setmName(rs.getString("mname"));
                teamMember.setSurName(rs.getString("surname"));
                Date d = rs.getDate("dob");

                if (null != d) {

                    teamMember.setDob(myFormat.format(d));
                }
                teamMember.setAge(rs.getLong("age"));
                teamMember.setRecidence(rs.getString("recidence"));
                teamMember.setProfession(rs.getString("profession"));
                teamMember.setMeritalStatus(rs.getString("meritalstatus"));
                d = rs.getDate("aniversary");
                if (null != d) {
                    teamMember.setAniversary(myFormat.format(d));
                }
                teamMember.setBloodGroup(rs.getString("bloodgroup"));
                teamMember.setSocialInvolveMent(rs.getString("socialinvolvement"));
                teamMember.setContact(rs.getString("contact"));
                teamMember.setPhoto(new String(Base64.encodeBase64(rs.getBlob("photo").getBytes(1, (int) rs.getBlob("photo").length()))));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        return teamMember;

    }


    /**
     * To get the record info
     * @param teamId
     * @return
     */
    public static Long getTotalRecordsCount(Long teamId) {
        Long totalCount = null;
        Connection con = null;
        try {
            con = JdbcConnection.getConnection();

            PreparedStatement pstmt = con.prepareStatement(SQLConstants.GET_RECORDS_TOTAL_COUNT);
            pstmt.setLong(1, teamId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                totalCount = rs.getLong(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalCount;
    }

    public static Long getFirstRecordId(long teamId) {

        Long firstRecordId = null;
        Connection con = null;
        try {
            con = JdbcConnection.getConnection();

            PreparedStatement pstmt = con.prepareStatement(SQLConstants.GET_FIRST_RECORD_ID_OF_TEAM);
            pstmt.setLong(1, teamId);
            ResultSet rs = pstmt.executeQuery();

            while (null != rs && rs.next()) {
                firstRecordId = rs.getLong(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return firstRecordId;
    }

    /**
     * To get the ratings of all teams by given tabName
     * 
     * @param tabName
     * @return
     */
    public static List<TeamRankingForm> getRanking(String tabName) {

        List<TeamRankingForm> teamRankingList = null;
        TeamRankingForm teamRanking = null;
        String query = SQLConstants.GET_RANKING_BY_TAB_NAME.replaceAll("#",tabName.toLowerCase().replaceAll("\\W", ""));
        
        System.out.println("[INFO] Generated Query:"+query);

        try {
            teamRankingList = new ArrayList<TeamRankingForm>();
            Connection con = JdbcConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                teamRanking = new TeamRankingForm();
                teamRanking.setId(rs.getLong("id"));
                teamRanking.setTeamId(rs.getLong("teamid"));
                teamRanking.setTeamName(getTeam(con, rs.getLong("teamid")).getName());
                teamRanking.setRank(rs.getLong("rank"));
                teamRanking.setRating(rs.getLong("rating"));
                
                teamRankingList.add(teamRanking);
                
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return teamRankingList;

    }

    /**
     * To get the Season at glance data of all teams by given tabName
     * 
     * @param tabName
     * @return
     */
    public static List<FixturesForm> getSeasonAtGlance(String tabName) {

        List<FixturesForm>fixturesFormList = null;
        FixturesForm fixturesForm = null;
        String query = SQLConstants.GET_SEASON_AT_GLANCE.replaceAll("#",tabName.toLowerCase().replaceAll("\\W", ""));
        
        System.out.println("[INFO] Generated Query:"+query);

        try {
            fixturesFormList = new ArrayList<FixturesForm>();
            Connection con = JdbcConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                fixturesForm = new FixturesForm();
                fixturesForm.setId(rs.getLong("id"));
                fixturesForm.setLeftTeamId(rs.getLong("leftTeamid"));
                fixturesForm.setRightTeamId(rs.getLong("rightTeamid"));
                fixturesForm.setLeftTeamName(getTeam(con, rs.getLong("leftTeamid")).getName());
                fixturesForm.setRightTeamName(getTeam(con, rs.getLong("rightTeamid")).getName());
                fixturesForm.setVenue(rs.getString("venue"));
                fixturesForm.setDate(rs.getString("date"));
                
                fixturesFormList.add(fixturesForm);
                
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return fixturesFormList;

    }

    /**
     * To get the Season at RESUTLS data of all teams by given tabName
     * 
     * @param tabName
     * @return
     */
    public static List<ResultsForm> getResults(String tabName) {

        List<ResultsForm>resultsFormList = null;
        ResultsForm resultsForm = null;
        String query = SQLConstants.GET_RESULTS.replaceAll("#",tabName.toLowerCase().replaceAll("\\W", ""));
        
        System.out.println("[INFO] Generated Query:"+query);

        try {
            resultsFormList = new ArrayList<ResultsForm>();
            Connection con = JdbcConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                resultsForm = new ResultsForm();
                resultsForm.setId(rs.getLong("id"));
                resultsForm.setLeftTeamId(rs.getLong("leftTeamid"));
                resultsForm.setRightTeamId(rs.getLong("rightTeamid"));
                resultsForm.setLeftTeamName(getTeam(con, rs.getLong("leftTeamid")).getName());
                resultsForm.setRightTeamName(getTeam(con, rs.getLong("rightTeamid")).getName());
                resultsForm.setWinningTeamId(rs.getLong("winningteamid"));
                resultsForm.setWinningTeamName(getTeam(con, rs.getLong("winningteamid")).getName());
                resultsForm.setWonBy(rs.getString("wonedby"));
                
                resultsFormList.add(resultsForm);
                
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultsFormList;

    }

    /**
     * Getting latest news
     * @param tabName
     * @return
     */
    public static List<LatestNewsForm> getLatestNews(String tabName) {

        List<LatestNewsForm>latestNewsFormList = null;
        LatestNewsForm latestNewsForm = null;
        String query = SQLConstants.GET_LATEST_NEWS;
        
        System.out.println("[INFO] Generated Query:"+query);

        try {
            latestNewsFormList = new ArrayList<LatestNewsForm>();
            Connection con = JdbcConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                latestNewsForm = new LatestNewsForm();
                
                latestNewsForm.setId(rs.getLong("id"));
                latestNewsForm.setNews(rs.getString("news"));
                
                latestNewsFormList.add(latestNewsForm);
                
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return latestNewsFormList;

    }
    
    
    /**
     * Getting pool info
     * @param tabName
     * @return
     */
    public static List<PoolForm> getPoolData(String poolTabName) {

        List<PoolForm>poolFormList = null;
        PoolForm poolForm          = null;
        String query               = SQLConstants.GET_POOL_INFO;
        String poolName            =poolTabName.substring(5);
        
        System.out.println("[INFO] Generated Query:"+query);

        try {
            poolFormList = new ArrayList<PoolForm>();
            Connection con = JdbcConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1,poolName.toLowerCase().replaceAll("\\W", ""));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                poolForm = new PoolForm();
                
                poolForm.setId(rs.getLong("id"));
                poolForm.setTeamId(rs.getLong("teamids"));
                poolForm.setTeamName(getTeam(con, rs.getLong("teamids")).getName());
                poolForm.setRr(rs.getString("rr"));
                poolFormList.add(poolForm);
                
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return poolFormList;

    }
    
    /**
     * Getting general statistics info
     * @param tabName
     * @return
     */
    public static List<GeneralStatsForm> getGeneralStats(String poolTabName) {

        List<GeneralStatsForm>generalStatsFormList = null;
        GeneralStatsForm generalStatsForm         = null;
        String query               = SQLConstants.GET_GENERAL_STATISTICS;
        
        
        System.out.println("[INFO] Generated Query:"+query);

        try {
            generalStatsFormList = new ArrayList<GeneralStatsForm>();
            Connection con = JdbcConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
          
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                generalStatsForm = new GeneralStatsForm();
                
                generalStatsForm.setId(rs.getLong("id"));
                generalStatsForm.setTeamId(rs.getLong("teamid"));
                generalStatsForm.setTeamName(getTeam(con, rs.getLong("teamid")).getName());
                generalStatsForm.setPlayed(rs.getLong("played"));
                generalStatsForm.setWon(rs.getLong("won"));
                generalStatsForm.setLoss(rs.getLong("loss"));
                generalStatsForm.setDraw(rs.getLong("draw"));
                generalStatsForm.setPoints(rs.getLong("points"));
                
                generalStatsFormList.add(generalStatsForm);
                
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return generalStatsFormList;

    }
  
    
    /**
     * Getting bestPlayers info
     * @param tabName
     * @return
     */
    public static List<BestPlayersForm> getBestPlayersInfo(String playerType) {

        List<BestPlayersForm>bestPlayersFormsList = null;
        BestPlayersForm bestPlayersForm         = null;
        String query               = SQLConstants.GET_BEST_PLAYERS_INFO;
        
        
        System.out.println("[INFO] Generated Query:"+query);

        try {
            bestPlayersFormsList = new ArrayList<BestPlayersForm>();
            Connection con = JdbcConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1,playerType.toUpperCase());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bestPlayersForm = new BestPlayersForm();
                
                bestPlayersForm.setId(rs.getLong("id"));
                bestPlayersForm.setTeamId(rs.getLong("teamid"));
                bestPlayersForm.setTeamName(getTeam(con, rs.getLong("teamid")).getName());
                bestPlayersForm.setTeamMemberId(rs.getLong("teammemberid"));
                bestPlayersForm.setTeamMemberName(getNextTeamMember(rs.getLong("teamid"), rs.getLong("teammemberid"),con).getmName());
                bestPlayersForm.setRuns(rs.getLong("runs"));
                bestPlayersForm.setMatches(rs.getLong("matches"));
                bestPlayersForm.setHs(rs.getLong("hs"));
                bestPlayersForm.setWickets(rs.getLong("wickets"));
                
                
                bestPlayersFormsList.add(bestPlayersForm);
                
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bestPlayersFormsList;

    }
  
    
    
}
