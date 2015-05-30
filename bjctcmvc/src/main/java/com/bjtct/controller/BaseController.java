/*
 * Copyright 2013 SanMoon
 *
 * Licensed under the SanMoon License, Version No (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.sanmoon.com
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *  
 * 
 * --------------------------------------------------------------------------------------------
 * 
 */
package com.bjtct.controller;

/**
 * 
 */
import java.io.File;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bjtct.dao.TeamDataDAO;
import com.bjtct.form.BestPlayersForm;
import com.bjtct.form.FixturesForm;
import com.bjtct.form.GeneralStatsForm;
import com.bjtct.form.LatestNewsForm;
import com.bjtct.form.PoolForm;
import com.bjtct.form.ResultsForm;
import com.bjtct.form.TeamForm;
import com.bjtct.form.TeamMemberForm;
import com.bjtct.form.TeamRankingForm;
import com.bjtct.pojo.Team;

@Controller
@RequestMapping("/team")
public class BaseController {

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String printWelcome(ModelMap model) throws SQLException {

        // got all team list
        List<Team> teamList = TeamDataDAO.getAllTeams();
        List<TeamForm> teamFormList = new ArrayList<TeamForm>();
        TeamForm tf = null;
        try {
            // preparing team form
            if (null != teamList && teamList.size() > 0) {

                for (Team t : teamList) {
                    tf = new TeamForm();
                    Blob b = t.getLogo();
                    byte[] blobAsBytes = b.getBytes(1, (int) b.length());
                    byte[] encoded = Base64.encodeBase64(blobAsBytes);
                    String pic = new String(encoded);

                    tf.setId(t.getId());
                    tf.setLogo(pic);
                    teamFormList.add(tf);
                    // model.addAttribute(t.getId().toString(), pic);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute(teamFormList);
        System.out.println(teamList.size());
        return "teams/home";

    }

    @RequestMapping(value = "/getTeamPage", method = RequestMethod.GET)
    public String getTeam(@RequestParam("teamId") long teamId, ModelMap model) throws SQLException {
        // Connection conn = JdbcConnection.getConnection();
        Team t = TeamDataDAO.getTeam(teamId);
        Long totalRecords = TeamDataDAO.getTotalRecordsCount(teamId);
        Long firstRecordId = TeamDataDAO.getFirstRecordId(teamId);
        System.out.println("Team Name:" + t.getName());
        model.addAttribute("firstRecordId", firstRecordId);
        model.addAttribute("totalRecordsCount", totalRecords);
        model.addAttribute("teamId", t.getId());
        model.addAttribute("team-name", t.getName());
        model.addAttribute("describe", t.getDescribe());
        return "teams/" + t.getName().toLowerCase().replaceAll("\\s+", "");

    }

    @RequestMapping(value = "/getNextTeamMember", method = RequestMethod.GET, headers = "Accept=*/*")
    @ResponseBody
    public String getTeamMember(HttpServletResponse response, @RequestParam("teamId") long teamId, @RequestParam("teamMemberId") long teamMemberId, ModelMap model)
            throws SQLException {
        String teamString = null;
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache, max-age=0,must-revalidate, no-store");
        response.setHeader("Expires", "-1");

        try {

            TeamMemberForm tm = TeamDataDAO.getNextTeamMember(teamId, teamMemberId);

            Team t = TeamDataDAO.getTeam(teamId);

            System.out.println("TeamId:" + teamId);
            System.out.println("TeamMemberId:" + teamMemberId);
            System.out.println("TeamMember Name:" + tm.getmName());

            model.addAttribute("team-name", t.getName());
            model.addAttribute("teamId", teamId);

            teamString = new ObjectMapper().writeValueAsString(tm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teamString;

    }

    @RequestMapping(value = "/addTeam", method = RequestMethod.GET, headers = "Accept=*/*")
    @ResponseBody
    public String addTeam(@RequestParam("name") String name, @RequestParam("logo") File logo, @RequestParam("desc") String desc, @RequestParam("photo") File photo) {
        System.out.println(name);
        System.out.println(logo);
        System.out.println(desc);
        System.out.println(photo);
        return "success";
    }
    
    /**
     * To get the get rating for the team wise
     * 
     * */
    @RequestMapping(value = "/getRatings", method = RequestMethod.GET, headers = "Accept=*/*")
    @ResponseBody
    public String getTeamRatings(@RequestParam("tabName") String tabName) {

        List<TeamRankingForm> teamRankinglist = null;
        String teamRankingString = null;
        try {
            teamRankinglist = new ArrayList<TeamRankingForm>();
            System.out.println("[INFO] Getting team ratings for :" + tabName);
            
            teamRankinglist = TeamDataDAO.getRanking(tabName);
            teamRankingString = new ObjectMapper().writeValueAsString(teamRankinglist);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teamRankingString;
    }
    
    @RequestMapping(value = "/getSeasonAtGlance", method = RequestMethod.GET, headers = "Accept=*/*")
    @ResponseBody
    public String getSeasonAtGlance(@RequestParam("tabName") String tabName) {

        
        List<FixturesForm>fixturesFormList = null;
        List<ResultsForm>resultsFormList = null;
        String listString = null;
        String resultsFormListString = null;
        
        try {
            if(tabName!=null && tabName.equalsIgnoreCase("Fixtures")){
            fixturesFormList = new ArrayList<FixturesForm>();
            System.out.println("[INFO] Getting team ratings for :" + tabName);
            
            fixturesFormList = TeamDataDAO.getSeasonAtGlance(tabName);
            listString = new ObjectMapper().writeValueAsString(fixturesFormList);
            }
            else if(tabName!=null && tabName.equalsIgnoreCase("Results")){
                resultsFormList = new ArrayList<ResultsForm>();
                System.out.println("[INFO] Getting team ratings for :" + tabName);
                
                resultsFormList = TeamDataDAO.getResults(tabName);
                listString = new ObjectMapper().writeValueAsString(resultsFormList);
                    
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listString;
    }
    
    /**
     *Getting latest news 
     */
    
    @RequestMapping(value = "/getLatestNews", method = RequestMethod.GET, headers = "Accept=*/*")
    @ResponseBody
    public String getLatestNews(@RequestParam("tabName") String tabName) {

        
        
        String listString = null;
        List<LatestNewsForm> latestNewsList = null;
        
        try {
            
                System.out.println("[INFO] Getting LestNews...");
                
                latestNewsList = TeamDataDAO.getLatestNews(tabName);
                listString = new ObjectMapper().writeValueAsString(latestNewsList);
                    
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listString;
    }
    
    
    /**
     * To get Pool related data
     * 
     * */
    @RequestMapping(value = "/getPoolInfo", method = RequestMethod.GET, headers = "Accept=*/*")
    @ResponseBody
    public String getPoolData(@RequestParam("poolTabName") String poolTabName) {

        List<PoolForm> teamPoolList = null;
        String teamRankingString = null;
        try {
            teamPoolList = new ArrayList<PoolForm>();
            System.out.println("[INFO] Getting Pool info for :" + poolTabName);
            
            teamPoolList = TeamDataDAO.getPoolData(poolTabName);
            teamRankingString = new ObjectMapper().writeValueAsString(teamPoolList);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teamRankingString;
    }

    /**
     * To get General statistics related data
     * 
     * */
    @RequestMapping(value = "/getGeneralStats", method = RequestMethod.GET, headers = "Accept=*/*")
    @ResponseBody
    public String getGeneralStats(@RequestParam("tabName") String tabName) {

        List<GeneralStatsForm> generalStatsFormList = null;
        String generalStatsFormString = null;
        try {
            generalStatsFormList = new ArrayList<GeneralStatsForm>();
            System.out.println("[INFO] Getting general Statistics info for :" + tabName);
            
            generalStatsFormList = TeamDataDAO.getGeneralStats(tabName);
            generalStatsFormString = new ObjectMapper().writeValueAsString(generalStatsFormList);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return generalStatsFormString;
    }
    
    
    /**
     * To get Bestplayers related data
     * 
     * */
    @RequestMapping(value = "/getBestPlayersInfo", method = RequestMethod.GET, headers = "Accept=*/*")
    @ResponseBody
    public String getBestPlayersInfo(@RequestParam("playerType") String playerType) {

        List<BestPlayersForm> bestPlayersFormsList = null;
        String bestPlayersString = null;
        try {
            bestPlayersFormsList = new ArrayList<BestPlayersForm>();
            System.out.println("[INFO] Getting BestPlayers info for :" + playerType);
            
            bestPlayersFormsList = TeamDataDAO.getBestPlayersInfo(playerType);
            bestPlayersString = new ObjectMapper().writeValueAsString(bestPlayersFormsList);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bestPlayersString;
    }
    
    

}